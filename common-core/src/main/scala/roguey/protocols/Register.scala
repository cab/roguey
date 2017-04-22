package roguey.protocols

// import com.esotericsoftware.kryonet.Kryo
import com.esotericsoftware.kryo.{Registration, Kryo}

import roguey.macros._

import reflect._

object Register {
  def register(implicit kryo: Kryo): Seq[Registration] = {
    println(Sealed.classes[NetValue])
    Sealed.classes[NetValue].map(c => kryo.register(c)).toSeq
  }

  private def register[T](implicit kryo: Kryo, tag: ClassTag[T]): Registration = {
    kryo.register(tag.runtimeClass)
  }

}
