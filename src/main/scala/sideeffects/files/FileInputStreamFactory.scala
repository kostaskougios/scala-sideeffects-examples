package sideeffects.files

import java.io.{ByteArrayInputStream, FileInputStream}

trait FileInputStreamFactory:
  def fileInputStream(fileName: String): InputStreamIO

trait StdFileInputStreamFactory extends FileInputStreamFactory:
  def fileInputStream(fileName: String) = new InputStreamIO(new FileInputStream(fileName))

trait InMemoryFileInputStreamFactory extends FileInputStreamFactory:
  def fileInputStreamContent(fileName: String): Array[Byte]
  def fileInputStream(fileName: String) = new InputStreamIO(new ByteArrayInputStream(fileInputStreamContent(fileName)))
