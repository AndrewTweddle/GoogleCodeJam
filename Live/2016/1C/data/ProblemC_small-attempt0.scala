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
          val line = lines.next()
          val scanner = new Scanner(line)
          val j = scanner.nextInt()
          val p = scanner.nextInt()
          val s = scanner.nextInt()
          val k = scanner.nextInt()
          val solution = solve(j, p, s, k)
          val dayCount = solution.length
          bw.write(s"Case #$testCase: $dayCount")
          bw.newLine()
          for ((jix, kix, skix) <- solution) {
            bw.write(s"$jix $kix $skix")
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

  def solve(j: Int, p: Int, s: Int, k: Int): IndexedSeq[Tuple3[Int, Int, Int]] = {
    val sk = math.min(s, k)
    val combos = for {
      jix <- 1 to j
      pix <- 1 to p
      skix <- 1 to sk
    } yield (jix, pix, jix + pix + skix - 2)
    combos
  }
}
