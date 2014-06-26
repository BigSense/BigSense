import _root_.sbt.Keys._
import com.typesafe.sbt.SbtNativePackager.packageArchetype
import com.typesafe.sbt.SbtNativePackager.Universal
import NativePackagerKeys._

name := "bigsense"

version := "0.2alpha"

scalaVersion := "2.10.3"

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
    "org.slf4j" % "slf4j-log4j12" % "1.6.1",
    "org.scalaj" %% "scalaj-collection" % "1.5",
    "bouncycastle" % "bcprov-jdk15" % "140",
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
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

//publishing

bintray.Keys.bintrayOrganization in bintray.Keys.bintray := Some("bigsense")

bintray.Keys.repository in bintray.Keys.bintray := "trusty"

seq(bintrayPublishSettings:_*)

//packaging

packageArchetype.java_server

packageDescription := "web service designed for consuming and reporting data from sensor networks"

packageSummary := "BigSense is a web service designed to process and report data from sensor networks. (http://bigsense.io)"

maintainer := "Sumit Khanna<sumit@penguindreams.org>"

fork in run := true

Twirl.settings

