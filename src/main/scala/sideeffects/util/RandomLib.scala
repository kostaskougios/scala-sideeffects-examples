package sideeffects.util

import scala.util.Random

trait RandomLib:
  def Random: ScalaRandom

trait ScalaRandom:
  def nextInt(n: Int): Int

trait StdRandomLib extends RandomLib:
  override val Random = new StdScalaRandom

private class StdScalaRandom extends Random with ScalaRandom

