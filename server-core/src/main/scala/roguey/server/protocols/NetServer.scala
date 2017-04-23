package roguey.server.protocols

import roguey.protocols._
import com.esotericsoftware.kryonet.{Connection, Server, Listener}
import scala.concurrent.{ExecutionContext, Future}

import com.twitter.logging.Logger

final case class NetServerConfig(
  tcpPort: Int,
  udpPort: Int
)

class NetServer(config: NetServerConfig) extends Endpoint {
  private val kryoServer: Server                = new Server(8192, 2048)
  private val logger                            = Logger(getClass)
  private var listeners: Seq[EndpointListener] = Nil

  override def addListener(listener: EndpointListener): Unit = {
    listeners = listeners :+ listener
  }

  def start(): Unit = {
    Register.register(kryoServer.getKryo)
    kryoServer.start()
    kryoServer.bind(config.tcpPort, config.udpPort)
    kryoServer.addListener(new Listener {
      override def received(connection: Connection, packet: Any): Unit = packet match {
        case packet: Packet =>
          listeners.foreach(_.onPacket(packet, connection))
        // packet match {
        //   case CreateEntity(entity) => println(entity)
        //   case RemoveEntity(entity) => println(entity)
        //   case Login(username)      => logger.info(":)")
        //   case LoadMap(_) =>
        //     logger.warning(s"Client should not send $packet")
        //     connection.close
        // }
        case keepAlive: com.esotericsoftware.kryonet.FrameworkMessage$KeepAlive =>
        case ignored                                                            => logger.warning(s"ignoring packet: $ignored")
      }
    })
  }

  def sendTCP[T](packet: T): Unit = {
    kryoServer.sendToAllTCP(packet)
  }

  def sendUDP[T](packet: T): Unit = {
    kryoServer.sendToAllUDP(packet)
  }

}
