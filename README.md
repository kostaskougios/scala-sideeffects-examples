# A pragmatic approach to code side effects

## Everyone wrote a hello world program but no one wrote a test for it, this is why.

We'll see the simplest side effect program, one that prints "Hello world" to the console.
As the title implies we will see why it is very hard to write a test for it and a pragmatic
solution that results in as-simple-as code as the original program but code that is easy to
test.


### Hello world but untestable

So lets see our hello world app:

```scala
@main def veryHardToTestHelloWorld() = println("Hello World!") // now try to write a test for this!
```

This code is simple but writing a test for it is a nightmare. We may be able to redirect the console to a file 
or output stream, but overall not something we want to do. Especially when an app grows, it will be a nightmare
to write and maintain the testcase. Later on we'll see the guess-a-number app that reads from stdin and prints, 
so things would get out of hand pretty quickly when it comes to testing and the hardcoded println and readln methods.

### Why is it untestable?

It is untestable because we are not in control of println. It is hardcoded to write to the console and that can hardly change.
In my opinion the problem with side effects is not how to make them functional but how to gain control of what
they do. I.e. wrapping the above code in an `IO { println (...) }` doesn't help in terms of testing. We may
have a pure function but the result is not something we can use straight away other than running it. And then
we got the same problem, the untestable side effect.

Testing println's may not be the most useful thing in actual commercial projects, but we can use the same
solution to test i.e. files, jdbc calls, sftp uploads, api calls, messaging queues etc.

### Making it testable

So lets go make our code testable. Testing side effects IMO is a matter of dependency injection rather than
anything else. In the println example we are limited by the fact that println is a hardcoded method we have
no control over. To gain control we will put it in a trait and make it injectable so that we can inject
the println we want in prod code (which will just print to the console) or the println we want in the
tests (which will allow us to assert). Assuming we have a lib that is already tested and that the injectable println
works as expected, then we won't have to bother with streams, console etc when testing. Let's see it in action:

```scala
trait PrintLnLib:
  def println(x: Any): Unit

trait StdPrintLnLib extends PrintLnLib:
  override def println(x: Any): Unit = Predef.println(x)
```

Ok so here controlling println is easy. Lets see how we can use it by refactoring our hello world app:

```scala
class HelloWorldPrinter(lib: PrintLnLib):
  import lib.*
  def helloWorld(): Unit = println("Hello World!") // println is actually lib.println
```

So here we will inject the `PrintLnLib` that we need. Now we are in control of the side effect method. In
production, we can call it like:

```scala
@main def helloWorld =
  val lib = new StdPrintLnLib {}
  new HelloWorldPrinter(lib).helloWorld()
```

Ok our code grew a bit compared to the initial hello world but still it is very simple, we don't have wrappers like `IO`
that complicates our code and things work as expected for pretty standard code, i.e. the code is very similar to
the original hello-world, stacktraces will be straight forward etc. The overhead of injecting the lib will be minimal
for real life applications where the bulk of the code will be implementing logic.

The big benefit? It is easy to test and we can write complete tests (tests that test every bit of our app) for it. Now
compare that to that project at work!

### The test

It is time to test it. First lets create a lib we can use for tests. We could as well use mocking but for this example
we can just write some simple scala code.

```scala
trait InMemoryPrintLnLib extends PrintLnLib:
  private val p = ArrayBuffer.empty[String]
  def printed = p.toList
  override def println(x: Any): Unit = p += x.toString
```

So this lib keeps everything printed into memory. Calling `printed` we get a `List[String]` with whatever we printed,
this includes "Hello world" of our sample app.

Now the test:

```scala
class HelloWorldPrinterTest extends AnyFunSuiteLike:
  val lib = new InMemoryPrintLnLib {}

  test("it does print hello world!") {
    new HelloWorldPrinter(lib).helloWorld()
    lib.printed should be(Seq("Hello World!"))
  }
```

Simple! 

## A more complicated and realistic example. Guess-the-number.

Ok testing `println` may have been a nightmare but the code under test was very simple. To see a more realistic app
with side effects, we can see this guess-the-number app. It will create a random number and then print a prompt and wait
for the user to enter a number into stdin. It will then match it with the random number, print a msg if the guess was
wrong and repeat.

Here it is, it could be a bit more functional but will do for the purposes of this example:

```scala
class GuessNumber:
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
    if n == r then "You found it!"
    else if n > r then s"the number is smaller than $n"
    else s"the number is greater than $n"
```

Testing that as is will be a nightmare! But lets apply the same methodology. Lets create libs for all side effects. We already
have the lib for println, we need a lib for Random and one for reading from stdin.

ReadLineLib:

```scala
import scala.io.StdIn

trait ReadLineLib:
  def readLine(): String

trait StdReadLineLib extends ReadLineLib:
  override def readLine(): String = StdIn.readLine()
```

RandomLib:

```scala
trait RandomLib:
  def Random: ScalaRandom

trait ScalaRandom:
  def nextInt(n: Int): Int

trait StdRandomLib extends RandomLib:
  override val Random = new StdScalaRandom

private class StdScalaRandom extends Random with ScalaRandom
```

### guess-the-number, testable this time
So now we can write our code which will be easily testable. Actually the code is almost exactly the same as before.
I've included the `main()` method to demonstrate how to create the lib. But `GuessNumber` class is almost identical
as before.

```scala
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
```

### Testing guess-the-number

So now it is time for some tests. We can either mock the libs again or just write some quick libs to use during testing.
For this example I wrote some testing libs:

```scala
trait MockRandomLib(intVal: Int) extends RandomLib:
  override val Random = new ScalaRandom:
    override def nextInt(n: Int) = intVal

trait InMemoryReadLineLib(lines: Seq[String]) extends ReadLineLib:
    private var l = 0
    override def readLine(): String =
        val s = lines(l)
        l += 1
        s
```

and the actual test itself:

```scala
class GuessNumberTest extends AnyFunSuiteLike:
  val PleaseGuessPrompt = "Please guess a number between 1 and 100"

    class App(randomNumber: Int, stdIn: String*):
        val lib = new InMemoryPrintLnLib with MockRandomLib(randomNumber) with InMemoryReadLineLib(stdIn)
        val app = new GuessNumber(lib)

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
```

# Conclusion (for now)

We took untestable code with side effects and managed to keep the code as simple as it was but made it testable.
Full code is in this repo for all the examples above.

In the future we will see how we can work with:
- Side effects that have temporary lifecycle like InputStreams where we need to open/close it multiple times during the lifetime of our app.
- Side effects that have app-wide lifecycle like database connections