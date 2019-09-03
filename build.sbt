organization := "com.sergigp.quasar"
name := "quasar"

version := "0.5"

homepage := Some(url("https://github.com/sergigp/quasar"))

bintrayPackageLabels := Seq("cqrs", "quasar")

licenses ++= Seq(("MIT", url("http://opensource.org/licenses/MIT")))
lazy val publishSettings = Seq(
  bintrayRepository := "quasar"
)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.slf4j"     % "slf4j-api"                    % "1.7.28",
  "org.scalatest" %% "scalatest"                   % "3.0.4" % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test
)

addCommandAlias("c", "compile")
addCommandAlias("prep", ";scalastyle;test:scalastyle;scalafmtCheck;test:scalafmtCheck")

// Tests
addCommandAlias("tc", "test:compile")
addCommandAlias("t", "test")
addCommandAlias("to", "testOnly")
