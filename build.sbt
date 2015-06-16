
enablePlugins(JavaServerAppPackaging)
enablePlugins(DebianPlugin)
enablePlugins(RpmPlugin)
enablePlugins(SbtTwirl)

name := "bigsense"

version := Process("git" , Seq("describe" , "--dirty")).!!.trim() 

scalaVersion := "2.10.4"

//sbt-build info
buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)

buildInfoPackage := "io.bigsense"

//dependencies

resolvers += "couchbase" at "http://files.couchbase.com/maven2/"

libraryDependencies ++= Seq(
    "org.springframework" % "spring-beans" % "3.0.5.RELEASE",
    "org.springframework" % "spring-context" % "3.0.5.RELEASE",
    "org.springframework" % "spring-core" % "3.0.5.RELEASE",
    "org.springframework" % "spring-expression" % "3.0.5.RELEASE",
    "org.springframework" % "spring-asm" % "3.0.5.RELEASE",
    "org.springframework" % "spring-aop" % "3.0.5.RELEASE",
    "cglib" % "cglib" % "2.2.2",
    "aopalliance" % "aopalliance" % "1.0",
    "commons-logging" % "commons-logging" % "1.1.1",
    "commons-codec" % "commons-codec" % "1.6",
    "commons-io" % "commons-io" % "2.4",
    "net.sourceforge.jtds" % "jtds" % "1.2.4",
    "com.jolbox" % "bonecp" % "0.7.1.RELEASE",
    "org.slf4j" % "slf4j-log4j12" % "1.7.12",
    "org.scalaj" %% "scalaj-collection" % "1.5",
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
    "org.apache.commons" % "commons-compress" % "1.6"
)

licenses := Seq("GPL-3.0" -> url("http://www.gnu.org/licenses/gpl-3.0.html"))

//packaging

packageDescription := "web service designed for consuming and reporting data from sensor networks"

packageSummary := "BigSense is a web service designed to process and report data from sensor networks. (http://bigsense.io)"

maintainer := "Sumit Khanna<sumit@penguindreams.org>"

fork in run := true

linuxPackageMappings ++= Seq (
  packageMapping(file("conf/log4j.properties") -> "/etc/bigsense/log4j.properties") withPerms("0644"),
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


