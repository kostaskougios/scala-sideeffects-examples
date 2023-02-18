package sideeffects.files

import sideeffects.closables.Closable

import java.io.{FileInputStream, InputStream}
import scala.io.Source

class InputStreamIO private[files] (inputStreamCreator: () => InputStream) extends Closable[InputStream](inputStreamCreator, _.close()):
  def flatMap[R](f: InputStream => R): R = map(f)

  def mapLines[R](f: String => R): List[R] =
    map(in => Source.fromInputStream(in, "UTF-8").getLines().map(f).toList)
