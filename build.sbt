import com.typesafe.sbt.SbtAspectj._

aspectjSettings

oneJarSettings

enablePlugins(JavaServerAppPackaging)
enablePlugins(DebianPlugin)
enablePlugins(RpmPlugin)
enablePlugins(SbtTwirl)
enablePlugins(sbtdocker.DockerPlugin)

name := "bigsense"

organization := "bigsense.io"

version := Process("git" , Seq("describe" , "--dirty")).!!.trim() 

scalaVersion := "2.11.8"

//sbt-build info
buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)

buildInfoPackage := "io.bigsense"

//dependencies

resolvers += "couchbase" at "http://files.couchbase.com/maven2/"

libraryDependencies ++= Seq(
    "org.springframework" % "spring-core" % "3.0.5.RELEASE",
    "net.sourceforge.jtds" % "jtds" % "1.2.4",
    "com.jolbox" % "bonecp" % "0.8.0.RELEASE",
    "org.scalaj" %% "scalaj-collection" % "1.6",
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "commons-codec" % "commons-codec" % "1.6",
    "bouncycastle" % "bcprov-jdk15" % "140",
    "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
    "org.postgis" % "postgis-jdbc" % "1.3.3",
    "mysql" % "mysql-connector-java" % "5.1.30",
    "org.eclipse.jetty" % "jetty-server" % "9.1.4.v20140401",
    "org.eclipse.jetty" % "jetty-servlet" % "9.1.4.v20140401",
    "org.eclipse.jetty" % "jetty-webapp" % "9.1.4.v20140401",
    "org.rogach" %% "scallop" % "0.9.5",
    "org.apache.tomcat.embed" % "tomcat-embed-core"         % "7.0.53" ,
    "org.apache.tomcat.embed" % "tomcat-embed-logging-juli" % "7.0.53" ,
    "org.apache.tomcat.embed" % "tomcat-embed-jasper"       % "7.0.53" ,  //JSP (remove?)
    "org.apache.commons" % "commons-compress" % "1.6",
    "com.typesafe.play" % "play-json_2.11" % "2.5.2",
    "org.flywaydb" % "flyway-core" % "4.0.3"
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

// AspectJ

AspectjKeys.inputs in Aspectj <+= compiledClasses

products in Compile <<= products in Aspectj

products in Runtime <<= products in Compile

dockerfile in docker := {
    val appDir: File = stage.value
    val targetDir = "/app"

    new Dockerfile {
        from("java")
        entryPoint(s"$targetDir/bin/${executableScriptName.value}", "-e")
        copy(appDir, targetDir)
    }
}