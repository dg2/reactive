package info.dg2.reactive.examples

import java.net.URI
import java.util.concurrent.TimeUnit

import rx.lang.scala.Observable
import info.dg2.reactive.WebSocketObservable

import scala.concurrent.duration.FiniteDuration

/**
 * Apply the WebSocketObservable to process a real time stream of
 * Wikipedia activity
 */
object WebSocketDemo extends App {
  val windowLen = 5

  // Observable from a WebSocket connection
  val uri = new URI("ws://wiki-update-sockets.herokuapp.com/")
  val ws = WebSocketObservable(uri)

  // Let's test that the connection is closed when the
  // subscription is finished.
  ws
    .take(10)
    .subscribe( (x: String) => println(x) )

  // In parallel let's count the events in windows of 5 seconds
  ws
    .map(_ => 1)
    .tumblingBuffer(new FiniteDuration(windowLen, TimeUnit.SECONDS))
    .map(_.sum)
    .take(5)
    .subscribe(v => println(s"$v updates in the last $windowLen seconds"))

}
