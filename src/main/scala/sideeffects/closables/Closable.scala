package sideeffects.closables

import java.io.InputStream

trait Closable[C](allocateResource: () => C, close: C => Unit):
  def flatMap[R](f: C => R): R = map(f)
  def foreach(f: C => Unit): Unit =
    val r = allocateResource()
    try f(r)
    finally close(r)

  def map[R](f: C => R): R =
    val r = allocateResource()
    try f(r)
    finally close(r)
