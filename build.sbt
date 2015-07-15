import com.typesafe.sbt.SbtGit.GitKeys._
import com.typesafe.sbt.packager.SettingsHelper._

name := "ultimate-build"

scalaVersion := "2.11.7"

lazy val `ultimate-build` = (project in file(".")).enablePlugins(PlayScala, BuildInfoPlugin, GitVersioning, GitBranchPrompt)

//build info
buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
buildInfoPackage := "eu.byjean"

//git
showCurrentGitBranch
git.useGitDescribe := true
git.baseVersion := "0.0.0"
val VersionRegex = "v([0-9]+.[0-9]+.[0-9]+)-?(.*)?".r
git.gitTagToVersionNumber := {
  case VersionRegex(v,"SNAPSHOT") => Some(s"$v-SNAPSHOT")
  case VersionRegex(v,"") => Some(v)
  case VersionRegex(v,s) => Some(s"$v-$s-SNAPSHOT")
  case v => None
}

// sbt native packager
publishTo := Some("temp" at "file:///tmp/repository")
makeDeploymentSettings(Universal, packageBin in Universal, "zip")
