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

  def solve(k: Int, c: Int, s: Int): Option[Seq[BigInt]] = {
    val minS = (k + c - 1) / c
    if (s < minS) None else {
      val originalPositions = 0.until(k)  // NB: zero-based
      val positionsAtEachStage = originalPositions.sliding(c, c)
      val finalPositions = positionsAtEachStage.map(getFinalFractilePosition(_, k)).toSeq
      Some(finalPositions)
    }
  }

  def getFinalFractilePosition(originalPositions: Seq[Int], k: Int): BigInt =
    originalPositions.foldLeft(BigInt(0)){(acc, pos) => k * acc + pos} + 1
    /* Each foldLeft iteration calculates the zero-based position of the cell at the next level of complexity down.
     * Make it 1-based at the end.
     */
}
