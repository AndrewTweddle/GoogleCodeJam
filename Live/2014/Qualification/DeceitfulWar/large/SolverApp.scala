import scala.io.Source
import java.io._

object SolverApp extends App {
  def solveDeceitfulWar(blockCount: Int, naomisSortedBlocks: Vector[Double], kensSortedBlocks: Vector[Double]): Int = {
    
    def getNaomisBestScore(naomisScoreSoFar: Int, naomisBlocks: Vector[Double], kensBlocks: Vector[Double]): Int = {
      naomisBlocks match {
        case naomisLowest +: naomisRemainingBlocks =>
          kensBlocks match {
            case kensLowestBlockThatWins +: _ if kensLowestBlockThatWins > naomisLowest =>
              // Naomi states a value that is just lower than Ken's highest remaining block, but plays her lowest block...
              getNaomisBestScore(naomisScoreSoFar, naomisRemainingBlocks, kensBlocks.init)
            case _ +: kensRemainingBlocks => // i.e. Ken's lowest block is lower than Naomi's lowest block
              // Naomi states a value higher than Ken's highest remaining block, but both play their lowest blocks...
              getNaomisBestScore(naomisScoreSoFar + 1, naomisRemainingBlocks, kensRemainingBlocks)
            // There must be the same number in both lists, so no need to check any other cases
          }
        case _ => naomisScoreSoFar
      }
    }
    
    getNaomisBestScore(0, naomisSortedBlocks, kensSortedBlocks)
  }
  
  def solveHonestWar(blockCount: Int, naomisSortedBlocks: Vector[Double], kensSortedBlocks: Vector[Double]): Int = {
    
    def getKensBestScore(kensScoreSoFar: Int, naomisBlocks: Vector[Double], kensBlocks: Vector[Double]): Int = {
      naomisBlocks match {
        case naomisNextBlock +: naomisRemainingBlocks =>
          val kensHigherBlocks = kensBlocks.dropWhile(_ < naomisNextBlock)
          kensHigherBlocks match {
            case kensNextBlock +: kensRemainingBlocks => 
              getKensBestScore(kensScoreSoFar + 1, naomisRemainingBlocks, kensRemainingBlocks)
            case _ => kensScoreSoFar  // Ken has used up all blocks higher than Naomis next lowest block
          }
        case _ => kensScoreSoFar  // no blocks left for Naomi
      } 
    }
    
    blockCount - getKensBestScore(0, naomisSortedBlocks, kensSortedBlocks)
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
          val blockCount = lines.next.toInt
          val naomisSortedBlocks = lines.next.split(raw"\s+").map(_.toDouble).sorted.toVector   
          val kensSortedBlocks = lines.next.split(raw"\s+").map(_.toDouble).sorted.toVector
          
          val naomisDeceitfulScore = solveDeceitfulWar(blockCount, naomisSortedBlocks, kensSortedBlocks)
          val naomisHonestScore = solveHonestWar(blockCount, naomisSortedBlocks, kensSortedBlocks) 
          
          bw.write(s"Case #$testCase: $naomisDeceitfulScore $naomisHonestScore")
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