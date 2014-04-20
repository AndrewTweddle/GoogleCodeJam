using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Text.RegularExpressions;
using System.Diagnostics;

namespace MinesweeperMaster
{
    public class Position
    {
        public int Row { get; set; }
        public int Col { get; set; }

        public IEnumerable<Position> GetAdjacentPositions(int boardRowCount, int boardColCount)
        {
            for (int rOffset = -1; rOffset <= 1; rOffset++)
            {
                for (int cOffset = -1; cOffset <= 1; cOffset++)
                {
                    if (rOffset != 0 || cOffset != 0)
                    {
                        int r = Row + rOffset;
                        int c = Col + cOffset;
                        if (r >= 0 && r < boardRowCount && c >= 0 && c < boardColCount)
                        {
                            yield return new Position { Row = r, Col = c };
                        }
                    }
                }
            }
        }
    }

    public enum CellState { Unknown, Empty, Zero, NonZero, Mine }

    public class SearchNode
    {
        /// <summary>
        /// The frontier nodes are 
        /// </summary>
        public List<Position> KnownFrontierCells { get; set; }
        public Queue<Position> ExpansionCells { get; set; }
        public CellState[,] Cells { get; set; }
        public int EmptyCellCount { get; set; }
        public int RowCount { get; set; }
        public int ColCount { get; set; }

        protected SearchNode()
        {
            ExpansionCells = new Queue<Position>();
            KnownFrontierCells = new List<Position>();
        }

        public SearchNode(int rowCount, int colCount, Position clickPoint): this()
        {
            RowCount = rowCount;
            ColCount = colCount;
            Cells = new CellState[rowCount, colCount];
            for (int i = 0; i < rowCount; i++)
            {
                for (int j = 0; j < colCount; j++)
                {
                    if (i == clickPoint.Row && j == clickPoint.Col)
                    {
                        Cells[i, j] = CellState.Empty;
                    }
                    else
                    {
                        Cells[i, j] = CellState.Unknown;
                    }
                }
            }
            EmptyCellCount = 1;  // The click point
            ExpansionCells.Enqueue(clickPoint);
        }

        public SearchNode(SearchNode nodeToCopy): this()
        {
            RowCount = nodeToCopy.RowCount;
            ColCount = nodeToCopy.ColCount;
            EmptyCellCount = nodeToCopy.EmptyCellCount;
            KnownFrontierCells.AddRange(nodeToCopy.KnownFrontierCells);
            foreach (Position pos in nodeToCopy.ExpansionCells)
            {
                ExpansionCells.Enqueue(pos);
                // TODO: See if this is in the same order or not. Actually, doesn't really matter.
            }
            Cells = (CellState[,]) nodeToCopy.Cells.Clone();
        }

        public SearchNode Search(int targetEmptyCellCount)
        {
            if (targetEmptyCellCount == EmptyCellCount)
            {
                for (int i = 0; i < RowCount; i++)
                {
                    for (int j = 0; j < ColCount; j++)
                    {
                        if (Cells[i, j] == CellState.Unknown)
                        {
                            Cells[i, j] = CellState.Mine;
                        }
                    }
                }
                return this;
            }
            if (targetEmptyCellCount < EmptyCellCount)
            {
                return null;  // This is not a valid solution
            }

            // Clone a child search node:
            SearchNode childSearchNode = Clone();
            if (childSearchNode.ExpansionCells.Count == 0)
            {
                return null;  // Nothing further to expand
            }
            Position nextPos = childSearchNode.ExpansionCells.Dequeue();

            List<Position> adjacentUnknownCells
                = nextPos.GetAdjacentPositions(RowCount, ColCount).Where(adjPos => Cells[adjPos.Row, adjPos.Col] == CellState.Unknown).ToList();

            // Possibility 1: nextPos is already a zero without further expansion (due to previously expanded nodes)
            // was: if (childSearchNode.IsCellSurroundedByEmptyCells(nextPos))
            if (adjacentUnknownCells.Count == 0)
            {
                childSearchNode.Cells[nextPos.Row, nextPos.Col] = CellState.Zero;
                return childSearchNode.Search(targetEmptyCellCount);
            }

            SearchNode childSearchNode2 = childSearchNode.Clone();  // Clone now, in case needed later

            // Possibility 2: Make nextPos a zero by expanding it. It is not part of the final frontier with the mines.
            int newEmptyCellCount = EmptyCellCount;
            foreach (Position pos in adjacentUnknownCells)
            {
                newEmptyCellCount++;
                childSearchNode.ExpansionCells.Enqueue(pos);
                childSearchNode.Cells[pos.Row, pos.Col] = CellState.Empty;
            }
            childSearchNode.Cells[nextPos.Row, nextPos.Col] = CellState.Zero;
            childSearchNode.EmptyCellCount = newEmptyCellCount;
            if (childSearchNode.AreAllFinalFrontierNodesNonZero())
            {
                SearchNode solutionNode = childSearchNode.Search(targetEmptyCellCount);
                if (solutionNode != null)
                {
                    return solutionNode;
                }
            }

            // Possibility 3: nextPos must be non-zero. It is part of the final frontier and should not be expanded.
            childSearchNode2.Cells[nextPos.Row, nextPos.Col] = CellState.NonZero;
            childSearchNode2.KnownFrontierCells.Add(nextPos);
            return childSearchNode2.Search(targetEmptyCellCount);
        }

