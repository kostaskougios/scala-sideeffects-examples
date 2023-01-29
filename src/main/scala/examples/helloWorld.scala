package examples

import sideeffects.console.{PrintLnLib, StdPrintLnLib}

// Everyone wrote a hello world program. No one wrote the test case for it. This is why!
@main def veryHardToTestHelloWorld() = println("Hello World!") // now try to write a test for this!

// What happened at veryHardToTestHelloWorld()? Why is it so hard to test? The problem is that
// we have no control over the side effect that println does (well if we try hard we can hack it
// with a couple of tricks). Effect libs like IO { println(...) } don't improve this and
// on top of that add significant complexity.
@main def helloWorld =
  val lib = new StdPrintLnLib {}
  new HelloWorldPrinter(lib).helloWorld()

class HelloWorldPrinter(lib: PrintLnLib):
  import lib.*
  def helloWorld(): Unit = println("Hello World!") // println is actually lib.println
