name := "cqrs"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.typelevel"  %% "cats"           % "0.9.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

addCommandAlias("c", "compile")

// Tests
addCommandAlias("tc", "test:compile")
addCommandAlias("t", "test")
addCommandAlias("to", "testOnly")
