import java.io.File
import java.nio.file.{Files, Paths, StandardCopyOption}

import scala.collection.immutable

/**
  * Created by Andrew on 2016/04/15.
  */
object Master {
  val sizeMap = immutable.HashMap[String, String]("s" -> "small", "l" -> "large", "t"->"test")

  // e.g. D s 0
  //      D t
  //      D l data
  def main(args: Array[String]): Unit = {
    try {
      if (args.length < 2) {
        println("USAGE: problem size [attempt [inputFolder [outputFolder]]]")
        println()
        println("problem: a|b|...")
        println("size: s|small|l|large|t|test|{custom}")
        println("attempt: 0, 1, ... (only relevant for size=small, defaults to 0)")
        println("inputFolder: defaults to data, NB: no trailing slash")
        println("outputFolder: defaults to match inputFolder, NB: no trailing slash")
      } else {
        val problem = args(0).toUpperCase()
        val sizeCode = args(1).toLowerCase()
        val sizeName = sizeMap.getOrElse(sizeCode, sizeCode)
        val isSmall = sizeName == "small"
        val inFolderPos = if (isSmall) 3 else 2
        val attempt = if (isSmall && args.length > 2) args(2).toInt else 0
        val inFolderPath = if (args.length > inFolderPos) args(inFolderPos) else "data"
        val outFolderPath = if (args.length > inFolderPos + 1) args(inFolderPos) else inFolderPath
        val attemptSuffix = if (isSmall) s"-attempt$attempt" else ""
        val inFileName = s"$inFolderPath/$problem-$sizeName$attemptSuffix.in"
        val outFileName = s"$outFolderPath/$problem-$sizeName$attemptSuffix.out"
        run(problem, inFileName, outFileName)

        // Copy source file for easy upload (only relevant if part of live competition):
        if (sizeName == "small" || sizeName == "large") {
          val srcFilePath = Paths.get(s"src/main/scala/Problem$problem.scala")
          val destSrcFilePath = Paths.get(s"$outFolderPath/Problem${problem}_$sizeName$attemptSuffix.scala")
          if (Files.exists(srcFilePath)) {
            Files.copy(srcFilePath, destSrcFilePath, StandardCopyOption.REPLACE_EXISTING)
          }
        }
      }
    }
    catch {
      case e: Throwable => println(e)
    }

    def run(problem: String, inFileName: String, outFileName: String): Unit = {
      if (!new File(inFileName).exists()) {
        throw new IllegalArgumentException(s"ERROR: input file does not exist: $inFileName")
      } else {
        problem match {
          // case "A"  => ProblemA.processFiles(inFileName, outFileName)
          case "B" => ProblemB.processFiles(inFileName, outFileName)
          // case "C" => ProblemC.processFiles(inFileName, outFileName)
          case _ => throw new IllegalArgumentException(s"Unsupported problem: $problem")
        }
      }
    }
  }
}
