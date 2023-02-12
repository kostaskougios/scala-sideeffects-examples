package examples

import sideeffects.console.{PrintLnLib, StdPrintLnLib}
import sideeffects.files.{FileOutputStreamLib, StdFileOutputStreamLib}
import sideeffects.util.{RandomLib, StdRandomLib}

import java.io.{File, FileOutputStream, OutputStream}
import scala.annotation.tailrec

/** Warning: don't run this multiple times on SSD disks, it will sorted the lifespan of the disk.
  */
@main
def securelyEraseDisk() =
  val lib = new StdFileOutputStreamLib with StdPrintLnLib with StdRandomLib
  new SecurelyEraseDiskApp(lib).run()

class SecurelyEraseDiskApp(lib: FileOutputStreamLib with PrintLnLib with RandomLib):
  import lib.*
  def run() =
    val cwd = new File(".")
    println(s"Will securely erase deleted files for the disk at ${cwd.getAbsolutePath}. Warning: the disk will be filled up fully. Please close all programs.")
    fileOutputStream("securely.erase").foreach(out => writeTillDiskFull(out, cwd))
    println("Done. Cleaning up.")
    new File("securely.erase").delete()

  private val data = (1 to 32768).map(_ => Random.nextInt(255).toByte).toArray
  private val HundredMB = 100 * 1024 * 1024
  @tailrec
  private def writeTillDiskFull(out: OutputStream, cwd: File): Unit =
    out.write(data)
    if cwd.getFreeSpace > HundredMB then writeTillDiskFull(out, cwd)
