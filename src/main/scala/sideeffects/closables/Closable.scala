package sideeffects.closables

import java.io.InputStream

trait Closable[A: Close]:
  protected def allocateResource: A
  def foreach(f: A => Unit): Unit =
    val r = allocateResource
    try f(r)
    finally summon[Close[A]].close(r)

  def map[R](f: A => R): R =
    val r = allocateResource
    try f(r)
    finally summon[Close[A]].close(r)

trait Close[A]:
  def close(a: A): Unit
