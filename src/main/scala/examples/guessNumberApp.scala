package examples

import sideeffects.console
import sideeffects.console.{PrintLnLib, ReadLineLib, StdPrintLnLib, StdReadLineLib}
import sideeffects.util.{RandomLib, StdRandomLib}

@main def guessNumberApp() =
  val lib = new StdPrintLnLib with StdReadLineLib with StdRandomLib
  new GuessNumber(lib).runApp()

type GuessNumberLib = PrintLnLib with ReadLineLib with RandomLib

class GuessNumber(lib: GuessNumberLib):
  import lib.*
  def runApp(): Unit = runApp(Random.nextInt(100)) // this uses lib.Random

  private def runApp(r: Int): Unit =
    println("Please guess a number between 1 and 100")
    readLine() match
      case "exit" =>
      case s =>
        val n = s.toInt
        println(checkNumber(r, n))
        if n != r then runApp(r)

  private def checkNumber(r: Int, n: Int) =
    if n == r then "You found it!"
    else if n > r then s"the number is smaller than $n"
    else s"the number is greater than $n"
