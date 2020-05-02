# Google CodeJam Solutions

## Overview

This contains my solutions to various Google CodeJam problems.

Some were solved as part of the live competition.
Others were solved for fun or practice outside of the competition setting.

I sometimes struggle to concentrate under pressure.
Doing Google CodeJam is a way of working on this.

## Folder structure

The folder structure is primarily sub-divided by the context in which the problems were solved:

| Context         | Description |
| ---             | ---         |
| [Practice](https://github.com/AndrewTweddle/GoogleCodeJam/tree/master/Practice) | Attempts on past problems. May be timed, but more likely done at leisure. |
| [Live](https://github.com/AndrewTweddle/GoogleCodeJam/tree/master/Live) | Code written live during the competition (whether submitted or not).       |
| [PostCompetition](https://github.com/AndrewTweddle/GoogleCodeJam/tree/master/PostCompetition) | Rewrites of competition code for learning purposes... bug fixes, alternate algorithms, different programming languages. |


# Index to problems and solutions

| Competition  | Round          | Problem statement                                                                          | My solutions  | Notes  |
| ---          | ---            | ---                                                                                        | ---           | ---    |
| Africa 2010  | Qualification  | [Store Credit](https://code.google.com/codejam/contest/351101/dashboard#s=p0)              | [Practice (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Practice/Africa2010/Qualification/StoreCredit/lang/scala/src/Solver.scala)  [Practice (C#)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Practice/Africa2010/Qualification/StoreCredit/lang/cs/Solver/Program.cs) | |
| 2014         | Qualification  | [A. Magic Trick](https://code.google.com/codejam/contest/2974486/dashboard#s=p0)           | [Live (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2014/Qualification/MagicTrick/lang/scala/src/SolverApp.scala) | |
| 2014         | Qualification  | [B. Cookie Clicker Alpha](https://code.google.com/codejam/contest/2974486/dashboard#s=p1)  | [Live (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2014/Qualification/CookieCutterAlpha/lang/scala/src/SolverApp.scala) | |
| 2014         | Qualification  | [C. Minesweeper Master](https://code.google.com/codejam/contest/2974486/dashboard#s=p2)    | [Live (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2014/Qualification/MinesweeperMaster/lang/scala/src/SolverApp.scala) [Rewrite (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/PostCompetition/2014/Qualification/MinesweeperMaster/lang/scala/src/SolverApp.scala) [Rewrite (C#)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/PostCompetition/2014/Qualification/MinesweeperMaster/lang/cs/MinesweeperMaster/Program.cs)| Re-designed algorithm and rewrote in both C# and Scala to compare the languages |
| 2014         | Qualification  | [D. Deceitful War](https://code.google.com/codejam/contest/2974486/dashboard#s=p3)         | [Live (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2014/Qualification/DeceitfulWar/lang/scala/src/SolverApp.scala) | |
| 2014         | Round 1B       | [A. The Repeater](https://code.google.com/codejam/contest/2994486/dashboard#s=p0)          | [Live (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2014/Round1B/ProblemA/lang/scala/src/ProblemASolverApp.scala) [Bug fix (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/PostCompetition/2014/Round1B/ProblemA/lang/scala/src/ProblemASolverApp.scala)| Bug: should use median, not mean |
| 2014         | Round 1B       | [B. New Lottery Game](https://code.google.com/codejam/contest/2994486/dashboard#s=p1)      | [Live (C#)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2014/Round1B/ProblemB/lang/cs/ProblemB/Program.cs) [Bug fixes (C#)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/PostCompetition/2014/Round1B/ProblemB/lang/cs/ProblemB/Program.cs) [Rewrite (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/PostCompetition/2014/Round1B/ProblemB/lang/scala/src/ProblemBSolverApp.scala) | The fixed C# algorithm didn't scale to the large problem, hence the redesign and rewrite in Scala |
| 2014         | Round 1C       | [B. Reordering Train Cars](https://code.google.com/codejam/contest/3004486/dashboard#s=p1) | [Live (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2014/Round1C/ProblemB/lang/scala/src/ProblemBSolverApp.scala) [Rewrite (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/PostCompetition/2014/Round1C/ProblemB/lang/scala/src/ProblemBSolverApp.scala) | |
| 2016         | Qualification  | [A. Counting sheep](https://code.google.com/codejam/contest/6254486/dashboard#s=p0)        | [Live (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2016/Qualification/src/main/scala/ProblemA.scala) | |
| 2016         | Qualification  | [B. Revenge of the pancakes](https://code.google.com/codejam/contest/6254486/dashboard#s=p1) | [Live (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2016/Qualification/src/main/scala/ProblemB.scala) | |
| 2016         | Qualification  | [C. Coin Jam](https://code.google.com/codejam/contest/6254486/dashboard#s=p2)              | [Live (Scala) - incorrect](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2016/Qualification/src/main/scala/ProblemC.scala) | Small submission rejected. TODO: 2016-04-09: Work out why. |
| 2016         | Qualification  | [D. Fractiles](https://code.google.com/codejam/contest/6254486/dashboard#s=p3)             | [Live (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2016/Qualification/src/main/scala/ProblemD.scala) | |
| 2016         | Round 1A       | [A. The Last Word](https://code.google.com/codejam/contest/4304486/dashboard#s=p0)         | [Live (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2016/1A/src/main/scala/ProblemA.scala#L32-L41) | Wasted over half an hour on an IntelliJ Idea issue (Hint: Right click on src/main/scala, "Mark directory as" > "Sources Root") |
| 2016         | Round 1A       | [B. Rank and File](https://code.google.com/codejam/contest/4304486/dashboard#s=p1)         | [Live (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2016/1A/src/main/scala/ProblemB.scala#L36-L41) | |
| 2016         | Round 1A       | [C. BFFs](https://code.google.com/codejam/contest/4304486/dashboard#s=p2)                  | [Live (Scala) - incomplete](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2016/1A/src/main/scala/ProblemC.scala#L38-L52); Rewrite (Scala): [brute force](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/PostCompetition/2016/1A/src/main/scala/ProblemC.scala#L61-L79), [fast](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/PostCompetition/2016/1A/src/main/scala/ProblemC.scala#L81-L180) |  |
| 2016         | Round 1C       | [C. Fashion police](https://code.google.com/codejam/contest/4314486/dashboard#s=p2) | [Live (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2016/1C/src/main/scala/ProblemC.scala#L45-L53) | Logged in an hour late, not planning to participate, but gave in to temptation. Submitted successful large solution in the final minute! |
| 2016         | Round 1B       | [B. Close match](https://code.google.com/codejam/contest/11254486/dashboard#s=p1) | [Practice (Scala)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Practice/2016/1B/src/main/scala/ProblemB.scala#L45-L136) | Quite tricky - is there a simpler solution? |
| 2020         | Qualification  | [Nesting Depth](https://codingcompetitions.withgoogle.com/codejam/round/000000000019fd27/0000000000209a9f) | [Live (Python)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2020/Qualification/NestingDepth/Solution.py)| |
| 2020         | Qualification  | [Parenting Partnering Returns](https://codingcompetitions.withgoogle.com/codejam/round/000000000019fd27/000000000020bdf9) | [Live (C#)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2020/Qualification/ParentPartneringReturns/AndrewTweddle.GCJ2020.ParentPartneringReturns/Solution.cs)| |
| 2020         | Qualification  | [ESAb ATAd](https://codingcompetitions.withgoogle.com/codejam/round/000000000019fd27/0000000000209a9e) | [Live (C++)](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2020/Qualification/ESAbATAd/Cpp/Solution.cc)| |
| 2020         | Qualification  | [Indicium](https://codingcompetitions.withgoogle.com/codejam/round/000000000019fd27/0000000000209aa0) | [Live (Scala) - Incorrect](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2020/Qualification/Indicium/Scala/src/main/scala/Solution.scala)| |
 

# Performance in competition

| Competition                                                | Results |
| ---                                                        | ---     |
| [2014](http://www.go-hero.net/jam/14/name/atweddle)        | Qualified for round 1. Failed to reach round 2. |
| [2016](https://www.go-hero.net/jam/16/name/Andrew.Tweddle) | Qualified for round 1. Failed to reach round 2. |
| 2020                                                       | Qualified for round 1. |


# Learnings

## 2016 Round 1 experiences:

In round 1A I did quite well, despite losing the first 30 or 40 minutes to an IDE setup issue.
In round 1C I logged in an hour late, still had to set up my projects and narrowly missed making round 2.
In round 1B I was well prepared, could see in principle how to solve each of the 3 problems, but struggled to coordinate my thoughts and bombed out miserably.

_Conclusion: I do much better when I am on the back foot and have reduced expectations of myself. Am I undermining myself by placing too much pressure on myself?_

# Other items of interest

In 2014 I used a deep folder structure for problems and their data.

In the 2016 qualification round, I used a single data folder for the entire round.
I also added a [Master.scala](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2016/Qualification/src/main/scala/Master.scala) file to make it easier to switch between problems and test files.
This takes command line arguments for things like:
  * the problem (A, B, C or D)
  * problem size (s=small, l=large, t=test or a custom name)
  * attempt number (small attempts only, defaulting to zero) 
It generates input and output file names from these arguments and calls the appropriate solution.
If applicable, it also copies a snapshot of the ProblemX.scala source file to the data folder for easy uploading.

Additionally, if called with no arguments, then there is an option to enter the problem. 
If there are zero or one arguments, then the inputs will be read from the standard input stream.
In both cases the results will be written to the standard output stream.

This gives greater flexibility for participating in other coding competitions, such as CodeForces, which use the standard input and output streams.

The Google CodeJam format changed sometime between 2017 and 2020. So these templates are no longer applicable. 

# A sample problem file

A sample problem file for Scala solutions can be found [here](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Practice/2016/1B/src/main/scala/ProblemB.scala#L1-L41). This is only applicable to the older competition format.
