addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.17")

resolvers += "spray repo" at "http://repo.spray.io"

addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.4.0")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

addSbtPlugin("com.lightbend.sbt" % "sbt-aspectj" % "0.11.0")

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.5.0")