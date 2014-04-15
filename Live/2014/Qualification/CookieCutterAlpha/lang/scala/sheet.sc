object sheet {
  implicit val repetitions: Int = 1               //> repetitions  : Int = 1

  def time[R](block: => R)(implicit repetitions: Int): R = {
    val t0 = System.nanoTime()
    var result: R = block
    var repsLeft = repetitions - 1
    while (repsLeft > 0) {
      result = block    // call-by-name
      repsLeft -= 1
    }
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) / 1.0e6 / repetitions + " ms")
    result
  }                                               //> time: [R](block: => R)(implicit repetitions: Int)R

  val minC = 1                                    //> minC  : Int = 1
  val maxC = 10000                                //> maxC  : Int = 10000
  val minF = 1                                    //> minF  : Int = 1
  val maxF = 100                                  //> maxF  : Int = 100
  val minX = 1                                    //> minX  : Int = 1
  val maxX = 100000                               //> maxX  : Int = 100000

  time { SolverApp.solve(costPerCookieFarm = minC, ratePerCookieFarm = minF, targetNumberOfCookies = minX) }
                                                  //> Elapsed time: 15.220183 ms
                                                  //| res0: Double = 0.5
  time { SolverApp.solve(costPerCookieFarm = minC, ratePerCookieFarm = minF, targetNumberOfCookies = maxX) }
                                                  //> Elapsed time: 5.912064 ms
                                                  //| res1: Double = 12.090136129863371
  time { SolverApp.solve(costPerCookieFarm = minC, ratePerCookieFarm = maxF, targetNumberOfCookies = minX) }
                                                  //> Elapsed time: 0.017121 ms
                                                  //| res2: Double = 0.5
  time { SolverApp.solve(costPerCookieFarm = minC, ratePerCookieFarm = maxF, targetNumberOfCookies = maxX) }
                                                  //> Elapsed time: 4.215749 ms
                                                  //| res3: Double = 0.6305770977541738
  time { SolverApp.solve(costPerCookieFarm = maxC, ratePerCookieFarm = minF, targetNumberOfCookies = minX) }
                                                  //> Elapsed time: 0.009417 ms
                                                  //| res4: Double = 0.5
  time { SolverApp.solve(costPerCookieFarm = maxC, ratePerCookieFarm = minF, targetNumberOfCookies = maxX) }
                                                  //> Elapsed time: 0.008989 ms
                                                  //| res5: Double = 28289.682539682537
  time { SolverApp.solve(costPerCookieFarm = maxC, ratePerCookieFarm = maxF, targetNumberOfCookies = minX) }
                                                  //> Elapsed time: 0.008561 ms
                                                  //| res6: Double = 0.5
  time { SolverApp.solve(costPerCookieFarm = maxC, ratePerCookieFarm = maxF, targetNumberOfCookies = maxX) }
                                                  //> Elapsed time: 0.008989 ms
                                                  //| res7: Double = 5379.642572445766

}