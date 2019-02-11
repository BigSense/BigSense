enablePlugins(JavaServerAppPackaging)
enablePlugins(DebianPlugin)
enablePlugins(RpmPlugin)
enablePlugins(SbtTwirl)
enablePlugins(sbtdocker.DockerPlugin)
enablePlugins(BuildInfoPlugin)

name := "bigsense"

organization := "bigsense.io"

version := scala.sys.process.Process("git" , Seq("describe" , "--dirty")).!!.trim()

scalaVersion := "2.12.8"

//sbt-build info
lazy val root = (project in file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(
      buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
      buildInfoPackage := "io.bigsense"
  ).
  enablePlugins(SbtAspectj).
  settings(
    aspectjInputs in Aspectj += (aspectjCompiledClasses in Aspectj).value,
    products in Compile := (products in Aspectj).value,
    products in Runtime := (products in Compile).value
  )

//dependencies

resolvers += "couchbase" at "http://files.couchbase.com/maven2/"

libraryDependencies ++= Seq(
    "org.springframework" % "spring-core" % "5.1.4.RELEASE",
    "net.sourceforge.jtds" % "jtds" % "1.3.1",
    "com.jolbox" % "bonecp" % "0.8.0.RELEASE",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "commons-codec" % "commons-codec" % "1.11",
    "bouncycastle" % "bcprov-jdk16" % "140",
    "org.postgresql" % "postgresql" % "42.2.5.jre7",
    "net.postgis" % "postgis-jdbc" % "2.3.0",
    "mysql" % "mysql-connector-java" % "8.0.15",
    "org.eclipse.jetty" % "jetty-server" % "9.4.14.v20181114",
    "org.eclipse.jetty" % "jetty-servlet" % "9.4.14.v20181114",
    "org.eclipse.jetty" % "jetty-webapp" % "9.4.14.v20181114",
    "org.rogach" %% "scallop" % "3.1.5",
    "org.apache.tomcat.embed" % "tomcat-embed-core"         % "7.0.53" , // Version 9.0.16 fails to listen on socket
    "org.apache.tomcat.embed" % "tomcat-embed-logging-juli" % "7.0.53" ,
    "org.apache.commons" % "commons-compress" % "1.18",
    "org.flywaydb" % "flyway-core" % "5.2.4",
    "io.circe" %% "circe-core" % "0.11.1",
    "io.circe" %% "circe-generic" % "0.11.1",
    "io.circe" %% "circe-parser" % "0.11.1"
)

licenses := Seq("GPL-3.0" -> url("http://www.gnu.org/licenses/gpl-3.0.html"))

//packaging

packageDescription := "web service designed for consuming and reporting data from sensor networks"

packageSummary := "BigSense is a web service designed to process and report data from sensor networks. (http://bigsense.io)"

maintainer := "Sumit Khanna<sumit@penguindreams.org>"

fork in run := true

connectInput in run := true

linuxPackageMappings ++= Seq (
  packageMapping(file("conf/logback.xml") -> "/etc/bigsense/logback.xml") withPerms("0644"),
  packageMapping(file("conf/mysql.example.properties") -> "/etc/bigsense/examples/bigsense-mysql.conf") withPerms("0644"),
  packageMapping(file("conf/mssql.example.properties") -> "/etc/bigsense/examples/bigsense-mssql.conf") withPerms("0644"),
  packageMapping(file("conf/pgsql.example.properties") -> "/etc/bigsense/examples/bigsense-pgsql.conf") withPerms("0644")
)

//RPMs

rpmVendor := "BigSense.io"

name in Rpm := "BigSense"

rpmUrl := Some("http://bigsense.io")

version in Rpm := version.value.replace('-','_')

rpmRelease := "1"

rpmLicense := Some("GPL-3.0")

rpmGroup := Some("bigsense")

dockerfile in docker := {
    val appDir: File = stage.value
    val targetDir = "/app"

    new Dockerfile {
        from("openjdk:8-jre")
        entryPoint(s"$targetDir/bin/${executableScriptName.value}", "-e")
        copy(appDir, targetDir)
    }
}