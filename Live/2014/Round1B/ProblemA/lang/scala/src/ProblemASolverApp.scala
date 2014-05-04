import scala.io.Source
import java.io._
import scala.math.{round, abs}
import scala.util.Sorting._

object ProblemASolverApp extends App {
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
  
  def solve(floatStringCount: Float, strings: List[String]): String = {
    def toNextCharCountAndRemainder(nextStr: List[Char]): (Char, Int, List[Char]) = {
      def countNextChar(ch: Char, count: Int, rem: List[Char]): (Char, Int, List[Char]) = {
        rem match {
          case Nil => (ch, count, rem)
          case nextCh :: remChars if nextCh == ch => countNextChar(ch, count + 1, remChars)
          case _ :: remChars => (ch, count, rem)  
        }
      }
      
      nextStr match {
        case Nil => ('_', 0, nextStr)  // shouldn't happen
        case ch :: Nil => (ch, 1, Nil)
        case ch :: rem => countNextChar(ch, 1, rem)
      }
    }
    
    def countMoves(moveCount: Int, charCountsAndRemainders: List[(Char, Int, List[Char])]): Option[Int] = {
      if (charCountsAndRemainders.isEmpty) {
        Some(moveCount)
      } else {
        val nextChar = charCountsAndRemainders(0)._1
        if (charCountsAndRemainders.exists(_._1 != nextChar)) {
          None  // One of the strings is missing a character
        } else {
          // Use median not mean to get best target value
          // (during the live competition I used the mean, but the median is minimal, 
          // because if a target value is not at the median, then more than half of the numbers 
          // will reduce their distance to the target value if we move it closer to the median).
          // NB: If there are 2 medians, then I believe either can be used (or any value between them)
          val sortedCharCounts = charCountsAndRemainders.map(_._2).toArray
          quickSort(sortedCharCounts)
          val median = sortedCharCounts(sortedCharCounts.length / 2)
          val newMoveCount = sortedCharCounts.foldLeft(0) (
              (mCount: Int, chCount: Int) => mCount + math.abs(chCount - median)
          )
          val remCharLists = charCountsAndRemainders.map(_._3)
          if (remCharLists.forall(_.isEmpty)) {
            Some(moveCount + newMoveCount)
          } else {
            val remCharCountsAndRemainders = remCharLists.map(toNextCharCountAndRemainder(_))
            countMoves(moveCount + newMoveCount, remCharCountsAndRemainders)
          }
        }
      }
    }
    
    val firstCharCountsAndRemainders = strings.map {
      case str => (toNextCharCountAndRemainder(str.toList))
    }
    val answer = countMoves(0, firstCharCountsAndRemainders)
    answer match {
      case None => "Fegla Won"
      case Some(minMoves) => minMoves.toString
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
            val strings = (1 to stringCount).toList.map(num => lines.next()) 
          
            val solution = solve(stringCount, strings)
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