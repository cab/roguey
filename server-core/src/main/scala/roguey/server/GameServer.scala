package roguey.server

import roguey.engine
import protocols._
import roguey.protocols._
import roguey.server
import entitysystem._
import java.util.concurrent.Executors
import concurrent.{ExecutionContext, Await, Future}
import concurrent.duration._

class GameServer {
  implicit val context = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  def start(): Unit = {
    println("Starting")

    val world: MutableWorld = new MutableWorld()

    val netServer = new NetServer(
      NetServerConfig(
        tcpPort = 5959,
        udpPort = 5960
      ))

    world.addSystem(new roguey.systems.NetSystem(netServer))
    world.addSystem(new systems.LoginSystem)

    netServer.start

    val loop = engine.Loop.run(() => (), deltaTime => {
      world.update(deltaTime.toFloat)
    })

  }

}
