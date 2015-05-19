name :="futureOptions"

lazy val futureOptions=(project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.2",
  "org.specs2" %% "specs2" % "2.4.17"
)

resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"

offline := true
