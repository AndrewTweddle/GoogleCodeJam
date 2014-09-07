import scala.io.Source
import java.io._
import java.util.Scanner

object State extends Enumeration {
  type State = Value
  val CLICKED, EMPTY, POSSIBLE_MINE = Value
}

object SolverApp extends App {
  import State._

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

  def solve(rowCount: Int, colCount: Int, mineCount: Int, clickPosIndex: Int = 0): String = {
    val targetEmptySpaceCount = rowCount * colCount - mineCount

    case class Pos(x: Int, y: Int) {
      def toIndex: Int = y * colCount + x
      def isValid: Boolean = x >= 0 && y >= 0 && x < colCount && y < rowCount
      def adjacentCells: List[Pos] = (
        for {
          xAdj <- (-1 to 1).toList
          yAdj <- -1 to 1
          if xAdj != 0 || yAdj != 0 
        } yield Pos(x + xAdj, y + yAdj)) filter (_.isValid)
    }

    def toPos(index: Int): Pos = Pos(index % colCount, index / colCount)

    def isPermFrontierValid(board: Vector[State], permFrontier: List[Pos]): Boolean = {
      permFrontier.forall { // Every cell on the permanent frontier 
        case frontierPos => // must be next to at least one mine
          frontierPos.adjacentCells.exists(
            (adjCell: Pos) => board(adjCell.toIndex) == State.POSSIBLE_MINE)
      }
    }

    def displayBoard(board: Vector[State]): String = {
      (0 until rowCount) map {
        case y => (0 until colCount) map {
          x =>
            {
              val pos = Pos(x, y)
              val posIndex = pos.toIndex
              val state = board(posIndex)
              state match {
                case State.CLICKED => 'c'
                case State.POSSIBLE_MINE => '*'
                case _ => '.'
              }
            }
        } mkString ("")
      } mkString ("\r")
    }

    def search(board: Vector[State], tempFrontier: List[Pos], permFrontier: List[Pos], emptySpaceCount: Int): Option[Vector[State]] = {
      // permFrontier consists of cells known to be on the final frontier between the empty region and the mines.
      // tempFrontier is the current frontier of the expanding empty region.
      // Each element of tempFrontier might either be expanded to become a zero, or become part of the permanent frontier.
      if (emptySpaceCount == targetEmptySpaceCount) {
        Some(board)
      } else if (emptySpaceCount > targetEmptySpaceCount || tempFrontier.isEmpty) {
        None
      } else {
        // Consider the first cell in the temporary frontier as an expansion candidate
        val expandingPos = tempFrontier.head
        val expandingPosIndex = expandingPos.toIndex
        
        val newTempFrontierNodes = expandingPos.adjacentCells.filter {
          case adjPos: Pos => board(adjPos.toIndex) == State.POSSIBLE_MINE
        }
        
        if (newTempFrontierNodes.isEmpty) {
          // The expansion candidate is already a zero, so ignore it and search again:
          search(board, tempFrontier.tail, permFrontier, emptySpaceCount)
        } else {
          // Option 1: turn the cell into a zero, by expanding it:
          val newEmptySpaceCount = emptySpaceCount + newTempFrontierNodes.size
          val newBoard = newTempFrontierNodes.foldLeft(board) {
            case (b, newFrontierCell) => b.updated(newFrontierCell.toIndex, State.EMPTY)
          }
          val solution = if (isPermFrontierValid(newBoard, permFrontier)) {
            search(newBoard, newTempFrontierNodes ::: tempFrontier.tail, permFrontier, newEmptySpaceCount)
          } else {
            // The permanent frontier constraint is violated by expanding this node
            // (this check is to avoid reaching duplicate board configurations via different search paths)
            None
          }
          solution match {
            case Some(_) => solution
            case None => {
              // No solution could be found by expanding the cell. 
              // So add it to the permanent frontier and search again:
              search(board, tempFrontier.tail, expandingPos :: permFrontier, emptySpaceCount)
            }
          }
        }
      }
    }

    val boardSize = rowCount * colCount
    val initialBoard: Vector[State] = (0 until boardSize).map {
      i => if (i == clickPosIndex) State.CLICKED else State.POSSIBLE_MINE
    }.toVector
    val initialFrontier = List(toPos(clickPosIndex))
    val solution = search(initialBoard, initialFrontier, List.empty, 1)
    solution match {
      case None => "Impossible"
      case Some(board) => displayBoard(board)
    }
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
            val rowCount = scannable.nextInt()
            val colCount = scannable.nextInt()
            val mineCount = scannable.nextInt()

            val solution = solve(rowCount, colCount, mineCount, 0)
            // The last parameter is zero on the assumption that the top-left corner can always be the click point

            bw.write(s"Case #$testCase:")
            bw.newLine()
            bw.write(solution)
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

  if (args.size < 2) {
    println("USAGE: <inputFilePath> <outputFilePath>")
  } else {
    val inputFile = args(0)
    val outputFile = args(1)
    solveAll(inputFile, outputFile)
  }
}