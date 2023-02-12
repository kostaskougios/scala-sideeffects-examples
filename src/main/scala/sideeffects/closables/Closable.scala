package sideeffects.closables

import java.io.InputStream

trait Closable[A: Close](a: A):
  def foreach(f: A => Unit): Unit =
    try f(a)
    finally summon[Close[A]].close(a)

  def map[R](f: A => R): R =
    try f(a)
    finally summon[Close[A]].close(a)

trait Close[A]:
  def close(a: A): Unit
