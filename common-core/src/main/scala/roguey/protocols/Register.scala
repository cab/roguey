package roguey.protocols

// import com.esotericsoftware.kryonet.Kryo
import com.esotericsoftware.kryo.{Registration, Kryo}

import roguey.macros._

import reflect._

object Register {
  def register(implicit kryo: Kryo): Seq[Registration] = {
    val classes: Seq[Class[_]] = Sealed.classes[NetValue].toSeq ++
      (Sealed
        .classes[NetValue]
        .map(c => java.lang.reflect.Array.newInstance(c, 0).getClass())) ++
      Seq[Class[_]](
        classOf[Array[String]],
        classOf[Array[Int]]
      )
    classes.map(c => kryo.register(c)).toSeq
  }

  private def register[T](implicit kryo: Kryo, tag: ClassTag[T]): Registration = {
    kryo.register(tag.runtimeClass)
  }

}
