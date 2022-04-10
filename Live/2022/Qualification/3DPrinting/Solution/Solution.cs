using System;
using System.Collections.Generic;
using System.Linq;

class Solution
{
    private const int MAX_INK_LEVEL = 1000000;
    
    static void Main(string[] args)
    {
        int test_case_count = int.Parse(Console.ReadLine());
        for (int t = 1; t <= test_case_count; t++)
        {
            var inkLevels1 = GetInkLevels(Console.ReadLine());  
            var inkLevels2 = GetInkLevels(Console.ReadLine());  
            var inkLevels3 = GetInkLevels(Console.ReadLine());
            var finalInkLevels = new List<int>(4);

            int totalInk = 0;
            for (int i = 0; i < 4; i++)
            {
                int nextInkLevel = Math.Min(Math.Min(inkLevels1[i], inkLevels2[i]), inkLevels3[i]);
                if (totalInk + nextInkLevel > MAX_INK_LEVEL)
                {
                    nextInkLevel = MAX_INK_LEVEL - totalInk;
                }
                finalInkLevels.Add(nextInkLevel);
                totalInk += nextInkLevel;
            }

            Console.Write("Case #{0}:", t);
            if (totalInk == MAX_INK_LEVEL)
            {
                foreach (var inkLevel in finalInkLevels)
                {
                    Console.Write(" {0}", inkLevel);
                }
                Console.WriteLine();
            }
            else
            {
                Console.WriteLine(" IMPOSSIBLE");
            }
        }
    }

    static List<int> GetInkLevels(string InkLevelsStr)
    {
        var enumerableInkLevels = from inkLevelStr in InkLevelsStr.Split(" ") select int.Parse(inkLevelStr);
        return new List<int>(enumerableInkLevels);
    }
}
