package roguey.server

import roguey.engine
import protocols._
import roguey.protocols._
import roguey.server
import java.util.concurrent.Executors
import concurrent.{ExecutionContext, Await, Future}
import concurrent.duration._

class GameServer {
  implicit val context = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  def start(): Unit = {
    println("Starting")
    val netServer = new NetServer(NetServerConfig(
      tcpPort = 5959,
      udpPort = 5960
    ))
    netServer.start
    netServer.sendTCP(CreateEntity(Entity(2, "hi")))
  }

}
