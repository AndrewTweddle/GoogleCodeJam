using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Diagnostics;

namespace ProblemB
{
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length < 2)
            {
                Console.WriteLine("Usage: ProblemB.exe InputFilePath OutputFilePath");
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
                            string line = sr.ReadLine();
                            string[] inputs = line.Split(' ');
                            int upperA = int.Parse(inputs[0]);
                            int upperB = int.Parse(inputs[1]);
                            int upperK = int.Parse(inputs[2]);
                            string solution = String.Empty;
                            if (upperA >= upperB)
                            {
                                solution = Solve(upperA - 1, upperB - 1, upperK - 1);
                            }
                            else 
                            {
                                solution = Solve(upperB - 1, upperA - 1, upperK - 1);
                            }
                            sw.WriteLine("Case #{0}: {1}", problemNumber, solution);
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

        static int CountSolutions(int a, int b, int k, int mask, int maxA, int maxB)
        {
            if ((a > maxA) || (b > maxB))
            {
                return 0;
            }

            if (mask == 0)
            {
                return 1;
            }

            bool isInK = (k & mask) != 0;
            if (isInK)
            {
                return CountSolutions(a | mask, b | mask, k, mask >> 1, maxA, maxB);
            }
            else
            {
                int solutionsWithAMasked = CountSolutions(a | mask, b, k, mask >> 1, maxA, maxB);
                int solutionsWithBMasked = CountSolutions(a, b | mask, k, mask >> 1, maxA, maxB);
                int solutionsWithNeitherMasked = CountSolutions(a, b, k, mask >> 1, maxA, maxB);
                return solutionsWithAMasked + solutionsWithBMasked + solutionsWithNeitherMasked;
            }
        }

        static string Solve(int maxA, int maxB, int maxK)
        {
            // NB: maxA >= maxB
            int aBits = Convert.ToString(maxA, 2).Length;
            int solutionCount = 0;
            for (int k = 0; k <= maxK; k++)
            {
                int kBits = Convert.ToString(maxB, 2).Length;
                if (kBits <= aBits)
                {
                    // Iterate through the bits from largest to smallest:
                    int mask = 1 << aBits;
                    int a = 0;
                    int b = 0;
                    solutionCount += CountSolutions(a, b, k, mask, maxA, maxB);
                }
            }
            return solutionCount.ToString();
        }

    }
}
