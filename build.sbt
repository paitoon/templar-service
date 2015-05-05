name := "templar-service"

startYear := Some(2015)

description := "A high performance json rest-api for GeoIp and Grok named regex."

licenses += "GPLv2" -> url("https://www.gnu.org/licenses/gpl-2.0.html")

version := "0.1"

scalaVersion := "2.10.5"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.10"
libraryDependencies += "com.typesafe" % "config" % "1.2.1"
libraryDependencies += "io.spray" %% "spray-can" % "1.3.3"
libraryDependencies += "io.spray" %% "spray-routing" % "1.3.3"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.1"
libraryDependencies += "org.aicer.grok" % "grok" % "0.9.0"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.5.3"
libraryDependencies += "com.maxmind.geoip" % "geoip-api" % "1.2.14"
libraryDependencies += "org.rogach" %% "scallop" % "0.9.5" exclude("org.scala-lang", "scala-reflect")
libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.10.5"

mainClass in assembly := Some("templar.service.ServiceMain")

packSettings

packMain:= Map("templar-service" -> "templar.service.ServiceMain")
