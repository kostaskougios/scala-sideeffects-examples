package sideeffects.files

trait File:
  def getName: String
  def getAbsolutePath: String
  def delete(): Boolean

class StdFile(name: String) extends java.io.File(name) with File
