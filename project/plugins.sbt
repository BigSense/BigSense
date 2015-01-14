addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0-M4")

resolvers += "spray repo" at "http://repo.spray.io"

addSbtPlugin("io.spray" % "sbt-twirl" % "0.7.0")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.2")