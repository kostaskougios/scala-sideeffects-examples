package sideeffects.files

trait File:
  def getName: String
  def getAbsolutePath: String
  def delete(): Boolean
  def getFreeSpace: Long

class StdFile(name: String) extends java.io.File(name) with File
