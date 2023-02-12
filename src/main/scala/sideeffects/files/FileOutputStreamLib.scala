package sideeffects.files

import java.io.{FileInputStream, FileOutputStream}

trait FileOutputStreamLib:
  def fileOutputStream(fileName: String): OutputStreamIO

trait StdFileOutputStreamLib extends FileOutputStreamLib:
  override def fileOutputStream(fileName: String) = new OutputStreamIO(new FileOutputStream(fileName))
