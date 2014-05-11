import scala.io.Source
import java.io._
import java.util.Scanner

object ProblemBSolverApp extends App {
  implicit val repetitions: Int = 1

  def time[R](block: => R)(implicit repetitions: Int): R = {
    val t0 = System.nanoTime()
    var result: R = block
    var repsLeft = repetitions - 1
    while (repsLeft > 0) {
      result = block // call-by-name
      repsLeft -= 1
    }
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) / 1.0e6 / repetitions + " ms")
    result
  }
  
  def getNonZeroBitPositions(target: Int, maxPos: Int = 31): List[Int] = {
    val nonZeroBitPositions = (0 to maxPos) filter {
      case pos: Int => (target & (1 << pos)) != 0
    }
    nonZeroBitPositions.toList
  }
    
  def solve(aMax: Int, bMax: Int, kMax: Int): Long = {
    
    val aMaxHighBitPos = if (aMax == 0) 0 else { aMax.toBinaryString.length - 1 }
    val bMaxHighBitPos = if (bMax == 0) 0 else { bMax.toBinaryString.length - 1 }
    val kMaxHighBitPos = if (kMax == 0) 0 else { kMax.toBinaryString.length - 1 }
    
    val maxHighBitPos = aMaxHighBitPos max bMaxHighBitPos max kMaxHighBitPos
    
    def countValidCombinations(aHighestSmallerBitPos: Int, bHighestSmallerBitPos: Int, kHighestSmallerBitPos: Int): Long = {
      // SmallerBitPos indicates that the bit in that position is a zero, and is smaller than the one bit in the max value.
      // Highest indicates that it is the highest bit position (the bit where the value first becomes less than its maximum).
      // If -1, then the value (a, b or k) is equal to the maximum value.

      def countOptionsAtBitPos(bitPos: Int): Long = {
        val isABitFree = (bitPos < aHighestSmallerBitPos)
        val isBBitFree = (bitPos < bHighestSmallerBitPos)
        val isKBitFree = (bitPos < kHighestSmallerBitPos)
        val currBitMask = 1 << bitPos
        val aBit = (bitPos != aHighestSmallerBitPos) && ((aMax & currBitMask) != 0)
        val bBit = (bitPos != bHighestSmallerBitPos) && ((bMax & currBitMask) != 0)
        val kBit = (bitPos != kHighestSmallerBitPos) && ((kMax & currBitMask) != 0)
        
        (isABitFree, isBBitFree, isKBitFree) match {
          case (false, false, false) => if ((aBit && bBit) == kBit) 1 else 0
          case (false, false, true) => 1  // set kBit = aBit & bBit
          case (false, true, false) if kBit => if (aBit) 1 else 0  // aBit and bBit must both be true
          case (false, true, false) => if (aBit) {
            1  // set bBit = false 
          } else {
            2  // bBit can be zero or 1, since aBit is zero
          }
          case (false, true, true) => 2  // if aBit then kBit = bBit = either. If !aBit, then !kBit. So bBit can be either.
          case (true, false, false) if kBit => if (bBit) 1 else 0  // aBit and bBit must both be true
          case (true, false, false) => if (bBit) {
            1  // set aBit = false 
          } else {
            2  // aBit can be zero or 1, since bBit is zero
          }
          case (true, false, true) => 2  // if bBit then kBit = aBit = either. If !bBit, then !kBit. So aBit can be either.
          case (true, true, false) if kBit => 1  // aBit and bBit must both be true
          case (true, true, false) => 3  // Of the 4 combinations for aBit and bBit, only one is disallowed (aBit and bBit both true)
          case (true, true, true) => 4  // aBit and bBit can be any of the 4 combinations, and kBit is calculated from aBit && bBit
        }
      }
      
      maxHighBitPos.to(0, -1).foldLeft[Long](1L) {
        case (combinations: Long, bitPos: Int) =>
          if (combinations == 0L) 0L else (combinations * countOptionsAtBitPos(bitPos))
      }
    }
    
    // Consider every combination of possible bit positions where a/b/k can FIRST have a 0 bit and aMax/bMax/kMax a 1 bit:  
    val counts = for {
      aBitPos <- -1 :: getNonZeroBitPositions(aMax, aMaxHighBitPos)  // -1 indicates that a == aMax
      bBitPos <- -1 :: getNonZeroBitPositions(bMax, bMaxHighBitPos)  // -1 indicates that b == bMax
      kBitPos <- -1 :: getNonZeroBitPositions(kMax, kMaxHighBitPos)  // -1 indicates that k == kMax
    } yield countValidCombinations(aBitPos, bBitPos, kBitPos)
      
    counts.sum
  }
  
  def solveAll(inputFilePath: String, outputFilePath: String) {
    
    val bufferedSource = Source.fromFile(inputFilePath)
    try {
      val lines = bufferedSource.getLines()
      val outputFile = new File(outputFilePath)
      val bw = new BufferedWriter(new FileWriter(outputFile))
      try {
        time {
          val testCaseCount = lines.next.toInt
          for (testCase <- 1 to testCaseCount) {
            val line = lines.next()
            val scannable = new Scanner(line)
            val bigA = scannable.nextInt()
            val bigB = scannable.nextInt()
            val bigK = scannable.nextInt()
            
            val solution = solve(bigA - 1, bigB - 1, bigK - 1)
            bw.write(s"Case #$testCase: $solution") 
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
  
  val inputFile = args(0)
  val outputFile = args(1)
  solveAll(inputFile, outputFile)
}