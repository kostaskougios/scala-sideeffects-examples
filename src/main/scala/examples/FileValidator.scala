package examples

import org.apache.commons.io.IOUtils
import sideeffects.console.{PrintLnLib, StdPrintLnLib}
import sideeffects.files.{InputStreamIO, StdFileInputStreamLib}

@main def fileValidator(name: String) =
  class ProdEffects extends StdPrintLnLib with StdFileInputStreamLib
  new ProdEffects:
    printProcessedFile(fileInputStream(name))

    def printProcessedFile(loader: InputStreamIO) =
      println(processFile(loader))

    def processFile(loader: InputStreamIO): Seq[String] =
      loader.mapLines(_ + " **")
