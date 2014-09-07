import scala.collection.mutable.Map

object Worksheet {
  ProblemBSolverApp.checkWordHasOnlyContiguousLetters("abcbd".toList)
                                                  //> res0: Boolean = false
  ProblemBSolverApp.checkWordHasOnlyContiguousLetters("abac".toList)
                                                  //> res1: Boolean = false
  ProblemBSolverApp.checkWordHasOnlyContiguousLetters("abcde".toList)
                                                  //> res2: Boolean = true
  'a' - 'a'                                       //> res3: Int(0) = 0
  'z' - 'a'                                       //> res4: Int(25) = 25
  val sameSAndE = Map[Char, List[String]]()       //> sameSAndE  : scala.collection.mutable.Map[Char,List[String]] = Map()
  val diffSAndEByStart = Map[Char, String]()      //> diffSAndEByStart  : scala.collection.mutable.Map[Char,String] = Map()
  val diffSAndEByEnd = Map[Char, String]()        //> diffSAndEByEnd  : scala.collection.mutable.Map[Char,String] = Map()
  var strings = List("ab", "bc", "bd")            //> strings  : List[String] = List(ab, bc, bd)
  ProblemBSolverApp.mapStringsAndCheck(strings, sameSAndE, diffSAndEByStart, diffSAndEByEnd)
                                                  //> res5: Boolean = false
  strings = List("ab", "bc", "cd")
  sameSAndE.clear()
  diffSAndEByStart.clear()
  diffSAndEByEnd.clear()
  ProblemBSolverApp.mapStringsAndCheck(List("ab", "bc", "cd"), sameSAndE, diffSAndEByStart, diffSAndEByEnd)
                                                  //> res6: Boolean = true
  var s: String = "abc"                           //> s  : String = abc
  s(0)                                            //> res7: Char = a
  s(s.length() - 1)                               //> res8: Char = c
  s = "a"
  s(0)                                            //> res9: Char = a
  s(s.length() - 1)                               //> res10: Char = a
  
  sameSAndE.clear()
  diffSAndEByStart.clear()
  diffSAndEByEnd.clear()
  strings = List("ab", "b", "bc", "cxc", "cyc", "cd")
  ProblemBSolverApp.mapStringsAndCheck(strings, sameSAndE, diffSAndEByStart, diffSAndEByEnd)
                                                  //> res11: Boolean = true
  sameSAndE                                       //> res12: scala.collection.mutable.Map[Char,List[String]] = Map(b -> List(b), 
                                                  //| c -> List(cyc, cxc))
  diffSAndEByStart                                //> res13: scala.collection.mutable.Map[Char,String] = Map(b -> bc, a -> ab, c 
                                                  //| -> cd)
  diffSAndEByEnd                                  //> res14: scala.collection.mutable.Map[Char,String] = Map(b -> ab, d -> cd, c 
                                                  //| -> bc)
  var combCounts = ProblemBSolverApp.calculateCombinationsFromSameStartAndEndBlocks(sameSAndE, diffSAndEByEnd)
                                                  //> combCounts  : scala.collection.mutable.Map[Char,ProblemBSolverApp.Combinati
                                                  //| onCount] = Map(b -> CombinationCount(abb,1), d -> CombinationCount(cd,1), c
                                                  //|  -> CombinationCount(bccyccxc,2))
  ProblemBSolverApp.tryCombineStrings(combCounts) //> res15: Boolean = true
  combCounts                                      //> res16: scala.collection.mutable.Map[Char,ProblemBSolverApp.CombinationCount
                                                  //| ] = Map(b -> CombinationCount(abb,1), d -> CombinationCount(bccyccxccd,2))
                                                  //| 
  ProblemBSolverApp.tryCombineStrings(combCounts) //> res17: Boolean = true
  combCounts                                      //> res18: scala.collection.mutable.Map[Char,ProblemBSolverApp.CombinationCount
                                                  //| ] = Map(d -> CombinationCount(abbbccyccxccd,2))
  ProblemBSolverApp.tryCombineStrings(combCounts) //> res19: Boolean = false
  combCounts                                      //> res20: scala.collection.mutable.Map[Char,ProblemBSolverApp.CombinationCount
                                                  //| ] = Map(d -> CombinationCount(abbbccyccxccd,2))
  sameSAndE.clear()
  diffSAndEByStart.clear()
  diffSAndEByEnd.clear()
  strings = List("ab", "b", "bc", "cxc", "cyc", "cd", "ef", "ege", "e", "ee")
  ProblemBSolverApp.mapStringsAndCheck(strings, sameSAndE, diffSAndEByStart, diffSAndEByEnd)
                                                  //> res21: Boolean = true
  sameSAndE                                       //> res22: scala.collection.mutable.Map[Char,List[String]] = Map(e -> List(ee, 
                                                  //| e, ege), b -> List(b), c -> List(cyc, cxc))
  diffSAndEByStart                                //> res23: scala.collection.mutable.Map[Char,String] = Map(e -> ef, b -> bc, a 
                                                  //| -> ab, c -> cd)
  diffSAndEByEnd                                  //> res24: scala.collection.mutable.Map[Char,String] = Map(b -> ab, d -> cd, c 
                                                  //| -> bc, f -> ef)
  combCounts = ProblemBSolverApp.calculateCombinationsFromSameStartAndEndBlocks(sameSAndE, diffSAndEByEnd)
  ProblemBSolverApp.combineStrings(sameSAndE, combCounts)
  combCounts                                      //> res25: scala.collection.mutable.Map[Char,ProblemBSolverApp.CombinationCount
                                                  //| ] = Map(d -> CombinationCount(abbbccyccxccd,2), f -> CombinationCount(eeeeg
                                                  //| eef,4))
}