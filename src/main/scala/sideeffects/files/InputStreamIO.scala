package sideeffects.files

import sideeffects.closables.Closable

import java.io.{FileInputStream, InputStream}
import scala.io.Source

class InputStreamIO private[files] (inputStreamCreator:() => InputStream) extends Closable[InputStreamIO,InputStream]( inputStreamCreator, _.close()):

  def mapLines[R](f: String => R): List[R] =
    map(in => Source.fromInputStream(in, "UTF-8").getLines().map(f).toList)
