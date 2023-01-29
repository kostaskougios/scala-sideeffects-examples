package sideeffects

trait SideEffect

trait SideEffectResult[R]:
  def result: R

trait RunnableSideEffect:
  def run(): Unit
