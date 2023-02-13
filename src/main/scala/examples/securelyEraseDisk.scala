package examples

import sideeffects.console.{PrintLnLib, StdPrintLnLib}
import sideeffects.files.*
import sideeffects.util.{RandomLib, StdRandomLib}

import java.io.OutputStream
import scala.annotation.tailrec

/** Warning: don't run this multiple times on SSD disks, it will shorten the lifespan of the disk.
  */
@main
def securelyEraseDisk() =
  val lib = new StdFileOutputStreamLib with StdPrintLnLib with StdRandomLib with StdFileLib
  new SecurelyEraseDiskApp(lib).run()

class SecurelyEraseDiskApp(lib: FileOutputStreamLib with PrintLnLib with RandomLib with FileLib):
  import lib.*
  def run() =
    val cwd = file(".")
    println(s"Will securely erase deleted files for the disk at ${cwd.getAbsolutePath}. Warning: the disk will be filled up fully. Please close all programs.")
    val outputFile = file("securely.erase")
    fileOutputStream(outputFile).foreach(out => writeTillDiskFull(out, cwd))
    println("Done. Cleaning up.")
    outputFile.delete()

  private val data = (1 to 32768).map(_ => Random.nextInt(255).toByte).toArray
  private val HundredMB = 100 * 1024 * 1024
  @tailrec
  private def writeTillDiskFull(out: OutputStream, cwd: File): Unit =
    out.write(data)
    if cwd.getFreeSpace > HundredMB then writeTillDiskFull(out, cwd)
