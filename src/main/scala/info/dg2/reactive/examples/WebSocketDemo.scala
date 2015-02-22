package info.dg2.reactive.examples

import java.net.URI

import rx.lang.scala.Observable

import info.dg2.reactive.WebSocketObservable

/**
 * Apply the WebSocketObservable to process a real time stream of
 * Wikipedia activity
 */
object WebSocketDemo extends App {

  // Observable from a WebSocket connection
  val uri = new URI("ws://wiki-update-sockets.herokuapp.com/")
  val ws = WebSocketObservable(uri)

  // Let's test that the connection is closed when the
  // subscription is finished. We do that by zipping the
  // WebSocket observable with a simple finite observable.
  // When we run out of elements on the finite observable
  // the WebSocket should close
  val zippedObs = (Observable.just("1", "2", "3") zip ws)
  zippedObs
    .subscribe( (x: (String, String)) => println(x._1 +" " + x._2))

}
