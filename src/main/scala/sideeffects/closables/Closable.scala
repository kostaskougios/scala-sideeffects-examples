package sideeffects.closables

import java.io.InputStream

trait Closable[A[C],C](allocateResource: () => C, close: C => Unit):
  def flatMap[R](f: C => R): A[R] = ???
  def foreach(f: C => Unit): Unit =
    val r = allocateResource()
    try f(r)
    finally close(r)

  def map[R](f: C => R): R =
    val r = allocateResource()
    try f(r)
    finally close(r)
