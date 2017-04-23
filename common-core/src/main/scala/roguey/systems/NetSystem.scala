package roguey.systems

import entitysystem._
import roguey.protocols.{Packet, Endpoint, EndpointListener}
import com.esotericsoftware.kryonet.Connection
import java.util.concurrent.ConcurrentLinkedQueue

import collection.mutable

import annotation.tailrec

case class PacketEvent(packet: Packet, connection: Connection) extends Event

class NetSystem(endpoint: Endpoint) extends System with EndpointListener {

  private val packets: ConcurrentLinkedQueue[PacketEvent] = new ConcurrentLinkedQueue

  endpoint.addListener(this)

  @tailrec
  private def clearPackets(accum: Seq[PacketEvent]): Seq[PacketEvent] = {
    val next = packets.poll()
    if (next == null) {
      accum ++ Nil
    } else {
      clearPackets(accum :+ next)
    }
  }

  def process(delta: Float, entities: Entities, events: Seq[Event]): Seq[Event] = {
    clearPackets(Nil)
  }

  def onPacket(packet: Packet, connection: Connection): Unit = {
    val _ = packets.add(PacketEvent(packet, connection))
  }

}
