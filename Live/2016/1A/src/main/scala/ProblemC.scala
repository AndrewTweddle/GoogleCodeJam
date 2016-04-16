import scala.io._
import java.io._
import java.util.Scanner
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
          val n = lines.next().toInt
          val line = lines.next()
          val scanner = new Scanner(line)
          val friends = immutable.Map((1 to n).map(i => i -> scanner.nextInt()): _*)
          val solution = solve(n, friends)
          bw.write(s"Case #$testCase: $solution")
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

  case class Cycle(val first: Int, val last:Int, val length: Int)

  def solve(n: Int, friends: immutable.Map[Int, Int]): Int = {
    val emptyCycles = List[Cycle]()
    val cycles = friends.foldLeft(emptyCycles) { (cycs: List[Cycle], friendship) =>
      val updatedCycs = cycs.map {
        case Cycle(f, l, len) if l == friendship._1  => Cycle(f, friendship._2, len)
        case x @ Cycle(_, _, _) => x
      }
      Cycle(friendship._1, friendship._2, 2) :: updatedCycs
    }
    cycles.foldLeft(0) { (bestLen: Int, cyc: Cycle) =>
      if ((cyc.first == cyc.last) && (cyc.length > bestLen)) cyc.length else bestLen
    }
  }
}
