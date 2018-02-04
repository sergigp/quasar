organization := "com.sergigp.quasar"
name := "quasar"

version := "0.1"

homepage := Some(url("https://github.com/sergigp/quasar"))

bintrayPackageLabels := Seq("cqrs", "quasar")

licenses ++= Seq(("MIT", url("http://opensource.org/licenses/MIT")))
lazy val publishSettings =  Seq(
  bintrayRepository := "quasar"
)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.typelevel"  %% "cats"                        % "0.9.0",
  "ch.qos.logback" % "logback-classic"              % "1.2.3",
  "org.scalatest"  %% "scalatest"                   % "3.0.4" % Test,
  "org.scalamock"  %% "scalamock-scalatest-support" % "3.6.0" % Test
)

addCommandAlias("c", "compile")

// Tests
addCommandAlias("tc", "test:compile")
addCommandAlias("t", "test")
addCommandAlias("to", "testOnly")
