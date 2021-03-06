name := "scala-camp"

version := "0.1"

scalaVersion := "2.12.8"

lazy val root = (project in file("."))
  .settings(
      libraryDependencies += "com.h2database" % "h2" % "1.4.199",
      libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.6.4",
      libraryDependencies += "com.typesafe.slick" %% "slick" % "3.3.0",
      libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.0-M1",
      libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.0-M1",
      libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.8",
      libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8",
      libraryDependencies += "org.typelevel" %% "cats" % "0.9.0",
      libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,
      libraryDependencies += "org.scalamock" %% "scalamock" % "4.1.0" % Test,
      libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % "2.6.0-M1" % Test,
      libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "10.1.8" % Test
  )
