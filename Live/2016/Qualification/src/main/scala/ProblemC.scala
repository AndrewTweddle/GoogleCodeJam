import scala.io._
import java.io._
import java.util.Scanner
import scala.collection._

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

  case class BitCalculation(bitNo: Int) {
    val mask = 1 << bitNo
    val factors: Seq[BigInt] = BigInt(2).to(10).map(_.pow(bitNo)).toSeq
  }

  class JamCoin(val value: BigInt, val valuesByBase: IndexedSeq[BigInt]) {
    val divisors = Array[BigInt](0, 0, 0, 0, 0, 0, 0, 0, 0)

    def isAValidJamCoin = divisors.forall(_ != 0)

    def setDivisor(base: Int, divisor: BigInt): Unit = {
      val index = base - 2
      if (divisors(index) == 0) {
        divisors(index) = divisor
      }
    }
  }

  def solve(n: Int, j: Int): Seq[JamCoin] = {
    // Constants and lookups to assist with calculations:
    val bases = 2.to(10).toSeq
    val bits = 0.until(n).map(new BitCalculation(_)).toList
    val fixedAdj = (1 << (n - 1)) + 1  // Leading and trailing 1 in an n digit binary number
    val iMax = (1 << (n-2)) - 1
    val primesToBases = immutable.Map(
      2 -> List(3, 5, 7, 9),
      3 -> List(4, 7, 10),
      5 -> List(6),
      7 -> List(8)
    )

    // Track jam coins:

    // val candidates = mutable.ArrayBuffer()
    val jamCoins = mutable.ArrayBuffer[JamCoin]()
    var jamCoinCount = 0
    var i = 0

    while (jamCoinCount < j && i <= iMax) {
      // for (i <- 0.until(iMax)) {
      val base2Val = 2 * i + fixedAdj
      val sumOfDigits = bits.count(bitCalc => (bitCalc.mask & base2Val) != 0)
      val valuesInEachBase = bases.map(base =>
        bits.filter { bitCalc => (bitCalc.mask & base2Val) != 0 }.map(_.factors(base - 2)).reduce(_ + _)
      )
      var value = valuesInEachBase(8)

      println(s"Checking $value (# ${i + 1} of ${iMax + 1})")

      var jamCoin = new JamCoin(value, valuesInEachBase)

      // See if any of the primes divide the sum of digits, and update the divisor if they do
      for ((prime, bases) <- primesToBases) {
        if (base2Val % prime == 0) {
          jamCoin.setDivisor(2, prime)
        }

        if (sumOfDigits % prime == 0) {
          for (base <- bases) {
            jamCoin.setDivisor(base, prime)
          }
        }
      }

      if (jamCoin.isAValidJamCoin) {
        jamCoins += jamCoin
        jamCoinCount += 1
        println(s"  $jamCoinCount of $j jam coins found!")
      }
      i += 1
    }
    println(s"[$jamCoinCount jam coins found]")
    jamCoins
  }
}
