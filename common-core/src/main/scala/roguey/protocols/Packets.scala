package roguey.protocols

import roguey.macros._

sealed trait NetValue

sealed trait Packet extends NetValue


@packet class Location(x: Long, y: Long) extends NetValue

@packet class MapEntity(location: Location) extends NetValue

@packet class Map(entities: Seq[MapEntity]) extends NetValue

@packet class Entity(id: Long, name: String) extends NetValue

@packet class Login(name: String) extends Packet

@packet class LoadMap(map: Map) extends Packet

@packet class CreateEntity(entity: Entity) extends Packet

@packet class RemoveEntity(entity: Entity) extends Packet
