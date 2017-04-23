package entitysystem

import reflect._

trait World {

  def entities: Entities
  def systems: Seq[System]

  def addEntity(entity: Entity): World
  def addSystem(system: System): World

  def process(delta: Float, entities: Entities, events: Seq[Event]): Seq[Event]

}

class MutableWorld() extends World {

  import collection.mutable

  private var mutableEntities: Entities                  = Entities()
  private var mutableEvents: Seq[Event]                  = Seq.empty
  private var mutableSystems: mutable.ListBuffer[System] = mutable.ListBuffer.empty

  def update(delta: Float): Unit = {
    val newEvents = process(delta, mutableEntities, mutableEvents)
    newEvents foreach {
      case AddEntity(entity) =>
        mutableEntities = mutableEntities.add(entity)
      case RemoveEntity(entity) =>
        mutableEntities = mutableEntities.remove(entity)
      case _ =>
    }
    mutableEvents = newEvents
  }

  override def systems  = mutableSystems
  override def entities = mutableEntities

  override def process(delta: Float, entities: Entities, events: Seq[Event]): Seq[Event] = {
    val newEvents = systems.flatMap(_.process(delta, entities, events))
    newEvents
  }

  override def addEntity(entity: Entity): World = {
    mutableEntities = mutableEntities.add(entity)
    this
  }

  override def addSystem(system: System): World = {
    mutableSystems.+=(system)
    this
  }

}
