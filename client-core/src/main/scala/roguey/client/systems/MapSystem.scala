package roguey.client.systems

import entitysystem._
import roguey.protocols._
import roguey.systems._

class MapSystem extends System {

  def process(delta: Float, entities: Entities, events: Seq[Event]): Seq[Event] = events flatMap {
    case PacketEvent(LoadMap(map), connection) =>
      println("Loading map!")
      Nil
    case ignored => Nil
  }

}
