# A pragmatic approach to code side effects

## Everyone wrote a hello world program but no one wrote a test for it, this is why.

We'll see the simplest side effect program, one that prints "Hello world" to the console.
As the title implies we will see why it is very hard to write a test for it and a pragmatic
solution that results in as-simple-as code as the original program but code that is easy to
test.

So lets see our hello world app:

```scala
@main def veryHardToTestHelloWorld() = println("Hello World!") // now try to write a test for this!
```

This code is simple but writing a test for it is a nightmare. We may be able to redirect the console to a file 
or output stream, but overall not something we want to do. Especially when an app grows, it will be a nightmare
to write and maintain the testcase. Later on we'll see the guess-a-number app that reads from stdin and prints, 
so things would get out of hand pretty quickly when it comes to testing and the hardcoded println and readln methods.

In my opinion the problem with side effects is not how to make them functional but how to make them easily
testable. I.e. wrapping the above code in an `IO { println (...) }` doesn't help in terms of code. We may
have a pure function but the result is not something we can use straight away other than running it. And then
we got the same problem, the untestable side effect.

Testing println's may not be the most useful thing in actual commercial projects, but we can use the same
solution to test i.e. files, jdbc calls etc.

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
for real life applications.

The big benefit? It is easy to test and we can write complete tests (tests that test every bit of our app) for it. Now
compare that to the project you work daily!

It is time to test it. First lets create a lib we can use for tests:

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