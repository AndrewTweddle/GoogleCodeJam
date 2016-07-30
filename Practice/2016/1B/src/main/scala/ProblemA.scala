import scala.io._
import java.io._

object ProblemA {
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
        val testCaseCount = lines.next.toInt
        for (testCase <- 1 to testCaseCount) {
          val string = lines.next().trim()
          /*
          val solution = solve(strings)
          val sol = solution match {
            case None => "0"
            case Some(sol) => sol
          }
          bw.write(s"Case #$testCase: $sol")
          */
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

  def solve(input: List[String]): Option[String] = None
}
