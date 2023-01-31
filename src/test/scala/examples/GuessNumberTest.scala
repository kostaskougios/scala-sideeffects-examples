package examples

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import sideeffects.console.{InMemoryPrintLnLib, InMemoryReadLineLib}
import sideeffects.util.MockRandomLib

class GuessNumberTest extends AnyFunSuiteLike:
  val PleaseGuessPrompt = "Please guess a number between 1 and 100"

  test("correct guess") {
    new App(50, "50") {
      app.runApp()
      lib.printed should be(Seq(PleaseGuessPrompt, "You found it!"))
    }
  }

  test("Incorrect guess, guessed number less than actual number") {
    new App(50, "40", "exit") {
      app.runApp()
      lib.printed should be(Seq(PleaseGuessPrompt, "the number is greater than 40", PleaseGuessPrompt))
    }
  }

  test("more tests...") {
    // add more tests
  }

  class App(randomNumber: Int, stdIn: String*):
    val lib = new InMemoryPrintLnLib with MockRandomLib(randomNumber) with InMemoryReadLineLib(stdIn)
    val app = new GuessNumber(lib)
