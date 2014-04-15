using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Text.RegularExpressions;

namespace Solver
{
    class Program
    {
        private const int MAX_PRICE = 1000;

        static void Main(string[] args)
        {
            if (args.Length < 2)
            {
                Console.WriteLine("Usage: solver.exe InputFilePath OutputFilePath");
                return;
            }

            string inputFilePath = args[0];
            string outputFilePath = args[1];
            FileStream fsIn = File.OpenRead(inputFilePath);
            using (fsIn)
            {
                StreamReader sr = new StreamReader(fsIn, Encoding.UTF8);
                string firstLine = sr.ReadLine();
                int problemCount = int.Parse(firstLine);

                // Prepare to write the output file:
                FileStream fsOut = File.OpenWrite(outputFilePath);
                using (fsOut)
                {
                    StreamWriter sw = new StreamWriter(fsOut, Encoding.UTF8);
                    for (int problemNumber = 1; problemNumber <= problemCount; problemNumber++)
                    {
                        int credit = int.Parse(sr.ReadLine());
                        int itemCount = int.Parse(sr.ReadLine());
                        string itemsList = sr.ReadLine();
                        int[] indexesOfItemsByPrice = new int[MAX_PRICE+1];
                        IEnumerable<int> itemPrices = Regex.Split(itemsList, @"\s+").Select(s => int.Parse(s));
                        int nextPriceIndex = 1;
                        foreach (int nextPrice in itemPrices)
                        {
                            int firstPrice = credit - nextPrice;
                            if (firstPrice > 0)
                            {
                                int firstPriceIndex = indexesOfItemsByPrice[firstPrice];
                                if (firstPriceIndex > 0)
                                {
                                    int minPriceIndex = Math.Min(firstPriceIndex, nextPriceIndex);
                                    int maxPriceIndex = Math.Max(firstPriceIndex, nextPriceIndex);
                                    sw.WriteLine(String.Format("Case #{0}: {1} {2}", problemNumber, minPriceIndex, maxPriceIndex));
                                    continue;
                                }
                            }
                            indexesOfItemsByPrice[nextPrice] = nextPriceIndex;
                            nextPriceIndex++;
                        }
                    }
                    sw.Flush();
                }
            }
        }
    }
}
