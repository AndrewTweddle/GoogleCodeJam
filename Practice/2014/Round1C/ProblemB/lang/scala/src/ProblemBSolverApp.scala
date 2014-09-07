import scala.io.Source
import java.io._
import java.util.Scanner
import scala.collection.mutable.Map

object ProblemBSolverApp extends App {
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
  
  def factorial(n: Long): Long = {
    if (n == 0) {
      1
    } else {
      n * factorial(n-1)
    }
  }
  
  def checkWordHasOnlyContiguousLetters(word: List[Char]): Boolean = {
    val charMap = Map[Char, Boolean]()
    var lastChar: Char = ' '
    word.forall {
      case ch: Char => if (lastChar == ch)
      {
        true
      } else if (charMap.contains(ch)) {
        false
      } else {
        charMap.update(ch, true)
        lastChar = ch
        true
      }
    }
  }
  
  def mapStringsAndCheck(strings: List[String], sameSAndE: Map[Char, List[String]], diffSAndEByStart: Map[Char, String], 
      diffSAndEByEnd: Map[Char, String]): Boolean = {
    strings.forall {
      case s => {
        val start = s(0)
        val end = s(s.length() - 1)
        if (start == end) {
          if (sameSAndE.contains(start)) {
            sameSAndE.update(start, s :: sameSAndE(start))
          } else {
            sameSAndE.update(start, List(s))
          }
          true
        } else {
          if (diffSAndEByStart.contains(start) || diffSAndEByEnd.contains(end))
          {
            false
          } else {
            diffSAndEByStart.update(start, s)
            diffSAndEByEnd.update(end, s)
            true
          }
        }
      }
    }
  }
  
  case class CombinationCount(sampleString: String, count: Long)
  
  def calculateCombinationsFromSameStartAndEndBlocks(sameSAndE: Map[Char, List[String]], 
    diffSAndEByEnd: Map[Char, String]): Map[Char, CombinationCount] = {
    
    val result = Map[Char, CombinationCount]()
    diffSAndEByEnd.keys.foreach {
      case ch: Char => {
        val startStr = diffSAndEByEnd(ch)
        val combCount = if (sameSAndE.contains(ch)) {
          val strings = sameSAndE(ch) 
          val count = factorial(strings.size)
          val sample = strings.foldLeft(startStr)(_ + _)
          CombinationCount(sample, count)
        } else {
          CombinationCount(startStr, 1)
        }
        result.update(ch, combCount)
      }
    }
    result
  }
  
  def tryCombineStrings(combCountByEnd: Map[Char, CombinationCount]): Boolean = {
    val keys = combCountByEnd.keys.toList
    var combined = false
    keys foreach {
      case end => {
        // First check if contained, as it might have been combined with another string already:
        if (combCountByEnd.contains(end)) {
          val combCount = combCountByEnd(end)
          val start = combCount.sampleString(0)
          if (combCountByEnd.contains(start)) {
            // Combine the 2 strings and update the map
            val firstCombCount = combCountByEnd(start)
            val newSampleString = firstCombCount.sampleString + combCount.sampleString
            val newCount = firstCombCount.count * combCount.count
            val newCombinedCount = CombinationCount(newSampleString, newCount)
            combCountByEnd.remove(start)
            combCountByEnd.update(end, newCombinedCount) 
            combined = true
          } else {
            // Do nothing as this is the start of a block
          }
        }
      }
    }
    combined
  }
  
  def fixUpRepeatingInitialBlock(sameSAndE: Map[Char, List[String]], 
    combCountByEnd: Map[Char, CombinationCount]): Unit = {
    
    combCountByEnd.keys foreach {
      case end => {
        val combCount = combCountByEnd(end)
        val start = combCount.sampleString(0)
        if (sameSAndE.contains(start)) {
          val strings = sameSAndE(start) 
          val count = factorial(strings.size)
          val sample = strings.reduce(_ + _)
          val newCombCount = CombinationCount(sample + combCount.sampleString, count * combCount.count)
          combCountByEnd.update(end, newCombCount)
          sameSAndE.remove(start)
       }
      }
    }
  }
  
  def combineStrings(sameSAndE: Map[Char, List[String]], combCountByEnd: Map[Char, CombinationCount]): Unit = {
    while (tryCombineStrings(combCountByEnd)) {}
    fixUpRepeatingInitialBlock(sameSAndE, combCountByEnd)
  }
  
  def solve(strings: List[String]): Option[Long] = {
    if (!strings.map(_.toList).forall(checkWordHasOnlyContiguousLetters))
    {
      None
    } else {
      val sameSAndE = Map[Char, List[String]]()
      val diffSAndEByStart = Map[Char, String]()
      val diffSAndEByEnd = Map[Char, String]()
      
      val isValid = ProblemBSolverApp.mapStringsAndCheck(strings, sameSAndE, diffSAndEByStart, diffSAndEByEnd)
      if (!isValid) {
        None
      } else {
        val combCounts = ProblemBSolverApp.calculateCombinationsFromSameStartAndEndBlocks(sameSAndE, diffSAndEByEnd)
        ProblemBSolverApp.combineStrings(sameSAndE, combCounts)
        var sampleString = "";
        val combinations = combCounts.keys.foldLeft[Long](1) {
          case (count: Long, key: Char) => {
            val combCount = combCounts(key)
            sampleString = sampleString + combCount.sampleString
            count * combCount.count
          }
        }
        if (!checkWordHasOnlyContiguousLetters(sampleString.toList)) {
          None
        } else {
          Some(combinations)
        }
      }
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
          val testCaseCount = lines.next.toInt
          for (testCase <- 1 to testCaseCount) {
            val stringCount = lines.next().toInt
            val line = lines.next()
            val strings = line.split(" ").toList
            val solution = solve(strings)
            
            val sol = solution match {
              case None => "0"
              case Some(sol) => sol % 1000000007 
            }
            
            bw.write(s"Case #$testCase: $sol") 
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