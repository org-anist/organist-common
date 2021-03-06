lazy val sharedSettings = Seq(
  organization := "org.anist",
  version := "0.2.0",
  licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT"))),
  bintrayOrganization := Some("organist"),
  scalaVersion := "2.11.12",
  crossScalaVersions := Seq("2.11.12", "2.12.6"),
  scalacOptions := Seq(
    "-target:jvm-1.8",
    "-deprecation",
    "-unchecked",
    "-feature",
    "-Xfuture"
  )
)

lazy val root = (project in file(".")).settings(skip in publish := true).aggregate(common, io, stats, text)
lazy val common = (project in file("common")).settings(sharedSettings)
lazy val io = (project in file("io")).settings(sharedSettings)
lazy val stats = (project in file("stats")).settings(sharedSettings)
lazy val text = (project in file("text")).settings(sharedSettings)
