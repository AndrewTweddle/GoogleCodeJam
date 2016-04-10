import scala.io._
import java.io._

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
          val pancakes = lines.next().toList
          val solution = solve(pancakes)
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

  case class Accumulator(lastPancake: Char, signChangeCount: Int)

  def solve(pancakes: Seq[Char]): Int = {
    val accumulator = pancakes.foldLeft(new Accumulator(pancakes.head, 0)) {(acc, pancake) =>
      if (pancake == acc.lastPancake) acc else {
        new Accumulator(pancake, acc.signChangeCount + 1)
        /* i.e. flip the entire stack until before the change in pancake orientation (sign),
         * so that the stack above the next pancake matches its orientation
         */
      }
    }
    accumulator.signChangeCount + (if (accumulator.lastPancake == '+') 0 else 1 /* flip the entire stack over */)
  }
}
