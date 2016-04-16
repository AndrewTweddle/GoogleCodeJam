import scala.io._
import java.io._

import scala.collection.immutable.ListMap

object ProblemB {
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
          val n = lines.next().toInt
          val soldiers = (1 to 2 * n - 1).flatMap(x => lines.next().split(' ')).map(_.toInt).toIndexedSeq
          val solution = solve(soldiers)
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

  // Each soldier will be in two lists, except for soldiers on the missing page...
  def solve(soldiers: IndexedSeq[Int]): String = {
    val byNumber = soldiers.groupBy(x => x)
    val sorted = ListMap(byNumber.toSeq.sortBy(_._1): _*)
    val odds = sorted.filter(_._2.size % 2 == 1).keys
    odds.mkString(" ")
  }
}
