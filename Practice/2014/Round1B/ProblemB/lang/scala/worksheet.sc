object worksheet {
  ProblemBSolverApp.getNonZeroBitPositions(2, 2)  //> res0: List[Int] = List(1)
  ProblemBSolverApp.getNonZeroBitPositions(12, 4) //> res1: List[Int] = List(2, 3)
  ProblemBSolverApp.getNonZeroBitPositions(30)    //> res2: List[Int] = List(1, 2, 3, 4)
  
  val aMax = 2                                    //> aMax  : Int = 2
  val bMax = 3                                    //> bMax  : Int = 3
  val kMax = 1                                    //> kMax  : Int = 1
  val aMaxHighBitPos = if (aMax == 0) 0 else { aMax.toBinaryString.length - 1 }
                                                  //> aMaxHighBitPos  : Int = 1
  val bMaxHighBitPos = if (bMax == 0) 0 else { bMax.toBinaryString.length - 1 }
                                                  //> bMaxHighBitPos  : Int = 1
  val kMaxHighBitPos = if (kMax == 0) 0 else { kMax.toBinaryString.length - 1 }
                                                  //> kMaxHighBitPos  : Int = 0
    
  val maxHighBitPos = aMaxHighBitPos max bMaxHighBitPos max kMaxHighBitPos
                                                  //> maxHighBitPos  : Int = 1
  maxHighBitPos.to(0, -1)                         //> res3: scala.collection.immutable.Range.Inclusive = Range(1, 0)
  for {
      aBitPos <- -1 :: ProblemBSolverApp.getNonZeroBitPositions(aMax, aMaxHighBitPos)  // -1 indicates that a == aMax
      bBitPos <- -1 :: ProblemBSolverApp.getNonZeroBitPositions(bMax, bMaxHighBitPos)  // -1 indicates that b == bMax
      kBitPos <- -1 :: ProblemBSolverApp.getNonZeroBitPositions(kMax, kMaxHighBitPos)  // -1 indicates that k == kMax
    } yield (aBitPos, bBitPos, kBitPos)           //> res4: List[(Int, Int, Int)] = List((-1,-1,-1), (-1,-1,0), (-1,0,-1), (-1,0,0
                                                  //| ), (-1,1,-1), (-1,1,0), (1,-1,-1), (1,-1,0), (1,0,-1), (1,0,0), (1,1,-1), (1
                                                  //| ,1,0))

    def countValidCombinations(aHighestSmallerBitPos: Int, bHighestSmallerBitPos: Int, kHighestSmallerBitPos: Int): Int = {
      // SmallerBitPos indicates that the bit in that position is a zero, and is smaller than the one bit in the max value.
      // Highest indicates that it is the highest bit position (the bit where the value first becomes less than its maximum).
      // If -1, then the value (a, b or k) is equal to the maximum value.

      def countOptionsAtBitPos(bitPos: Int): Int = {
        val isABitFree = (bitPos < aHighestSmallerBitPos)
        val isBBitFree = (bitPos < bHighestSmallerBitPos)
        val isKBitFree = (bitPos < kHighestSmallerBitPos)
        val currBitMask = 1 << bitPos
        val aBit = (bitPos != aHighestSmallerBitPos) && ((aMax & currBitMask) != 0)
        val bBit = (bitPos != bHighestSmallerBitPos) && ((bMax & currBitMask) != 0)
        val kBit = (bitPos != kHighestSmallerBitPos) && ((kMax & currBitMask) != 0)
        
        (isABitFree, isBBitFree, isKBitFree) match {
          case (false, false, false) => if (aBit && bBit == kBit) 1 else 0
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
      
      maxHighBitPos.to(0, -1).foldLeft(1) {
        case (combinations: Int, bitPos: Int) =>
          if (combinations == 0) 0 else (combinations * countOptionsAtBitPos(bitPos))
      }
    }                                             //> countValidCombinations: (aHighestSmallerBitPos: Int, bHighestSmallerBitPos:
                                                  //|  Int, kHighestSmallerBitPos: Int)Int

  for {
      aBitPos <- -1 :: ProblemBSolverApp.getNonZeroBitPositions(aMax, aMaxHighBitPos)  // -1 indicates that a == aMax
      bBitPos <- -1 :: ProblemBSolverApp.getNonZeroBitPositions(bMax, bMaxHighBitPos)  // -1 indicates that b == bMax
      kBitPos <- -1 :: ProblemBSolverApp.getNonZeroBitPositions(kMax, kMaxHighBitPos)  // -1 indicates that k == kMax
    } yield (aBitPos, bBitPos, kBitPos, countValidCombinations(aBitPos, bBitPos, kBitPos))
                                                  //> res5: List[(Int, Int, Int, Int)] = List((-1,-1,-1,0), (-1,-1,0,0), (-1,0,-1
                                                  //| ,0), (-1,0,0,0), (-1,1,-1,0), (-1,1,0,2), (1,-1,-1,0), (1,-1,0,0), (1,0,-1,
                                                  //| 0), (1,0,0,0), (1,1,-1,0), (1,1,0,0))
 
  def countOptionsAtBitPos(bitPos: Int)(aHighestSmallerBitPos: Int, bHighestSmallerBitPos: Int, kHighestSmallerBitPos: Int): Int = {
      def countOptionsAtBitPos(bitPos: Int): Int = {
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
      
      maxHighBitPos.to(0, -1).foldLeft(1) {
        case (combinations: Int, bitPos: Int) =>
          if (combinations == 0) 0 else (combinations * countOptionsAtBitPos(bitPos))
      }
    }                                             //> countOptionsAtBitPos: (bitPos: Int)(aHighestSmallerBitPos: Int, bHighestSma
                                                  //| llerBitPos: Int, kHighestSmallerBitPos: Int)Int
    
  countOptionsAtBitPos(1)(1, 1, 0)                //> res6: Int = 3
  countOptionsAtBitPos(0)(1, 1, 0)                //> res7: Int = 3
  
  val aHighestSmallerBitPos: Int = 1              //> aHighestSmallerBitPos  : Int = 1
  val bHighestSmallerBitPos: Int = 1              //> bHighestSmallerBitPos  : Int = 1
  val kHighestSmallerBitPos: Int = 0              //> kHighestSmallerBitPos  : Int = 0
  val bitPos = 1                                  //> bitPos  : Int = 1
	val isABitFree = (bitPos < aHighestSmallerBitPos)
                                                  //> isABitFree  : Boolean = false
	val isBBitFree = (bitPos < bHighestSmallerBitPos)
                                                  //> isBBitFree  : Boolean = false
	val isKBitFree = (bitPos < kHighestSmallerBitPos)
                                                  //> isKBitFree  : Boolean = false
	val currBitMask = 1 << bitPos             //> currBitMask  : Int = 2
	val aBit = (bitPos != aHighestSmallerBitPos) && ((aMax & currBitMask) != 0)
                                                  //> aBit  : Boolean = false
	val bBit = (bitPos != bHighestSmallerBitPos) && ((bMax & currBitMask) != 0)
                                                  //> bBit  : Boolean = false
	val kBit = (bitPos != kHighestSmallerBitPos) && ((kMax & currBitMask) != 0)
                                                  //> kBit  : Boolean = false
  kMax & currBitMask                              //> res8: Int = 0
}