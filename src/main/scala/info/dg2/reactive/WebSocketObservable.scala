package info.dg2.reactive

import java.net.URI

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import rx.lang.scala.{Observable, Subscriber}

/**
 * A basic WebSocket client implemented which allows for configurable
 * message handling and callback on close.
 *
 * Example:
 * ```
 * var client = new BasicClient(wiki, println)
 * client.connect()
 * ```
 * @param serverUri
 * @param msgHandler
 * @param closeCallback A function to be called when the WebSocket connection is closed
 */
case class BasicClient(serverUri: URI,
                       msgHandler: String => Unit,
                       closeCallback: Option[() => Unit] = None)
  extends WebSocketClient(serverUri)
{
  override def onError(ex: Exception) = ex.printStackTrace()
  override def onClose(code: Int, reason: String, remote: Boolean) = {
    println("Connection closed by " + (if (remote) "remote peer" else "us"))
    closeCallback.foreach(f => f())
  }
  override def onMessage(msg: String) = msgHandler(msg)
  override def onOpen(handshakeData: ServerHandshake) = println("Connection open")
}

/**
 * A Rx-Aware WebSocket client which pushes messages into the subscriber, closes the
 * connection when the subscriber is no longer active and completes the subscription
 * if the connection is closed by any other means.
 *
 * @param serverUri
 * @param subscriber
 */
case class ObservableBasicClient(serverUri: URI,
                                 subscriber: Subscriber[String])
  extends WebSocketClient(serverUri)
{
  override def onError(ex: Exception) =
    if (!subscriber.isUnsubscribed) subscriber.onError(ex)

  override def onClose(code: Int, reason: String, remote: Boolean) = {
    println("Connection closed by " + (if (remote) "remote peer" else "us"))
    subscriber.onCompleted()
  }

  override def onMessage(msg: String) =
    if (subscriber.isUnsubscribed) this.close() else subscriber.onNext(msg)

  override def onOpen(handshakeData: ServerHandshake) = println("Connection open")
}

/**
 * Wrap a WebSocket client in an Observable so that it can be used
 * with RxScala
 */
object WebSocketObservable {
  def apply(uri: URI): Observable[String] = {
    Observable { subscriber =>
      ObservableBasicClient(uri, subscriber).connect()
    }
  }
}
