# Google CodeJam Solutions

## Overview

This contains my solutions to various Google CodeJam problems.

Some were solved as part of the live competition. Others were solved for fun or practice outside of the competition setting.

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
| 2016         | Round 1A       | [C. BFFs](https://code.google.com/codejam/contest/4304486/dashboard#s=p2)                  | [Live (Scala) - incomplete](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2016/1A/src/main/scala/ProblemC.scala#L38-L52); Post-competition (Scala): [brute force](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/PostCompetition/2016/1A/src/main/scala/ProblemC.scala#L95-L113)], [fast (https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/PostCompetition/2016/1A/src/main/scala/ProblemC.scala#L115-L176)] |  |

# Performance in competition

| Competition                                         |
| ---                                                 |
| [2014](http://www.go-hero.net/jam/14/name/atweddle) |

# Other items of interest

In 2014 I used a deep folder structure for problems and their data.
In the 2016 qualification round, I used a single data folder for the entire round.
I also added a [Master.scala](https://github.com/AndrewTweddle/GoogleCodeJam/blob/master/Live/2016/Qualification/src/main/scala/Master.scala) file to make it easier to switch between problems and test files.
This takes command line arguments for things like:
  * the problem (A, B, C or D)
  * problem size (s=small, l=large, t=test or a custom name)
  * attempt number (small attempts only, defaulting to zero) 
It generates input and output file names from these arguments and calls the appropriate solution.

# Future work

* Modify Master.scala to move the relevant ProblemX.scala file into the data folder, renaming it to ProblemX_{size}.scala, for easier uploading.
* Move Master.scala and ProblemX.scala template files into a templates folder
* Make polyglot programming easier: Write similar master files for other languages (sharing the same maven/sbt folder layout including the shared data folder)
* Make it easier to do unit testing
