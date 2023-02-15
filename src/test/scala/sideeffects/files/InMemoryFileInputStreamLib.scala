package sideeffects.files

import java.io.ByteArrayInputStream

trait InMemoryFileInputStreamLib extends FileInputStreamLib:
  def fileInputStreamContent(fileName: String): Array[Byte]
  def fileInputStream(fileName: String) = new InputStreamIO(() => new ByteArrayInputStream(fileInputStreamContent(fileName)))

trait InMemoryStringContentFileStreamLib(fileToContentMap: Map[String, String]) extends InMemoryFileInputStreamLib:
  override def fileInputStreamContent(fileName: String) = fileToContentMap(fileName).getBytes("UTF-8")
