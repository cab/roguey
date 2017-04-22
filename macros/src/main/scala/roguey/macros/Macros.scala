package roguey.macros

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

/**
* A packet is like a `case class` with mutable fields and a no-arg constructor
*/
class packet[T] extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro packetMacro.impl
}

object packetMacro {
  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    val result = {
      annottees.map(_.tree).toList match {
        case r @ q"class $name(..$fields) extends $ex with ..$w { ..$body }" :: Nil =>

          val fieldTypes = fields.map {
            case q"$accessors val $name: $tpe = $v" => tpe
            case fail =>
              println(fail)
              c.abort(c.enclosingPosition, "Could not determine field type")
          }
          val fieldNames = fields.map {
            case q"$accessors val $name: $tpe = $v" => name
            case fail =>
              println(fail)
              c.abort(c.enclosingPosition, "Could not determine field name")
          }
          val updatedFields = fields.map {
            case q"$accessors val $name: $tpe = $v" => q"var $name: $tpe = $v"
            case fail =>
              println(fail)
              c.abort(c.enclosingPosition, "Could not determine field")
          }
          val fieldDefaults = fields.map {
            case q"$accessors val $name: $tpe = $v" => v
            case fail =>
              println(fail)
              c.abort(c.enclosingPosition, "Could not determine field")
          }
          // It looks like typer sometimes uses positions to decide whether stuff
          // (secondary constructors in this case) typechecks or not (?!!):
          // https://github.com/xeno-by/scala/blob/c74e1325ff1514b1042c959b0b268b3c6bf8d349/src/compiler/scala/tools/nsc/typechecker/Typers.scala#L2932
          //
          // In general, positions are important in getting error messages and debug
          // information right, but maintaining positions is too hard, so macro writers typically don't care.
          //
          // This has never been a problem up until now, but here we're forced to work around
          // by manually setting an artificial position for the secondary constructor to be greater
          // than the position that the default constructor is going to get after macro expansion.
          val defaultCtorPos = c.enclosingPosition
          val newCtorPos = defaultCtorPos
            .withEnd(defaultCtorPos.end + 1)
            .withStart(defaultCtorPos.start + 1)
            .withPoint(defaultCtorPos.point + 1)
          val newCtor = q"def this() = this(..$fieldDefaults)"
          val x = q"""
          class $name(..$updatedFields) extends $ex with ..$w {
            ${atPos(newCtorPos)(newCtor)}
          }
          object ${TermName(name.toString)} {

            def apply(..${updatedFields}): $name = new $name(..${fieldNames})

            def unapply(value: $name) = Some((
              ..${fieldNames.map(f => q"value.$f")}
            ))
          }
          """
          println(x)
          x
        case err =>
          println(err)
          c.abort(c.enclosingPosition, "@packet can only be used with classes")
      }
    }
    c.Expr[Any](result)
  }
}
