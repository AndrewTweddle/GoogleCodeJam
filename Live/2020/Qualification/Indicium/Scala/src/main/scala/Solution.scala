import java.io._

import scala.io.Source

object Solution {
  def main(args: Array[String]): Unit = processStdInOut()

  def processStdInOut(): Unit = {
    val src = Source.fromInputStream(System.in)
    processFromSource(src)
  }

  def processFromSource(src: Source): Unit = {
    try {
      val bw = new BufferedWriter(new OutputStreamWriter(System.out));
      try {
        val lines = src.getLines()
        processLines(lines, bw)
      } finally {
        bw.flush()
      }
    } finally {
      src.close();
    }
  }

  // Standard code above, custom code below

  case class Move(val rowIndex1: Int, val rowIndex2: Int, val colIndex1: Int, val colIndex2: Int)

  // Start with a valid Latin square with zeroes along its main diagonal, and values increasingly to the right.
  // Then permute its rows and columns to generate other Latin Squares, so that it always remains valid.
  class LatinSquare(val length: Int) {
    // Lookup the original row given the current row
    val rowPermutations:Array[Int] = (0 until length).toArray

    // Lookup the original col given the current col
    val colPermutations:Array[Int] = (0 until length).toArray

    // Lookup the current col given the original col
    val reverseColPermutations:Array[Int] = (0 until length).toArray

    // Lookup the current row given the original row
    val reverseRowPermutations:Array[Int] = (0 until length).toArray

    def applyMove(move: Move): Unit = {
      swapRows(move.rowIndex1, move.rowIndex2)
      swapCols(move.colIndex1, move.colIndex2)
    }

    def swapRows(ix1: Int, ix2: Int): Unit = {
      if (ix1 != ix2) {
        val origIx1 = rowPermutations(ix1)
        val origIx2 = rowPermutations(ix2)
        rowPermutations(ix1) = origIx2
        rowPermutations(ix2) = origIx1
        reverseRowPermutations(origIx1) = ix2
        reverseRowPermutations(origIx2) = ix1
      }
    }

    def swapCols(ix1: Int, ix2: Int): Unit = {
      if (ix1 != ix2) {
        val origIx1 = colPermutations(ix1)
        val origIx2 = colPermutations(ix2)
        colPermutations(ix1) = origIx2
        colPermutations(ix2) = origIx1
        reverseColPermutations(origIx1) = ix2
        reverseColPermutations(origIx2) = ix1
      }
    }

    def getColOfValueInRow(rowIndex: Int, value: Int): Int = {
      val origRowIx = rowPermutations(rowIndex)
      val origColIx = getColOfValueInOriginalRow(origRowIx, value)
      reverseColPermutations(origColIx)
    }

    def getColOfValueInOriginalRow(origRowIndex: Int, value: Int) = {
      (origRowIndex + value) % length
    }

    def getRowOfValueInCol(colIndex: Int, value: Int): Int = {
      val origColIx = colPermutations(colIndex)
      val origRowIx = getRowOfValueInOriginalCol(origColIx, value)
      reverseRowPermutations(origRowIx)
    }

    def getRowOfValueInOriginalCol(origColIndex: Int, value: Int) = {
      (origColIndex + value) % length
    }

    def getMovesInSubGridThatPlaceAValueInTheCorner(cornerIndex: Int, value: Int): Seq[Move] = {
      (cornerIndex to 0 by -1)
        .map(rowIx => (rowIx, getColOfValueInRow(rowIx, value)))
        .filter(_._2 <= cornerIndex)
        .map(pair => Move(pair._1, cornerIndex, pair._2, cornerIndex)) ++
      (cornerIndex to 0 by -1)
        .map(colIx => (getRowOfValueInCol(colIx, value), colIx))
        .filter(_._1 <= cornerIndex)
        .map(pair => Move(cornerIndex, pair._1, cornerIndex, pair._2))
      // Second set of moves and their supporting methods were added in the last 15 minutes,
      // in desperation, since I had had 2 failed attempts.
      // This change introduces bugs though, and should be reverted.
    }

