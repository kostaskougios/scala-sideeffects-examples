package sideeffects.console

import sideeffects.{SideEffect, RunnableSideEffect}

trait PrintLnLib:
  def println(x: Any): Unit

trait StdPrintLnLib extends PrintLnLib:
  override def println(x: Any): Unit = Predef.println(x)
