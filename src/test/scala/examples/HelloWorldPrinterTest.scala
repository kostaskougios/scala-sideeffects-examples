package examples

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import sideeffects.console.InMemoryPrintLnLib

class HelloWorldPrinterTest extends AnyFunSuiteLike:
  def createLib = new InMemoryPrintLnLib {}
  test("it does print hello world!") {
    val lib = createLib
    new HelloWorldPrinter(lib).helloWorld()
    lib.printed should be(Seq("Hello World!"))
  }
