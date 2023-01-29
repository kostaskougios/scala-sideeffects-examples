package sideeffects.util

trait MockRandomLib(intVal: Int) extends RandomLib:
  override val Random = new ScalaRandom:
    override def nextInt(n: Int) = intVal
