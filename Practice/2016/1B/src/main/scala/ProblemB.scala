import scala.io._
import java.io._
import scala.math.Ordered.orderingToOrdered

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
        val testCaseCount = lines.next.toInt
        for (testCase <- 1 to testCaseCount) {
          val line = lines.next()
          val strings = line.split(" ")
          val sol = solve(strings(0), strings(1))
          bw.write(s"Case #$testCase: ${sol.cStr} ${sol.jStr}")
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

  case class Solution(val c: Long, val j: Long, cStr: String, jStr: String) extends Ordered[Solution] {
    lazy val absDiff: Long = math.abs(c - j)
    def replaceAllQuMarks(cSubs: Char, jSubs: Char): Solution = {
      val newCStr = replaceQu(cStr, cSubs)
      val newJStr = replaceQu(jStr, jSubs)
      val newC = convertToLong(newCStr)
      val newJ = convertToLong(newJStr)
      Solution(newC, newJ, newCStr, newJStr)
    }
    def compare(that: Solution): Int = (this.absDiff, this.c, this.j) compare (that.absDiff, that.c, that.j)
  }

  def replaceQu(s: String, digit: Char) = s.replace('?', digit)

  def convertToLong(s: String): Long = {
    val stripped = s.dropWhile(_ == '0')
    if (stripped == "") 0 else stripped.toLong
  }
  def charToInt(ch: Char): Int = ("" + ch).toInt
  def digitToChar(digit: Int): Char = digit.toString()(0)
  def isDigit(digit: Int) = (digit >= 0) && (digit <= 9)

  def getTheBetterSolution(solution1: Solution, solution2: Solution): Solution = {
    if (solution1 < solution2) solution1 else solution2
  }

  def solve(c: String, j: String): Solution = {
    def expand(candidate: Solution, index: Int): Solution = {
      def relaceAndExpandMany(cjDigits: (Int, Int) *): Solution = {
        def replaceAndExpand(newCDigit: Int, newJDigit: Int): Solution = {
          val newCStr = candidate.cStr.updated(index, digitToChar(newCDigit))
          val newJStr = candidate.jStr.updated(index, digitToChar(newJDigit))
          val newC = candidate.c * 10 + newCDigit
          val newJ = candidate.j * 10 + newJDigit
          expand(Solution(newC, newJ, newCStr, newJStr), index + 1)
        }
        val newCandidates = cjDigits.map(cj => replaceAndExpand(cj._1, cj._2))
        newCandidates.sorted.head
      }

      candidate match {
        // if intermediate c > j, make final c as small as possible and final j as large as possible:
        case Solution(c, j, _, _) if c > j => candidate.replaceAllQuMarks('0', '9')

        // if intermediate c < j, make final c as large as possible and final j as small as possible:
        case Solution(c, j, _, _) if c < j => candidate.replaceAllQuMarks('9', '0')

        // if intermediate c == j, expand the next character in each string:
        case _ => {
          if (candidate.cStr.length == index) { candidate } else {
            val cNext = candidate.cStr(index)
            val jNext = candidate.jStr(index)

            (cNext, jNext) match {
              case ('?', '?') => relaceAndExpandMany((0, 1), (1, 0), (0, 0))

              case ('?', chToTry) => {
                val valToTry = charToInt(chToTry)
                val pairsToTry = Seq(valToTry - 1, valToTry, valToTry + 1)
                  .filter(isDigit)
                  .map((_, valToTry))
                relaceAndExpandMany(pairsToTry: _*)
              }

              case (chToTry, '?') => {
                val valToTry = charToInt(chToTry)
                val pairsToTry = Seq(valToTry - 1, valToTry, valToTry + 1)
                  .filter(isDigit)
                  .map((valToTry, _))
                relaceAndExpandMany(pairsToTry: _*)
              }

              // If both of next digits are defined, expand one level and call expand again
              case _ => {
                val cDigit = charToInt(cNext)
                val jDigit = charToInt(jNext)
                val newCandidate = candidate.copy(c = candidate.c * 10 + cDigit, j = candidate.j * 10 + jDigit )
                expand(newCandidate, index + 1)
              }
            }
          }
        }
      }
    }

    val initialCandidate = Solution(0, 0, c, j)
    expand(initialCandidate, 0)
  }
}
