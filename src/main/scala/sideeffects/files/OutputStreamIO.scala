package sideeffects.files

import sideeffects.closables.Closable

import java.io.{InputStream, OutputStream}

class OutputStreamIO private[files] (outputStreamCreator: () => OutputStream) extends Closable(outputStreamCreator, _.close())
