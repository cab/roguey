package entitysystem

import org.scalatest._
import entitysystem._


case object XKey extends AspectKey

case object X extends Aspect {
  implicit val _: HasAspectKey[X.type] = HasAspectKey[X.type] { XKey }
}

case object YKey extends AspectKey

case object Y extends Aspect {
  implicit val _: HasAspectKey[Y.type] = HasAspectKey[Y.type] { YKey }
}

object TestSystem extends System {
  def process(delta: Float, entities: Entities, events: Seq[Event]): Seq[Event] =
    Seq(
      AddEntity(Entity(Map(Y.key -> Y)))
    ) ++ entities.findAll(X.key).map(RemoveEntity)
}

class EntityTest extends FlatSpec with Matchers {
  it should "x" in {
    val entity1 = Entity(Map(X.key -> X, Y.key -> Y))
    val entity2 = Entity(Map(X.key -> X))
    val entity3 = Entity(Map(Y.key -> Y))
    val entities = Entities(entity1, entity2, entity3)
    println(entities.findAll(X.key))
    println(entities.findAll(Y.key))
    println(entities.findAll(X.key, Y.key))
  }

  it should "world" in {
    val entity1 = Entity(Map(X.key -> X))
    val entity2 = Entity(Map(X.key -> X))
    val world = new MutableWorld()
    world.addEntity(entity1)
    world.addEntity(entity2)
    world.addSystem(TestSystem)
    println(world.entities)
    world.update(20f)
    println(world.entities)
  }

}
