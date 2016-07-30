import scala.io._
import java.io._
import java.util.Scanner

import scala.annotation.tailrec
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
          println(s"Test case: $testCase")
          /*
          val str = lines.next().split(' ')
          val line = lines.next()
          val scanner = new Scanner(line)
          val friends = (0 until n).map(i => scanner.nextInt() - 1)
          val solution = solve(n, friends)
          val solStr = s"Case #$testCase: $solution"
          println(solStr)
          println()
          bw.write(solStr)
          bw.newLine()
          */
        }
      } finally {
        bw.flush()
        bw.close()
      }
    } finally {
      bufferedSource.close()
    }
  }
}
