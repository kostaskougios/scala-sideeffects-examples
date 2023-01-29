package sideeffects.console

trait PrintLnLib:
  def println(x: Any): Unit

trait StdPrintLnLib extends PrintLnLib:
  override def println(x: Any): Unit = Predef.println(x)
