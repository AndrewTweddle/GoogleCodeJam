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
        val problem = if (args.length == 1) args(0).toUpperCase() else {
          println("USAGE: problem size [attempt [inputFolder [outputFolder]]]")
          println()
          println("problem: a|b|...")
          println("size: s|small|l|large|t|test|{custom}")
          println("attempt: 0, 1, ... (only relevant for size=small, defaults to 0)")
          println("inputFolder: defaults to data, NB: no trailing slash")
          println("outputFolder: defaults to match inputFolder, NB: no trailing slash")
          println()
          println("Choose a problem to solve interactively or press ENTER to exit:")
          println()
          io.StdIn.readLine().toUpperCase()
        }
        if (problem.isEmpty) {
          println("No problem selected.");
          println();
          println("Exiting...");
        } else {
          println()
          println(s"Read problem $problem from stdin (or type inputs below):")
          runInteractively(problem)
          println()
          println("DONE!")
        }
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
        println()
        println("DONE!")

        // Copy source file for easy upload (only relevant if part of live competition):
        if (sizeName == "small" || sizeName == "large") {
          val srcFilePath = Paths.get(s"src/main/scala/Problem$problem.scala")
          val destFilePath = Paths.get(s"$outFolderPath/Problem${problem}_$sizeName$attemptSuffix.scala")
          if (Files.exists(srcFilePath)) {
            Files.copy(srcFilePath, destFilePath, StandardCopyOption.REPLACE_EXISTING)
            println()
            println(s"Copied $srcFilePath to $destFilePath")
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
        getProblem(problem).processFiles(inFileName, outFileName)
      }
    }

    def runInteractively(problem: String): Unit = getProblem(problem).processStdInOut()

    // To customize, enable relevant problem objects below...

    def getProblem(problem: String):
      { def processStdInOut(): Unit; def processFiles(inFileName: String, outFileName: String): Unit } = {
        problem match {
          // case "A" => ProblemA
          case "B" => ProblemB
          // case "C" => ProblemC
          // case "D" => ProblemD
          case _ => throw new IllegalArgumentException(s"Unsupported problem: $problem")
        }
    }
  }
}
