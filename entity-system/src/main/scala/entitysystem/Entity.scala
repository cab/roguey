package entitysystem

import reflect._

class Entity private (
    val id: Int,
    val aspects: Map[ClassTag[_ <: Aspect], _ <: Aspect]
) {

  val keys: Set[ClassTag[_ <: Aspect]] = aspects.keys.toSet

  def apply[A <: Aspect](implicit tag: ClassTag[A]): Option[A] =
    aspects.get(tag).map(_.asInstanceOf[A])

  override def toString =
    s"""Entity(${id}, ${aspects.values.map(_.toString).mkString(", ")})"""

}

object Entity {
  private var currentId: Int = 0
  private def nextId: Int = synchronized {
    val i = currentId
    currentId += 1
    i
  }

  def apply(aspects: Map[ClassTag[_ <: Aspect], _ <: Aspect]): Entity = {
    val id = nextId
    new Entity(id, aspects)
  }

  // private def getAspect[AspectKey, Aspect](entity: Entity[AspectKey, Aspect], prism: Prism[Aspect, A])(implicit ev: HasAspectKey[A]) = {
  //   val aspects = (entity applyLens _aspects).get
  //
  //   aspects.get(ev.key).toMaybe.flatMap { aspect =>
  //     prism.getMaybe(aspect)
  //   }
  // }

}
