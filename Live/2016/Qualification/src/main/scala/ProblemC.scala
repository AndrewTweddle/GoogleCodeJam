import scala.io._
import java.io._
import java.util.Scanner

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
          val line = lines.next()
          val scanner = new Scanner(line)
          val n = scanner.nextInt()
          val j = scanner.nextInt()
          val solution = solve(n, j)
          bw.write(s"Case #$testCase:")
          for (sol <- solution) {
            bw.newLine()
            bw.write(sol.value.toString())
            bw.write(" ")
            val divisorsStr = sol.divisors.mkString(" ")
            bw.write(divisorsStr)
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

  class JamCoin(val value: BigInt) {
    val divisors = Array[BigInt](0, 0, 0, 0, 0, 0, 0, 0, 0)
  }

  def solve(n: Int, j: Int): Seq[JamCoin] = List.empty
}
