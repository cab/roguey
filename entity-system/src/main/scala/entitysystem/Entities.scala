package entitysystem

import reflect._

class Entities(private val entities: Map[Int, Entity]) {
  lazy val values: List[Entity] = entities.values.toList
  override def toString = values.mkString("Entities(", ", ", ")")

  def findAll(x: ClassTag[_ <: Aspect], xs: ClassTag[_ <: Aspect]*): List[Entity] = {
    val keys = (x +: xs)
    findAll(keys)
  }

  def findAll(keys: Seq[ClassTag[_ <: Aspect]]): List[Entity] = {
    val keySet = keys.toSet
    values.filter(entity => keySet.subsetOf(entity.keys))
  }

  val all: List[Entity] = values

  private[entitysystem] def add(entity: Entity): Entities = {
    new Entities(entities + ((entity.id, entity)))
  }

  private[entitysystem] def add(newEntities: Seq[Entity]): Entities = {
    new Entities(entities ++ newEntities.map(e => (e.id, e)).toMap)
  }

  private[entitysystem] def remove(entity: Entity): Entities = {
    new Entities(entities - (entity.id))
  }

}

object Entities {

  def apply(): Entities = new Entities(Map.empty)

  def apply(x: Entity, xs: Entity*): Entities = {
    val entities = (x +: xs).map { x =>
      x.id -> x
    }.toMap

    new Entities(entities)
  }


}
