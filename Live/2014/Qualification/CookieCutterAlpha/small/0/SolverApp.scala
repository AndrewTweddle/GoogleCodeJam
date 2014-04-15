import scala.io.Source
import java.io._
import java.util.Scanner

object SolverApp extends App {
  def solve(costPerCookieFarm: Double, ratePerCookieFarm: Double, targetNumberOfCookies: Double): Double = {

    def timeToAchieveTarget(cookieRate: Double, timeTakenSoFar: Double, bestTime: Double): Double = {
      val timeTakenWithNoMorePurchases = timeTakenSoFar + targetNumberOfCookies / cookieRate

      // Calculate possible new best time (for pruning search):
      val newBestTime = if (timeTakenWithNoMorePurchases < bestTime) {
        timeTakenWithNoMorePurchases
      } else {
        bestTime
      }

      val timeTakenAfterNextPurchase = timeTakenSoFar + costPerCookieFarm / cookieRate
      if (timeTakenAfterNextPurchase >= newBestTime) {
        newBestTime
      } else {
        timeToAchieveTarget(cookieRate + ratePerCookieFarm, timeTakenAfterNextPurchase, newBestTime)
      }
    }

    timeToAchieveTarget(cookieRate = 2, timeTakenSoFar = 0.0, bestTime = Double.MaxValue)
  }
  
  def solveAll(inputFilePath: String, outputFilePath: String) {

    val bufferedSource = Source.fromFile(inputFilePath)
    try {
      val lines = bufferedSource.getLines()
      val outputFile = new File(outputFilePath)
      val bw = new BufferedWriter(new FileWriter(outputFile))
      try {
        val testCaseCount = lines.next.toInt
        for (testCase <- 1 to testCaseCount) {
          val line = lines.next
          val scannable = new Scanner(line)
          val costPerCookieFarm = scannable.nextDouble()
          val ratePerCookieFarm = scannable.nextDouble()
          val targetNumberOfCookies = scannable.nextDouble()

          val solution = solve(costPerCookieFarm, ratePerCookieFarm, targetNumberOfCookies)
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

  val inputFile = args(0)
  val outputFile = args(1)
  solveAll(inputFile, outputFile)
}