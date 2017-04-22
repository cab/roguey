package roguey.macros

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

object Sealed {
  def classes[A]: Set[Class[_]] = macro classes_impl[A]

  def classes_impl[A: c.WeakTypeTag](c: Context) = {
    import c.universe._

    val symbol = weakTypeOf[A].typeSymbol

    if (!symbol.isClass) c.abort(
      c.enclosingPosition,
      "Can only enumerate classes of a sealed trait or class."
    ) else if (!symbol.asClass.isSealed) c.abort(
      c.enclosingPosition,
      "Can only enumerate classes of a sealed trait or class."
    ) else {
      def getClasses(baseSymbol: Symbol): Seq[ClassSymbol] = {
        val base = baseSymbol.asClass.knownDirectSubclasses.map(_.asClass).toSeq
        base.filter(_.isTrait).filter(_.isSealed) match {
          case Nil => base
          case list => list.flatMap(i => getClasses(i)) ++ base
        }
      }
      val allClasses = getClasses(symbol)
      q"Set[Class[_]](..${allClasses.map(c => q"classOf[$c]")})"
    }
  }
}
