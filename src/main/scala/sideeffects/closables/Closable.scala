package sideeffects.closables

import sideeffects.monads.{CanForeach, CanMap}

import java.io.InputStream

trait Closable[C](allocateResource: () => C, close: C => Unit) extends CanMap[C] with CanForeach[C]:
  def foreach(f: C => Unit): Unit =
    val r = allocateResource()
    try f(r)
    finally close(r)

  override def map[R](f: C => R): R =
    val r = allocateResource()
    try f(r)
    finally close(r)