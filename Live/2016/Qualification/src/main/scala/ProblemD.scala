import scala.io._
import java.io._

object ProblemD {
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
          val line = lines.next()
          val kcsList = line.split(" ").map(_.toInt)
          val (k, c, s) = kcsList match {
            case Array(ki, ci, si) => (ki, ci, si)
          }
          val solution = solve(k, c, s)
          val sol = solution match {
            case None => "IMPOSSIBLE"
            case Some(tiles) => tiles.mkString(" ")
          }
          bw.write(s"Case #$testCase: $sol")
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

  def solve(k: Int, c: Int, s: Int): Option[List[BigInt]] = None
}
