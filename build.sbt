val scala3Version = "3.2.1"

val CommonsIO = "org.apache.commons" % "commons-io" % "1.3.2"
val ScalaTest = "org.scalatest" %% "scalatest" % "3.2.15" % Test
val CatsEffects = "org.typelevel" %% "cats-effect" % "3.5-6581dc4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "effects",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(CommonsIO, ScalaTest)
  )
