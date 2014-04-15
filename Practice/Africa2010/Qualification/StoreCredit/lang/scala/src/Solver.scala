import scala.io.Source
import java.io._
import java.util.Scanner
import scala.util.control.Breaks._
import scala.collection.mutable.ArrayBuffer

object Solver extends App {
  val linesPerProblem = 3
  val maxPrice = 1000

  def solveAll(inputFilePath: String, outputFilePath: String) {
    def readAndSolveProblem(problemNumber: Int, lines: Seq[String]): String = {
      val credits = lines(0).toInt
      val itemCount = lines(1).toInt
      val priceList = lines(2)

      val firstIndexOfEachValue = new Array[Int](maxPrice + 1) // Element i of the array contains first priceIndex of value i in the list (or zero if not found yet)
      var minIndex = -1
      var maxIndex = -1

      val scannable = new Scanner(priceList)
      breakable {
        for (index <- 1 to itemCount) {
          val nextPrice = scannable.nextInt()
          if (nextPrice < credits) {
            val indexOfFirst = firstIndexOfEachValue(credits - nextPrice)
            if (indexOfFirst != 0) {
              minIndex = index min indexOfFirst
              maxIndex = index max indexOfFirst
              break
            } else if (firstIndexOfEachValue(nextPrice) == 0) {
              firstIndexOfEachValue(nextPrice) = index
            }
          }
        }
      }
      s"Case #${problemNumber}: $minIndex $maxIndex"
    }

    val bufferedSource = Source.fromFile(inputFilePath)
    try {
      val lines = bufferedSource.getLines()
      val outputFile = new File(outputFilePath)
      val bw = new BufferedWriter(new FileWriter(outputFile))
      try {
        lines
          .drop(1)
          .grouped(linesPerProblem)
          .zipWithIndex
          .map(linesAndIndex => readAndSolveProblem(linesAndIndex._2 + 1, linesAndIndex._1))
          .foreach( solText => { bw.write(solText); bw.newLine() } )
      } finally {
        bw.flush()
        bw.close()
      }
    } finally {
      bufferedSource.close()
    }
  }

  val inputFile = args(0)
  val outputFile = args(1)
  solveAll(inputFile, outputFile)
}
