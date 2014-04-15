import scala.io.Source
import java.io._

object SolverApp extends App {
  def solveDeceitfulWar(blockCount: Int, naomisSortedBlocks: List[Double], kensSortedBlocks: List[Double]): Int = {
    val naomisBlocks = naomisSortedBlocks
    val kensReversedBlocks = kensSortedBlocks.reverse
    val kensScore = naomisBlocks.zip(kensReversedBlocks).takeWhile( { case (naomi, ken) => naomi < ken } ).size
    blockCount - kensScore 
  }
  
  def solveHonestWar(blockCount: Int, naomisSortedBlocks: List[Double], kensSortedBlocks: List[Double]): Int = {
    
    def getKensBestScore(kensScoreSoFar: Int, naomisBlocks: List[Double], kensBlocks: Seq[Double]): Int = {
      naomisBlocks match {
        case Nil => kensScoreSoFar
        case naomisNextBlock :: naomisRemainingBlocks =>
          val kensHigherBlocks = kensBlocks.dropWhile(_ < naomisNextBlock)
          kensHigherBlocks match {
            case Nil => kensScoreSoFar  // Ken has used up all blocks higher than Naomis next lowest block
            case kensNextBlock :: kensRemainingBlocks => 
              getKensBestScore(kensScoreSoFar + 1, naomisRemainingBlocks, kensRemainingBlocks)
          }
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
          val naomisSortedBlocks = lines.next.split(raw"\s+").map(_.toDouble).sorted.toList   
          val kensSortedBlocks = lines.next.split(raw"\s+").map(_.toDouble).sorted.toList
          
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