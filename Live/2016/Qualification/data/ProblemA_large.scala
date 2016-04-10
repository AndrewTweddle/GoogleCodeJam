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
          val n = lines.next().toInt
          val solution = solve(n)
          val sol = solution match {
            case None => "INSOMNIA"
            case Some(sol) => sol.toString
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

  def solve(n: Int): Option[Int] = {
    println(s"Solving for $n:")
    if (n == 0) None else {
      var rem = 0.to(9).flatMap(_.toString.toCharArray).toSet
      var lastNumber = 0
      while (!rem.isEmpty) {
        lastNumber += n
        println(s"Testing $lastNumber")
        rem = rem -- lastNumber.toString.toList
      }
      Some(lastNumber)
    }
  }
}
