import sbtbuildinfo.BuildInfoPlugin.autoImport._

name := "ultimate-build"

scalaVersion := "2.11.7"

lazy val `ultimate-build` = (project in file(".")).enablePlugins(PlayScala, BuildInfoPlugin)

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
buildInfoPackage := "eu.byjean"
