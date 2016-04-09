import java.io.File

import scala.collection.immutable

/**
  * Created by Andrew on 2016/04/08.
  */
object Master {
  val sizeMap = immutable.HashMap[String, String]("s" -> "small", "l" -> "large", "t"->"test")

  // e.g. D s 0
  //      D t
  //      D l data
  def main(args: Array[String]): Unit = {
    try {
      if (args.length < 3) {
        println("USAGE: command problem size [attempt [inputFolder [outputFolder]]]")
        println()
        println("command: run (others to be added later)")
        println("problem: a|b|...")
        println("size: s|small|l|large|t|test|{custom}")
        println("attempt: 0, 1, ... (only relevant for size=small, defaults to 0)")
        println("inputFolder: defaults to data, NB: no trailing slash")
        println("outputFolder: defaults to match inputFolder, NB: no trailing slash")
      } else {
        val command = args(0).toLowerCase()
        val problem = args(1).toLowerCase()
        val sizeCode = args(2).toLowerCase()
        val sizeName = sizeMap.getOrElse(sizeCode, sizeCode)
        val isSmall = sizeName == "small"
        val attempt = if (isSmall && args.length > 3) args(3).toInt else 0
        val inFolderPath = if (isSmall && args.length > 4) args(4) else if (args.length > 3) args(3) else "data"
        val outFolderPath = if (isSmall && args.length > 5) args(5) else if (args.length > 4) args(4) else inFolderPath
        val attemptSuffix = if (isSmall) s"-attempt$sizeName" else ""
        val inFileName = s"$inFolderPath/$problem-$sizeName$attemptSuffix.in"
        val outFileName = s"$outFolderPath/$problem-$sizeName$attemptSuffix.out"
        command match {
            case "run" => run(problem, inFileName, outFileName)
            case _ => throw new IllegalArgumentException(s"Unsupported command: $command")
        }
      }
    }
    catch {
      case e: Throwable => println(e)
    }

    def run(problem: String, inFileName: String, outFileName: String): Unit = {
      if (!new File(inFileName).exists()) {
        println("ERROR: input file does not exist: " + inFileName)
      } else {
        problem match {
          /*
          case "a"  => ProblemA.processFiles(inFileName, outFileName)
          case "b" => ProblemB.processFiles(inFileName, outFileName)
          case "c" => ProblemC.processFiles(inFileName, outFileName)
          */
          case "d" => ProblemD.processFiles(inFileName, outFileName)
          case _ => throw new IllegalArgumentException(s"Unsupported problem: $problem")
        }
      }
    }
  }
}
