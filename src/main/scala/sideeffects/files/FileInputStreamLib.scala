package sideeffects.files

import java.io.{ByteArrayInputStream, FileInputStream}

trait FileInputStreamLib:
  def fileInputStream(fileName: String): InputStreamIO

trait StdFileInputStreamLib extends FileInputStreamLib:
  def fileInputStream(fileName: String) = new InputStreamIO(() => new FileInputStream(fileName))
