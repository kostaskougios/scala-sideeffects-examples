package sideeffects.monads

trait CanMap[A]:
  def map[R](f: A => R): R
