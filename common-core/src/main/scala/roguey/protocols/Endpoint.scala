package roguey.protocols

import com.esotericsoftware.kryonet.Connection

trait EndpointListener {
  def onPacket(packet: Packet, connection: Connection): Unit
}

trait Endpoint {
  def addListener(listener: EndpointListener): Unit
}
