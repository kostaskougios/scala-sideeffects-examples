package sideeffects.console

import scala.collection.mutable.ArrayBuffer

trait InMemoryPrintLnLib extends PrintLnLib:
  private val p = ArrayBuffer.empty[String]
  def printed = p.toList
  override def println(x: Any): Unit = p += x.toString
