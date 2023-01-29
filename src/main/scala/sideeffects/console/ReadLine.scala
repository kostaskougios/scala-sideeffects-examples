package sideeffects.console

import sideeffects.{SideEffectResult, RunnableSideEffect}

import scala.io.StdIn

trait ReadLine:
  def readLine(): String

trait StdReadLine extends ReadLine:
  override def readLine(): String = StdIn.readLine()