        private bool AreAllFinalFrontierNodesNonZero()
        {
            foreach (Position pos in KnownFrontierCells)
            {
                if (IsCellSurroundedByEmptyCells(pos))
                {
                    return false;
                }
            }
            return true;
        }

        private bool IsEmptyCell(Position pos)
        {
            CellState cellState = Cells[pos.Row, pos.Col];
            return cellState == CellState.Empty || cellState == CellState.Zero || cellState == CellState.NonZero;
        }

        private bool IsCellSurroundedByEmptyCells(Position nextPos)
        {
            
            IEnumerable<Position> adjacentPositions = nextPos.GetAdjacentPositions(RowCount, ColCount);
            return adjacentPositions.All(pos => IsEmptyCell(pos));
        }

        public SearchNode Clone()
        {
            SearchNode clone = new SearchNode(this);
            return clone;
        }

        internal string DisplayBoard(Position clickPos)
        {
            StringBuilder sb = new StringBuilder();
            StringWriter sw = new StringWriter(sb);
            for (int i = 0; i < RowCount; i++)
            {
                for (int j = 0; j < ColCount; j++)
                {
                    if (i == clickPos.Row && j == clickPos.Col)
                    {
                        sw.Write('c');
                    }
                    else
                    {
                        switch (Cells[i, j])
                        {
                            case CellState.Empty:
                            case CellState.Zero:
                            case CellState.NonZero:
                                sw.Write('.');
                                break;
                            case CellState.Mine:
                            case CellState.Unknown:
                                sw.Write('*');
                                break;
                            default:
                                // Should never happen
                                break;
                        }
                    }
                }
                sw.WriteLine();
            }
            sw.Flush();
            return sb.ToString();
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length < 2)
            {
                Console.WriteLine("Usage: MinesweeperMaster.exe InputFilePath OutputFilePath");
                return;
            }

            string inputFilePath = args[0];
            string outputFilePath = args[1];
            FileStream fsIn = File.OpenRead(inputFilePath);
            using (fsIn)
            {
                StreamReader sr = new StreamReader(fsIn, Encoding.ASCII);
                string firstLine = sr.ReadLine();
                int problemCount = int.Parse(firstLine);

                // Prepare to write the output file:
                FileStream fsOut = File.OpenWrite(outputFilePath);
                using (fsOut)
                {
                    StreamWriter sw = new StreamWriter(fsOut, Encoding.ASCII);
                    try
                    {
                        Stopwatch swatch = Stopwatch.StartNew();
                        for (int problemNumber = 1; problemNumber <= problemCount; problemNumber++)
                        {
                            string problemDimensionsString = sr.ReadLine();
                            string[] problemDimensions = problemDimensionsString.Split(' ');
                            int rowCount = int.Parse(problemDimensions[0]);
                            int colCount = int.Parse(problemDimensions[1]);
                            int mineCount = int.Parse(problemDimensions[2]);
                            string solution = Solve(rowCount, colCount, mineCount);
                            sw.WriteLine("Case #{0}:", problemNumber);
                            sw.Write(solution);
                        }
                        swatch.Stop();
                        Console.WriteLine("Total duration: {0}", swatch.Elapsed);
                    }
                    finally
                    {
                        sw.Flush();
                    }
                }
            }
        }

        static string Solve(int rowCount, int colCount, int mineCount)
        {
            Position clickPos = new Position { Row = 0, Col = 0 };
            SearchNode searchNode = new SearchNode(rowCount, colCount, clickPos);
            SearchNode solutionNode = searchNode.Search(rowCount * colCount - mineCount);
            if (solutionNode == null)
            {
                return "Impossible\r\n";
            }
            return solutionNode.DisplayBoard(clickPos);
        }
    }
}
