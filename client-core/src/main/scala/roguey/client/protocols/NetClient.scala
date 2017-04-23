package roguey.client.protocols

import roguey.protocols._
import com.esotericsoftware.kryonet.{Connection, Listener, Client}
import scala.concurrent.{ExecutionContext, Future}
import com.twitter.logging.Logger

final case class NetClientConfig(
  host: String,
  tcpPort: Int,
  udpPort: Int,
  timeout: Int = 5000
)

class NetClient(config: NetClientConfig) {
  private val kryoClient: Client = new Client
  private val logger             = Logger(getClass)

  def start(): Unit = {
    Register.register(kryoClient.getKryo)
    kryoClient.addListener(new Listener {

      override def connected(connection: Connection): Unit = {
        logger.info("Logging in")
        val _ = sendTCP(Login("scout"))
      }

      override def received(connection: Connection, packet: Any): Unit = packet match {
        case packet: Packet =>
          packet match {
            case CreateEntity(entity) => println(entity)
            case RemoveEntity(entity) => println(entity)
            case LoadMap(_)           => logger.info("Loading map")
            case Login(_)             => ???
          }
        case keepAlive: com.esotericsoftware.kryonet.FrameworkMessage$KeepAlive =>
        case ignored                                                            => logger.warning(s"ignoring packet: $ignored")
      }
    })
    kryoClient.start()
    kryoClient.connect(config.timeout, config.host, config.tcpPort, config.udpPort)

  }

  def sendTCP[T <: Packet](packet: T): Int = {
    kryoClient.sendTCP(packet)
  }

  def sendUDP[T <: Packet](packet: T): Int = {
    kryoClient.sendUDP(packet)
  }

}
