package sideeffects.files

import org.apache.commons.io.IOUtils
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class FileInputStreamLibTest extends AnyFunSuiteLike:

  test("for loop") {
    new App("file1" -> "Hello", "file2" -> "World"):
      val r = for {
        in1 <- fileInputStream("file1")
        in2 <- fileInputStream("file2")
      } yield IOUtils.toString(in1) + "/" + IOUtils.toString(in2)
      r should be("Hello/World")

    class App(fileContents: (String, String)*) extends InMemoryStringContentFileStreamLib(fileContents.toMap)
  }
