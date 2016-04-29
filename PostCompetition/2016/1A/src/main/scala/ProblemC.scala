import scala.io._
import java.io._
import java.util.Scanner

import scala.annotation.tailrec
import scala.collection._
import scala.collection.immutable.List

object ProblemC {
  def main(args: Array[String]): Unit = {
    processFiles(args(0), args(1))
  }

  def processFiles(inputFilePath: String, outputFilePath: String): Unit = {
    val bufferedSource = Source.fromFile(inputFilePath)
    try {
      val lines = bufferedSource.getLines()
      val outputFile = new File(outputFilePath)
      val bw = new BufferedWriter(new FileWriter(outputFile))
      try {
        val testCaseCount = lines.next().toInt
        for (testCase <- 1 to testCaseCount) {
          println(s"Test case: $testCase")
          val n = lines.next().toInt
          val line = lines.next()
          val scanner = new Scanner(line)
          val friends = (0 until n).map(i => scanner.nextInt() - 1) // NB: it's zero-based
          val solution = solve(n, friends)
          val solStr = s"Case #$testCase: $solution"
          println(solStr)
          println()
          bw.write(solStr)
          bw.newLine()
        }
      } finally {
        bw.flush()
        bw.close()
      }
    } finally {
      bufferedSource.close()
    }
  }

  // Use case classes to define enumerations for each path starting at a particular node:
  sealed trait Path { def length = 0 }

  // An invalid path leads to a cycle, without being part of the cycle:
  final case object Invalid extends Path
  // A loop is a pair of mutual best friends:
  final case class Loop(mutualBestFriend: Int) extends Path {
    override val length = 2
    override def toString = s"Loop(${mutualBestFriend + 1})"
  }
  // A lasso is a path that ends in a loop (a pair of mutual best friends):
  final case class Lasso(override val length: Int, loopEntryPoint: Int) extends Path {
    override def toString = s"Lasso(length: $length, entryPoint: ${loopEntryPoint + 1})"
  }
  final case class Cycle(override val length: Int) extends Path

  // Temporary states while paths are being expanded...

  // A PartialCycle is used to unwind the stack back to "friendAtStartOfCycle":
  final case class PartialCycle(override val length: Int, friendAtStartOfCyle: Int,
                                nodesOnCycle: List[Int]) extends Path {
    override def toString = s"PartialCycle($length, ${friendAtStartOfCyle + 1}, ${nodesOnCycle.map(_ + 1)})"
  }
  final case object Unexpanded extends Path
  final case object Expanding extends Path
    // Used to track status of being on the expansion path.
    // When we follow a path to a node that is already in an Expanding state, we have found a cycle or a loop.

  // The Line class tracks the longest line that can be made with a single loop at its centre or at the end:
  // NB: All such lines can be strung together to create a valid solution.
  case class Line(leftLoopIndex: Int, rightLoopIndex: Int, // NB: leftLoopIndex < rightLoopIndex
                  leftLineLength: Int = 0, rightLineLength: Int = 0) {
    // NB: The line length excludes the loop (i.e. the two mutual best friends are counted separately)
    val length = 2 + leftLineLength + rightLineLength
    override def toString =
      s"Line(${leftLoopIndex + 1}, ${rightLoopIndex + 1}, left len: $leftLineLength, right len: $rightLineLength)"
  }

  val solve = solveFast _

  def solveBoth(n: Int, friends: IndexedSeq[Int]): Int = {
    val bruteForce = solveByBruteForce(n, friends)
    val fast = solveFast(n, friends)
    if (bruteForce != fast) {
      println("******")
      println("WARNING: brute force and fast solutions differ!")
      println()
    }
    bruteForce
  }

  def solveByBruteForce(n: Int, friends: IndexedSeq[Int]): Int = {
    List.range(n, 1, -1).find { size =>
      (0 until n).combinations(size).exists { comb =>
        comb.tail.permutations.map( comb.head +: _).exists { perm =>
          val isValid = (0 until size).forall { i =>
            val beforeIndex = (size + i - 1) % size
            val afterIndex = (i + 1) % size
            val child = perm(i)
            val friend = friends(child)
            perm(beforeIndex) == friend || perm(afterIndex) == friend
          }
          if (isValid) {
            println(s"Size: $size; items: " + perm.map(_+1).mkString(" -> "))
          }
          isValid
        }
      }
    }.get  // Okay, since there must be a solution
  }

  def solveFast(n: Int, friends: IndexedSeq[Int]): Int = {
    val paths = mutable.ArrayBuffer.fill[Path](n)(Unexpanded)

    def expandPath(nodeToExpand: Int): Path = {
      val cachedPath = paths(nodeToExpand)
      if (cachedPath != Unexpanded) cachedPath else {
        paths(nodeToExpand) = Expanding
        val bestFriend = friends(nodeToExpand)
        val bfPath = expandPath(bestFriend)
        val expandedPath = bfPath match {
          case PartialCycle(len, startOfCycle, subNodes) if startOfCycle == nodeToExpand => {
            val cycle = Cycle(len + 1)
            subNodes.foreach(paths(_) = cycle)
            cycle
          }
          case PartialCycle(len, startOfCycle, subNodes) =>
            PartialCycle(len + 1, startOfCycle, nodeToExpand :: subNodes)
          case Cycle(_) => Invalid
          case Loop(bf) if bf == nodeToExpand => Loop(bestFriend)
          case Loop(_) => Lasso(3, bestFriend)
          case Lasso(len, entry) => Lasso(len + 1, entry)
          case Invalid => Invalid
          case Expanding if friends(bestFriend) == nodeToExpand => Loop(bestFriend)
          // The other node will get counted on unwinding the stack
          case Expanding => PartialCycle(1, bestFriend, List(nodeToExpand))
          case _ => throw new Exception("Should never occur")
        }

        paths(nodeToExpand) = expandedPath
        println(s"${nodeToExpand + 1} := $expandedPath")
        expandedPath
      }
    }

    // Expand the paths, and also track the maximum simple path length (i.e. a cycle, loop or lasso):
    val maxSimplePathLength = (0 until n).map(expandPath(_).length).max

    def getLongestLassoLineLengthsByEntryPoint() = {
      // Find longest lassos for each entry point into a loop:
      val lassoLengthsByEntryPoint = paths.collect {
        case Lasso(len, entry) => entry -> (len - 2)
      }.groupBy(_._1)
      lassoLengthsByEntryPoint.map {
        case (entry, entryLengths) => entry -> entryLengths.map(_._2).max
      }
    }

    val maxLineLengthsByEntryPoint = getLongestLassoLineLengthsByEntryPoint()
    println(s"max lasso line lengths by loop entry point (zero-based): $maxLineLengthsByEntryPoint")

    // There could be pairs of lassos which share a common loop and can be joined together at the common pair:
    val linesByLeftIndex = paths.zipWithIndex.collect {
      case (Loop(entry), index) if entry < index =>
        Line(entry, index,
          maxLineLengthsByEntryPoint.getOrElse(entry, 0),
          maxLineLengthsByEntryPoint.getOrElse(index, 0))
    }
    println(s"linesByLeftIndex: $linesByLeftIndex")

    val sumOfLineLengths = if (linesByLeftIndex.isEmpty) 0 else linesByLeftIndex.map(_.length).sum
    math.max(maxSimplePathLength, sumOfLineLengths)
  }
}
