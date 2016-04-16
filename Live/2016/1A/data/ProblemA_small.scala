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
          val s = lines.next()
          val solution = solve(s)
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

  def solve(s: String): String = {
    println(s"Solving for $s")
    val solution = s.toList.foldLeft(List[Char]()) {
      (cs, c) => if (cs.isEmpty) List(c) else cs match {
        case hd :: _ if c >= hd => c :: cs
        case _  => cs ::: (c :: Nil)
      }
    }
    solution.mkString
  }
}
