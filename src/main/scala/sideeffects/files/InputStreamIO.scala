package sideeffects.files

import sideeffects.closables.{Closable, Close}
import sideeffects.{SideEffect, SideEffectResult}

import java.io.{FileInputStream, InputStream}
import scala.io.Source

class InputStreamIO private[files](inputStream: InputStream) extends Closable(inputStream):

  def mapLines[R](f: String => R): List[R] =
    map(in => Source.fromInputStream(in, "UTF-8").getLines().map(f).toList)

given Close[InputStream] with {
  override def close(a: InputStream): Unit = a.close()
}