package examples

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers._
import sideeffects.console.InMemoryPrintLnLib

class HelloWorldPrinterTest extends AnyFunSuiteLike:
  val lib = new InMemoryPrintLnLib {}

  test("it does print hello world!") {
    new HelloWorldPrinter(lib).helloWorld()
    lib.printed should be(Seq("Hello World!"))
  }
