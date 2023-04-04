package sideeffects.monads

trait CanForeach[A]:
  def foreach(f: A => Unit): Unit
