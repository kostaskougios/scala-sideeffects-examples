package examples

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import sideeffects.console.{InMemoryPrintLnLib, InMemoryReadLineLib}
import sideeffects.util.MockRandomLib

class GuessNumberTest extends AnyFunSuiteLike:

  test("correct guess") {
    new App(
      50,
      "50"
    ) {
      app.runApp()
      lib.printed should be(Seq("Please guess a number between 1 and 100", "Found it!"))
    }
  }

  class App(randomNumber: Int, stdIn: String*):
    val lib = new InMemoryPrintLnLib with MockRandomLib(randomNumber) with InMemoryReadLineLib(stdIn)
    val app = new GuessNumber(lib)
