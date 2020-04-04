using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace AndrewTweddle.GCJ2020.ParentPartneringReturns.ConsoleApp
{
    class Activity
    {
        public Activity(int activityId, int startTime, int endTime)
        {
            ActivityId = activityId;
            StartTime = startTime;
            EndTime = endTime;
        }
        public int ActivityId { get; }
        public int StartTime { get; }
        public int EndTime { get; }
        public char AllocatedParent { get; set; } = '?';
    }
    
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length > 0)
            {
                // Redirect an input file to the standard input stream
                string inputFilePath = args[0];
                using (var inputFileReader = File.OpenText(inputFilePath))
                {
                    var oldIn = Console.In;
                    try
                    {
                        Console.SetIn(inputFileReader);
                        if (args.Length > 1)
                        {
                            // Redirect standard output to an output file
                            string outputFilePath = args[1];
                            using (var outputFileWriter = File.CreateText(outputFilePath))
                            {
                                var oldOut = Console.Out;
                                try
                                {
                                    Console.SetOut(outputFileWriter);
                                    SolveAllTestCases();
                                }
                                finally
                                {
                                    Console.SetOut(oldOut);
                                }
                            }
                        }
                        else
                        {
                            // Redirect standard input only. Still write output to the console.
                            SolveAllTestCases();
                        }
                    }
                    finally
                    {
                        Console.SetIn(oldIn);
                    }
                }
            }
            else
            {
                // Read inputs from, and write outputs to, the console
                SolveAllTestCases();
            }
        }
        
        static void SolveAllTestCases()
        {
            string testCaseLine = Console.ReadLine();
            int testCaseCount = int.Parse(testCaseLine);
            
            for (int t = 1; t <= testCaseCount; t++)
            {
                var activities = ParseActivities();
                bool IsPossible = TryToAllocateAllActivities(activities);
                if (!IsPossible)
                {
                    Console.WriteLine($"Case #{t}: IMPOSSIBLE");
                    continue;
                }

                string allocation = GetActivityAllocation(activities);
                Console.WriteLine($"Case #{t}: {allocation}");
            }
        }

        private static IList<Activity> ParseActivities()
        {
            string activityCountLine = Console.ReadLine();
            int activityCount = int.Parse(activityCountLine);
            IList<Activity> activities = new List<Activity>();
            for (int i = 0; i < activityCount; i++)
            {
                string timesLine = Console.ReadLine();
                string[] timeStrings = timesLine.Split(' ');
                int startTime = int.Parse(timeStrings[0]);
                int endTime = int.Parse(timeStrings[1]);
                Activity activity = new Activity(i, startTime, endTime);
                activities.Add(activity);
            }

            return activities;
        }

        static bool TryToAllocateAllActivities(IList<Activity> activities)
        {
            var actsSortedByStartAndEndTime = 
                activities.OrderBy(a => a.StartTime).ThenBy(a => a.EndTime);
            int lastActEndTimeC = 0;
            int lastActEndTimeJ = 0;
            foreach (var act in actsSortedByStartAndEndTime)
            {
                if (act.StartTime >= lastActEndTimeC)
                {
                    act.AllocatedParent = 'C';
                    lastActEndTimeC = act.EndTime;
                    continue;
                }
                    
                if (act.StartTime >= lastActEndTimeJ)
                {
                    act.AllocatedParent = 'J';
                    lastActEndTimeJ = act.EndTime;
                    continue;
                }

                // This activity overlaps both parents' last activities, so no solution is possible
                return false;
            }

            return true;
        }

        static string GetActivityAllocation(IList<Activity> activities)
        {
            StringBuilder allocBuilder = new StringBuilder();
            activities.Aggregate(allocBuilder, (sb, act) => sb.Append(act.AllocatedParent));
            return allocBuilder.ToString();
        }
    }
}