import _root_.sbt.Keys._
import com.typesafe.sbt.SbtNativePackager.packageArchetype
import com.typesafe.sbt.SbtNativePackager.Universal
import NativePackagerKeys._

name := "BigSense"

version := "0.1"

scalaVersion := "2.10.3"

resolvers += "couchbase" at "http://files.couchbase.com/maven2/"

libraryDependencies ++= Seq(
   //runtime dependencies
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
    "javax.servlet" % "jstl" % "1.1.2",
    "taglibs" % "standard" % "1.1.2",
    "net.sourceforge.jtds" % "jtds" % "1.2.4",
    "com.jolbox" % "bonecp" % "0.7.1.RELEASE",
    "org.slf4j" % "slf4j-log4j12" % "1.6.1",
    "org.scalaj" % "scalaj-collection_2.9.1" % "1.2",
    "bouncycastle" % "bcprov-jdk15" % "140",
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "mysql" % "mysql-connector-java" % "5.1.27",
    //"io.netty" % "netty-all" % "4.0.14.Final",
    //"com.escalatesoft.subcut" % "subcut_2.9.2" % "2.0",
    "org.eclipse.jetty" % "jetty-server" % "9.1.4.v20140401",
    "org.eclipse.jetty" % "jetty-servlet" % "9.1.4.v20140401",
    "org.rogach" %% "scallop" % "0.9.5",
    //bulk loader only (TODO: independent configuration and build
    "org.apache.commons" % "commons-compress" % "1.6",
    //build dependencies
    "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided"
)


licenses := Seq("GNU General Public License v3" -> url("https://www.gnu.org/licenses/gpl.html"))

packageArchetype.java_server

packageDescription := "web service designed for consuming and reporting data from sensor networks"

packageSummary := "TODO"

maintainer := "Sumit Khanna<sumit@penguindreams.org>"

//Twirl.settings