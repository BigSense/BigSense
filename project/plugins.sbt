addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0-M4")

resolvers += "spray repo" at "http://repo.spray.io"

addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.0.4")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-aspectj" % "0.10.2")

addSbtPlugin("org.scala-sbt.plugins" % "sbt-onejar" % "0.8")

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.1")