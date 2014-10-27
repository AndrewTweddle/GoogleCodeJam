import scala.io.Source
import java.io._
import java.util.Scanner
import scala.collection.immutable.Map
import scala.collection.immutable.Set
import scala.annotation.tailrec

object ProblemBSolverApp extends App {
  lazy val modulus: Long = 1000000007L 
  implicit val repetitions: Int = 1

  def time[R](block: => R)(implicit repetitions: Int): R = {
    val t0 = System.nanoTime()
    var result: R = block
    var repsLeft = repetitions - 1
    while (repsLeft > 0) {
      result = block // call-by-name
      repsLeft -= 1
    }
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) / 1.0e6 / repetitions + " ms")
    result
  }
  
  class LongWrapper(value: Long) {
	// A binary operator to multiply two numbers and then reduce them modulo the modulus variable
	def *%(that: Long): Long = {
	  ( value * that ) % modulus
	}
  }
  
  // A type-converter to allow the binary operator to be used on a long directly: 
  implicit def longToLongWrapper(value: Long): LongWrapper = new LongWrapper(value)
  
  def factorial(n: Int): Long = {
    
    @tailrec
    def tailFactorial(accumulator: Long, subN: Long): Long = {
      if (subN <= 1) {
        accumulator
      } else {
        tailFactorial( subN *% accumulator, subN - 1)
      }
    }
    
    tailFactorial(1, n)
  }
  
  // A graph will be created with a Node per letter (or rather per sequence of identical letters within a set).
  // Sets consisting of a single letter will be treated as self-edges linking the node back to itself.
  // Sets containing one or more consecutive identical letters will contribute a node per such letter.
  // isInternalInSet is used to track whether the sequence of identical letters occurs in the interior of a set.
  // If so, then selfEdgeSetCount must be zero. If not, then self-edges are permissible.
  // Only a single previous node and next node need to be tracked, since multiple indicate no feasible solution.
  case class Node(
    thisChar: Char,
    selfEdgeSetCount: Int = 0, selfEdgeCharCount: Int = 0,
    otherEdgeCharCount: Int = 0, isInternalInSet: Boolean = false, 
    prevChar: Option[Char] = None, nextChar: Option[Char] = None) {
    
    def getPermutationCountOfSelfEdges(): Long = factorial(selfEdgeSetCount)
    
    def getPermutationCountOfNodesStartingHere(graph: Map[Char, Node]): Long = {
      val followingNodesCount = nextChar match {
        case Some(next) => {
          val nextNode = graph(next)
          nextNode.getPermutationCountOfNodesStartingHere(graph)
        }
        case None => 1
      } 
      followingNodesCount *% getPermutationCountOfSelfEdges() 
    }
  }
  
  // A subset represents a sequence of consecutive identical characters within a set:
  case class SubSet(thisChar: Char, charCount: Int = 1, 
    prevChar: Option[Char] = None, nextChar: Option[Char] = None)
  
  def splitIntoSubSetsInReverseOrder(set: String): List[SubSet] = {
    set.foldLeft[List[SubSet]](List.empty) { (subSets, thisChar) =>
      subSets match {
        case Nil => List(SubSet(thisChar))
        case SubSet(tc, cc, pc, None) :: ss if tc == thisChar =>
          SubSet(tc, cc+1, pc, None) :: ss
        case SubSet(tc, cc, pc, None) :: ss =>
          SubSet(thisChar, 1, Some(tc)) :: SubSet(tc, cc, pc, Some(thisChar)) :: ss
      }
    }
  }
  
  def addSubSetToGraph(graphOrNone: Option[Map[Char, Node]], subset: SubSet): Option[Map[Char, Node]] = {
    graphOrNone match {
      case None => None  // A missing graph indicates that the problem is invalid (previously determined)
      case Some(graph) => {
        var node = if (graph.contains(subset.thisChar)) graph(subset.thisChar) else Node(subset.thisChar)
        subset match {
          case SubSet(thisChar, charCount, None, None) => 
            // This subset represents a self-edge (a set consisting of all identical letters).
            
            // Check if a previous set has this char in the middle, in which case there is no solution:
            if (node.isInternalInSet) None else {
              node = node.copy(selfEdgeSetCount = node.selfEdgeSetCount + 1, 
                selfEdgeCharCount = node.selfEdgeCharCount + charCount)
              Some(graph + (thisChar -> node))
            }
            
          case SubSet(thisChar, charCount, prevChar, nextChar) => { 
            // This subset is part of a large set.
            val hasPrevChar = prevChar != None
            val hasNextChar = nextChar != None
            val isInternalInSet = hasPrevChar && hasNextChar
            
            // Check that the node doesn't already have a next or previous node: 
            if (hasNextChar && node.nextChar != None) None 
            else if (hasPrevChar && node.prevChar != None) None
            else if (isInternalInSet && node.selfEdgeSetCount > 0) None
            else {
              node = node.copy(otherEdgeCharCount = subset.charCount, 
                prevChar = if (hasPrevChar) prevChar else node.prevChar, 
                nextChar = if (hasNextChar) nextChar else node.nextChar,
                isInternalInSet = if (isInternalInSet) true else node.isInternalInSet)
              Some(graph + (thisChar -> node))
            }
          }
        }
      }
    }
  }
  
  def buildGraphOrNone(sets: List[String]): Option[Map[Char, Node]] = {
    val allSubSets = sets flatMap splitIntoSubSetsInReverseOrder
    val emptyGraph = Map[Char, Node]()
    val emptyGraphOrNone = Some(emptyGraph)
    val graph = allSubSets.foldLeft[Option[Map[Char, Node]]](emptyGraphOrNone)(
      (g : Option[Map[Char, Node]], ss: SubSet) => addSubSetToGraph(g,ss)
    )
    graph    
  }
  
  def hasCycles(graph: Map[Char, Node]): Boolean = {
    var untraversed = graph.keySet.toSet
    
    def traverseCharAndTestForCycle(nextChar: Char): Boolean = {
      if (untraversed.contains(nextChar)) {
        untraversed = untraversed - nextChar
        val nextNode = graph(nextChar)
        nextNode.nextChar match {
          case None => false
          case Some(nextCh) => traverseCharAndTestForCycle(nextCh)
        }
      } else {
        // nextChar is not in the untraversed list. 
        // That means it's already been visited before i.e. a cycle
        // (though this shouldn't be possible, but rather play it safe)
        true
      }
    }
    
    var charsToTraverse = graph.values.filter(_.prevChar == None).map(_.thisChar)
    val isCycleDetected = charsToTraverse.exists(ch => traverseCharAndTestForCycle(ch))
    if (isCycleDetected) true else {
      // There could be a cycle Any untraversed nodes must be part of a cycle:
      !untraversed.isEmpty
    }
  }
  
  def getValidPermutationCount(graph: Map[Char, Node]): Long = {
    if (hasCycles(graph)) 0 else {
	  val permutationCountsByStartChar 
	    = graph.values
               .filter(_.prevChar == None)  // Only consider letters which start a chain
               .map(_.getPermutationCountOfNodesStartingHere(graph))
      val combinedPermutationCount = permutationCountsByStartChar.reduce(_ *% _)
      val disconnectedSubGraphCount = permutationCountsByStartChar.size
      factorial(disconnectedSubGraphCount) *% combinedPermutationCount
    }
  }
  
  def solve(sets: List[String]): Long = {
    val graphOrNone = buildGraphOrNone(sets)
    graphOrNone match {
      case None => 0
      case Some(graph) => getValidPermutationCount(graph)
    }
  }
  
  def solveAll(inputFilePath: String, outputFilePath: String) {
    
    val bufferedSource = Source.fromFile(inputFilePath)
    try {
      val lines = bufferedSource.getLines()
      val outputFile = new File(outputFilePath)
      val bw = new BufferedWriter(new FileWriter(outputFile))
      try {
        time {
          val testCaseCount = lines.next().toInt
          for (testCase <- 1 to testCaseCount) {
            val stringCount = lines.next().toInt
            val line = lines.next()
            val strings = line.split(" ").toList
            val solution = solve(strings)
            bw.write(s"Case #$testCase: $solution") 
            bw.newLine()
          }
        }
      } finally {
        bw.flush()
        bw.close()
      }
    } finally {
      bufferedSource.close()
    }
  }
  
  val inputFile = args(0)
  val outputFile = args(1)
  solveAll(inputFile, outputFile)
}