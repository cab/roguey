package roguey.client

import roguey.engine
import protocols._
import roguey.server.GameServer
import entitysystem._
import java.util.concurrent.Executors
import concurrent.{ExecutionContext, Await, Future}
import concurrent.duration._

object App extends _root_.scala.App {

  implicit val context = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  Future {
    val server = new GameServer()
    server.start
  }

  new GameClient()

}
