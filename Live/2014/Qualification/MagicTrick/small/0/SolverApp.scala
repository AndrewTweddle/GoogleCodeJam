import scala.io.Source
import java.io._

object SolverApp extends App {
  val ROWS = 4
  val COLUMNS = 4

  def solveAll(inputFilePath: String, outputFilePath: String) {
    
    def extractAnswerRow(lines: Iterator[String]): Set[Int] = {
      val answerRowNumber = lines.next().toInt
      val rows = (1 to ROWS).map(_ => lines.next())
      val answerRow = rows(answerRowNumber - 1)
      answerRow.split(raw"\s+").map(_.toInt).toSet
    }
    
    def solve(answer1Numbers: Set[Int], answer2Numbers: Set[Int]): String = {
      val commonNumbers = answer1Numbers intersect answer2Numbers
      commonNumbers.size match {
        case 0 => "Volunteer cheated!"
        case 1 => commonNumbers.iterator.toList(0).toString
        case _ => "Bad magician!" 
      }
    }

    val bufferedSource = Source.fromFile(inputFilePath)
    try {
      val lines = bufferedSource.getLines()
      val outputFile = new File(outputFilePath)
      val bw = new BufferedWriter(new FileWriter(outputFile))
      try {
        val testCaseCount = lines.next.toInt
        for (testCase <- 1 to testCaseCount) {
          val answer1Numbers = extractAnswerRow(lines)
          val answer2Numbers = extractAnswerRow(lines)
          val solution = solve(answer1Numbers, answer2Numbers)
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
  
  val inputFile = args(0)
  val outputFile = args(1)
  solveAll(inputFile, outputFile)
}