name := "slice-and-dice"
organization := "org.matthicks"
version := "1.0.0-SNAPSHOT"

scalaVersion := "2.13.10"

fork := true

libraryDependencies ++= Seq(
  "com.outr" %% "scribe-slf4j" % "3.10.5",
  "com.outr" %% "scarango-driver" % "3.7.5",
  "com.outr" %% "spice-server-undertow" % "0.0.6",
  "com.outr" %% "spice-client-okhttp" % "0.0.6",
  "org.scalatest" %% "scalatest" % "3.2.14" % Test,
  "org.typelevel" %% "cats-effect-testing-scalatest" % "1.5.0" % Test,
)

enablePlugins(JavaServerAppPackaging)

dockerBaseImage := "azul/zulu-openjdk:19-latest"
packageSummary := "Slice & Dice"
packageDescription := "Coding Interview"
dockerExposedPorts += 8080
Docker / packageName := "matthicks/slice-and-dice"
Docker / maintainer := "matthicks"
Docker / daemonUserUid := None
Docker / daemonUser := "root"
Universal / mappings += baseDirectory.value / "deploy" / "config.json" -> "config.json"