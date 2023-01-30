package sideeffects.console

trait InMemoryReadLineLib(lines: Seq[String]) extends ReadLineLib:
  private var l = 0
  override def readLine(): String =
    val s = lines(l)
    l += 1
    s
