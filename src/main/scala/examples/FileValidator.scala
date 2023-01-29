package examples

import sideeffects.console.{PrintLnLib, StdPrintLnLib}
import sideeffects.files.{InputStreamIO, StdFileInputStreamFactory}
import org.apache.commons.io.IOUtils

@main def fileValidator(name: String) =
  class ProdEffects extends StdPrintLnLib with StdFileInputStreamFactory
  new ProdEffects:
    printProcessedFile(fileInputStream(name))

    def printProcessedFile(loader: InputStreamIO) =
      println(processFile(loader))

    def processFile(loader: InputStreamIO): Seq[String] =
      loader.mapLines(_ + " **")
