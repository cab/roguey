package entitysystem

import reflect._

trait Event

sealed trait WorldEvent extends Event

final case class AddEntity(entity: Entity) extends WorldEvent
final case class RemoveEntity(entity: Entity) extends WorldEvent
final case class ModifyEntity(entity: Entity) extends WorldEvent

trait System {
  def process(delta: Float, entities: Entities, events: Seq[Event]): Seq[Event]
}

abstract class IntervalSystem(interval: Float) extends System {

  private var timeBuffer: Float = 0

  def process(delta: Float, entities: Entities, events: Seq[Event]): Seq[Event] = {
    timeBuffer += delta
    if (timeBuffer >= interval) {
      val newEvents = updateInterval(timeBuffer, entities, events)
      timeBuffer = 0
      newEvents
    } else {
      Nil
    }
  }

  def updateInterval(delta: Float, entities: Entities, events: Seq[Event]): Seq[Event]
}

abstract class IteratingSystem(aspects: Seq[ClassTag[_ <: Aspect]]) extends System {

  def process(delta: Float, entities: Entities, events: Seq[Event]): Seq[Event] = {
    entities.findAll(aspects).flatMap(processEntity(delta, _, events))
  }

  def processEntity(delta: Float, entity: Entity, events: Seq[Event]): Seq[Event]

}
