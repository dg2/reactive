package info.dg2.reactive.examples

import rx.lang.scala.{Observable, Observer}

/**
 * Created by dario on 22/02/15.
 */
object Basics extends App {
  val obs: Observable[Int] = Observable.from(Array(1, 2, 3, 4))

  // Generate a new Observable by mapping
  // an existing one
  val other: Observable[Int] = obs
    .map(2 * _)

  // Act on an observable by subscribing to it
  // Long version:
  other
    .subscribe(Observer[Int]( (x: Int) => println(x)) )

  // Zipping observables also yield another observable
  val zipped: Observable[(Int, Int)] = (obs zip other)

  zipped
    .subscribe( (x: (Int, Int)) => println(s"${x._1} -> ${x._2}") )

}
