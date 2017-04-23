package roguey.protocols

import roguey.macros._

sealed trait NetValue

sealed trait Packet

@packet class Location(x: Long, y: Long) extends NetValue

@packet class MapEntity(kind: Long, location: Location) extends NetValue

@packet class GameMap(entities: Array[MapEntity]) extends NetValue

@packet class Entity(id: Long, name: String) extends NetValue

@packet class Login(name: String) extends Packet with NetValue

@packet class LoadMap(map: GameMap) extends Packet with NetValue

@packet class CreateEntity(entity: Entity) extends Packet with NetValue

@packet class RemoveEntity(entity: Entity) extends Packet with NetValue
