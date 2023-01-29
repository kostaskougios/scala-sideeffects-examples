package sideeffects.console

import scala.io.StdIn

trait ReadLineLib:
  def readLine(): String

trait StdReadLineLib extends ReadLineLib:
  override def readLine(): String = StdIn.readLine()