    def getValue(rowIndex: Int, colIndex: Int): Int = {
      val origRowIx = rowPermutations(rowIndex)
      val origColIx = colPermutations(colIndex)
      if (origColIx < origRowIx) { origColIx + length - origRowIx } else { origColIx - origRowIx }
    }

    def getGrid(): Seq[Seq[Int]] = {
      (0 until length).map(rowIx => (0 until length).map(colIx => getValue(rowIx, colIx)))
    }

    override def toString: String = {
      getGrid().map(_.map(entry => (entry+1).toInt).mkString(" ")).mkString("\n")
    }
  }

  def processLines(lines: Iterator[String], bw: BufferedWriter): Unit = {
    val testCaseCount = lines.next().toInt
    for (t <- 1 to testCaseCount) {
      val intParams = lines.next().split(" ").map(_.toInt)
      val n = intParams(0)
      val k = intParams(1)

      // We will work with zero-based values in the grid, so subtract 1 from the trace for each diagonal element
      val soln: Option[LatinSquare] = solve(n, k-n)
      soln match {
        case None =>
          bw.write(s"Case #$t: IMPOSSIBLE")
        case Some(square) =>
          bw.write(s"Case #$t: POSSIBLE")
          bw.newLine()
          bw.write(square.toString)
      }
      bw.newLine()
    }
  }

  // The target
  def solve(n: Int, targetTrace: Int): Option[LatinSquare] = {
    val square = new LatinSquare(n)

    // Try to solve for the corner position and its row and column,
    // working from the bottom right corner to the top left, with
    // with diagonal values eventually going from high to low (non-ascending order)
    // as we go from the top left to bottom right corner.
    // This is to avoid counting equivalent solutions multiple times.
    // NB: the values in the bottom corner (below the gnomon) are fixed.
    def tryToSolveGnomon(cornerIndex: Int, targetResidualTrace: Int, minCornerValue: Int): Boolean = {
      val maxTraceInSubGridExcludingGnomon = cornerIndex * (n - 1);

      def trySpecificValueAndHigherInCornerOfGnomon(cornerValue: Int): Boolean = {

        def tryToFindMove(): Boolean = {
          val canReachResidualTraceWithThisValue =
            (cornerValue + maxTraceInSubGridExcludingGnomon) >= targetResidualTrace

          val ableToFindMove = canReachResidualTraceWithThisValue && {
            val newResidualTrace = targetResidualTrace - cornerValue

            def tryMove(move: Move): Boolean = {
              square.applyMove(move)
              val ableToSolveWithMove =
                if (cornerIndex == 0) { newResidualTrace == 0 }
                else tryToSolveGnomon(cornerIndex - 1, newResidualTrace, cornerValue)

              if (ableToSolveWithMove) true else {
                // Apply the move again to reverse its effect
                square.applyMove(move)
                false
              }
            }

            val moves = square.getMovesInSubGridThatPlaceAValueInTheCorner(cornerIndex, cornerValue)
            moves.exists(tryMove)
          }

          // If unsuccessful, recursively try higher values in the corner of the gnomon
          ableToFindMove || ((cornerValue + 1 != n) && trySpecificValueAndHigherInCornerOfGnomon(cornerValue + 1))
        }

        val minTraceInSubGridIncludingGnomon = cornerValue * (cornerIndex + 1)
        val willDefinitelyExceedTargetResidualTrace = minTraceInSubGridIncludingGnomon > targetResidualTrace
        (!willDefinitelyExceedTargetResidualTrace) && tryToFindMove()
      }

      trySpecificValueAndHigherInCornerOfGnomon(minCornerValue)
    }

    if (tryToSolveGnomon(n-1, targetTrace, 0)) Some(square) else None
  }
}
