package sideeffects.files

import sideeffects.closables.{Closable, Close}

import java.io.{InputStream, OutputStream}

class OutputStreamIO private[files] (outputStream: OutputStream) extends Closable(outputStream)

given Close[OutputStream] with
  override def close(o: OutputStream): Unit = o.close()
