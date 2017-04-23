package roguey.server.systems

import entitysystem._
import roguey.protocols._
import roguey.systems._
import com.twitter.logging.Logger

class LoginSystem() extends System {

  private val logger = Logger(getClass)

  def process(delta: Float, entities: Entities, events: Seq[Event]): Seq[Event] = events flatMap {
    case PacketEvent(Login(name), connection) =>
      logger.info(s"$name logged in")
      connection.sendTCP(LoadMap(GameMap(Array.empty)))
      Nil
    case ignored => Nil
  }
}
