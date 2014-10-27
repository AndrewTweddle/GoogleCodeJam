import scala.collection.mutable.Map

object Worksheet {
  def testGraph(sets: List[String]) {
    val graphOrNone =  ProblemBSolverApp.buildGraphOrNone(sets)
    graphOrNone match {
    	case Some(graph) => {
    	  val hasCycles = ProblemBSolverApp.hasCycles(graph)
    	  println(s"Has cycles: $hasCycles")
    	  val permutationCount = ProblemBSolverApp.getValidPermutationCount(graph)
    	  println(s"Permutations: $permutationCount")
    	}
    	case None => println("No solution");
    }
    
    graphOrNone
  }                                               //> testGraph: (sets: List[String])Unit

  ProblemBSolverApp.splitIntoSubSetsInReverseOrder("aaabccdddd")
                                                  //> res0: List[ProblemBSolverApp.SubSet] = List(SubSet(d,4,Some(c),None), SubSet
                                                  //| (c,2,Some(b),Some(d)), SubSet(b,1,Some(a),Some(c)), SubSet(a,3,None,Some(b))
                                                  //| )

  testGraph(List("aa", "b", "bb", "ab", "ccd", "effg", "bc"))
                                                  //> Has cycles: false
                                                  //| Permutations: 4
                                                
  testGraph(List("aba", "ca"))                    //> No solution
  testGraph(List("aba", "ac"))                    //> No solution
  testGraph(List("ab", "ba"))                     //> Has cycles: true
                                                  //| Permutations: 0
  testGraph(List("aba"))                          //> Has cycles: true
                                                  //| Permutations: 0
  testGraph(List("efg", "f"))                     //> No solution
  testGraph(List("f", "efg"))                     //> No solution
  testGraph(List("a"))                            //> Has cycles: false
                                                  //| Permutations: 1
  testGraph(List("a", "b", "c"))                  //> Has cycles: false
                                                  //| Permutations: 6
  testGraph(List("a", "aa", "ab", "b"))           //> Has cycles: false
                                                  //| Permutations: 2
  testGraph(List("a", "aa", "ab", "b", "c", "cc", "ccc"))
                                                  //> Has cycles: false
                                                  //| Permutations: 24
  testGraph(List("a", "aa", "ab", "b", "c", "cc", "ccc", "cd", "e"))
                                                  //> Has cycles: false
                                                  //| Permutations: 72
  testGraph(List("a","a","a"))                    //> Has cycles: false
                                                  //| Permutations: 6
  testGraph(List("a","a","a", "b", "b", "b"))     //> Has cycles: false
                                                  //| Permutations: 72
}