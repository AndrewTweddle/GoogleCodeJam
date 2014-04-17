import scala.io.Source
import java.io._
import java.util.Scanner
import math.floor

object State extends Enumeration {
  type State = Value
  val CLICKED, ZERO, EMPTY_FRONTIER, EMPTY_FINAL, UNALLOCATED, MINE = Value
}

object SolverApp extends App {
  import State._
  
  def solve(rowCount: Int, colCount: Int, mineCount: Int): String = {
    val targetEmptySpaceCount = rowCount * colCount - mineCount
    
    case class Pos(x: Int, y: Int) {
      def toIndex: Int = y * colCount + x
      def isValid: Boolean = x >= 0 && y >= 0 && x < rowCount && y < colCount
      // def expandedFrontier: List[Pos] = List(Pos(x+1, y), Pos(x+1, y+1), Pos(x, y+1)).filter(_.isValid)
      def adjacentCells: Vector[Pos] = (
          for {
            xAdj <- (-1 to 1).toVector
            yAdj <- -1 to 1
            if xAdj != 0 || yAdj != 0
          } yield Pos(x + xAdj, y + yAdj)
        ) filter(_.isValid)
    }
    
    def toPos(index: Int): Pos = Pos(index / rowCount, index % rowCount)
    
    def isBoardValid(board: Vector[State]): Boolean = {
      (0 until board.size).forall {
        case i => {
          board(i) match {
            case State.EMPTY_FINAL => {
              toPos(i).adjacentCells.exists {
                case adjPos => {
                  board(adjPos.toIndex) match {
                    case State.UNALLOCATED => true
                    case State.MINE => true
                    case _ => false
                  }
                }
              }
            }
            case _ => true
          }
        }
      }
    }
    
    def displayBoard(board: Vector[State]): String = {
      (0 until rowCount) map {
        case row => (0 until colCount) map {
          col => {
            val pos = Pos(row, col)
            val state = board(pos.toIndex)
            state match {
              case State.CLICKED => 'c'
              case State.MINE => '*'
              case State.UNALLOCATED => '*'
              case _ => '.'
            }
          }
        } mkString("")
      } mkString("\r")
    }
    
    class SearchNode(val board: Vector[State], frontier: Vector[Pos], emptySpaceCount: Int) {
      
      def expandTheSearch(frontierNodeIndex: Int): Option[SearchNode] = {
        var newBoard = board
        var newEmptySpaceCount = emptySpaceCount
        
        frontier.take(frontierNodeIndex - 1).foreach {
          case unexpandedPos => newBoard = newBoard.updated(unexpandedPos.toIndex, State.EMPTY_FINAL)  
        }
        val expandingPos = frontier(frontierNodeIndex)
        val newFrontierNodeState = if (expandingPos.toIndex == 0) State.CLICKED else State.ZERO
        newBoard = newBoard.updated(frontierNodeIndex, newFrontierNodeState)
        
        val extraFrontierNodes = expandingPos.adjacentCells.filter {
          // Yuck... mutating data in a filter - I apologise... 
          case adjacentPos => 
            val boardIndex = adjacentPos.toIndex
            val adjacentState = newBoard(boardIndex)
            adjacentState match {
              case State.UNALLOCATED => {
                newBoard = newBoard.updated(boardIndex, State.EMPTY_FRONTIER)
                newEmptySpaceCount += 1
                true
              }
              case _ => false
            }
        }
        if ((newEmptySpaceCount > targetEmptySpaceCount) || !isBoardValid(newBoard)) {
          None
        } else {
          val newFrontier = extraFrontierNodes ++ frontier.drop(frontierNodeIndex)
          val newSearchNode = new SearchNode(newBoard, newFrontier, newEmptySpaceCount)
          if (newEmptySpaceCount == targetEmptySpaceCount) {
            Some(newSearchNode)
          } else {
            if (newFrontier.size == 0) {
              None
            } else {
              // Keep searching...
              val foundNodeStream = for {
                nextFrontierNodeIndex <- (0 until newFrontier.size).toStream
                if nextFrontierNodeIndex < newFrontier.size
                found <- newSearchNode.expandTheSearch(nextFrontierNodeIndex)
              } yield found
              foundNodeStream.headOption
            }
          }
        }
      }
    }
    
    val boardSize = rowCount * colCount
    val initialBoard: Vector[State] = (0 until boardSize).map {
      i => if (i == 0) State.EMPTY_FRONTIER else State.UNALLOCATED 
    }.toVector
    val initialFrontier = Vector(Pos(0,0))
    val rootSearchNode = new SearchNode(initialBoard, initialFrontier, 1)
    val solution = rootSearchNode.expandTheSearch(0)
    solution match {
      case None => "Impossible"
      case Some(finalSearchNode) => displayBoard(finalSearchNode.board)
    }
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
          val line = lines.next()
          val scannable = new Scanner(line)
          val rowCount = scannable.nextInt()
          val colCount = scannable.nextInt()
          val mineCount = scannable.nextInt()
          
          val solution = solve(rowCount, colCount, mineCount) 
          
          bw.write(s"Case #$testCase:")
          bw.newLine()
          bw.write(solution)
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