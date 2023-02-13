package sideeffects.files

import java.io

trait FileLib:
  def file(name: String): File

trait StdFileLib:
  def file(name: String) = new StdFile(name)
