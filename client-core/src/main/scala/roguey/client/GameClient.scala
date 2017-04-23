package roguey.client

import roguey.engine
import protocols._
import roguey.server
import entitysystem._
import java.util.concurrent.Executors
import concurrent.{ExecutionContext, Await, Future}
import concurrent.duration._

class GameClient {
  implicit val context = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  val netClient = new NetClient(
    NetClientConfig(
      host = "localhost",
      tcpPort = 5959,
      udpPort = 5960
    ))
  netClient.start

  val world: MutableWorld = new MutableWorld()
  world.addSystem(new systems.RenderSystem())

  engine.Loop.run(() => (), deltaTime => {
    world.update(deltaTime.toFloat)
  })

}
