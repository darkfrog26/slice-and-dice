name := "slice-and-dice"
organization := "org.matthicks"
version := "1.0.0-SNAPSHOT"

scalaVersion := "2.13.10"

fork := true

libraryDependencies ++= Seq(
  "com.outr" %% "scribe-slf4j" % "3.10.5",
  "com.outr" %% "scarango-driver" % "3.7.5",
  "com.outr" %% "spice-server-undertow" % "0.0.6"
)