package examples

import sideeffects.console
import sideeffects.console.{PrintLnLib, ReadLine, StdPrintLnLib, StdReadLine}
import sideeffects.util.{RandomLib, StdRandomLib}

@main def guessNumberApp() =

  val lib = new StdPrintLnLib with StdReadLine with StdRandomLib
  new GuessNumber(lib).runApp()

type GuessNumberLib = PrintLnLib with ReadLine with RandomLib

class GuessNumber(lib: GuessNumberLib):
  import lib.*
  def runApp(): Unit = runApp(Random.nextInt(100))

  private def runApp(r: Int): Unit =
    println("Please guess a number between 1 and 100")
    readLine() match
      case "exit" =>
      case s =>
        val n = s.toInt
        println(checkNumber(r, n))
        if n != r then runApp(r)

  private def checkNumber(r: Int, n: Int) =
    if n == r then "Found it!"
    else if n > r then s"the number is smaller than $n"
    else s"the number is greater than $n"
