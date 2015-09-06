package io.bigsense.server

import org.rogach.scallop.{ScallopOption, ScallopConf}
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._
import io.bigsense.spring.BigSensePropertyLocation

/**
 * Created by sumit on 4/22/14.
 */
class Configuration(args : Array[String]) {

  val log = LoggerFactory.getLogger(this.getClass)

  val requiredProperties = List("dbms","dbHostname","dbDatabase","dbPort",
    "dbUser","dbPass","dboUser","dboPass")

  class Conf(args : Array[String]) extends ScallopConf(args) {
    val name = io.bigsense.BuildInfo.name
    version(f"$name%s ${io.bigsense.BuildInfo.version}%s")
    banner(f"""Usage: $name%s [-c|--config <file>] [-l|--list-config]
             |                [-b|--bulk-load <file> (-s) <chunk size> (-y) <min year>]
             |                [-k|--key (generate|import|export) (-f) <relay_name>]
             |
             |$name%s is web service for storing and retrieving sensor network data.
             |
             |Options:
             |""".stripMargin)
    footer("\nGNU GPL v3 :: http://bigsense.io")
    val configFile:ScallopOption[String] = opt[String]("config",descr="BigSense Configuration Property File",required = true, argName="file")
    val listConfig:ScallopOption[Boolean] = opt[Boolean]("list-config",descr="Prints current configuration and exits")
    val showDDL:ScallopOption[Boolean] = opt[Boolean]("ddl", descr="Prints the initial DDL for the database and exits")
    val bulkLoad:ScallopOption[String] = opt[String]("bulk-load",descr = "Loads a Bzip2 archive of sensor.xml files and exits", argName="file")
    val chunkSize:ScallopOption[Long] = opt[Long]("block-size", short='s',descr="Number of records to load before database write for bulk loading",argName="chunk size",default=Some(10000))
    val minYear:ScallopOption[Int] = opt[Int]("min-year",short = 'y', descr="Timestamp must be greater or equal to year in bulk processing", argName="min year")
    val key:ScallopOption[String] = opt[String]("key",short = 'k', descr="Generate, Import or Export a verification key for the given relay", argName="relay_id")
    val forceKey:ScallopOption[Boolean] = opt[Boolean]("force",short ='f', descr="Force overwriting existing key for generate/import")
    val relayName = trailArg[String](name="relayName", required=false, descr="Relay name for used with -k|--key commands")
    dependsOnAny(chunkSize,List(bulkLoad))
    dependsOnAny(minYear,List(bulkLoad))
    dependsOnAny(forceKey, List(key))
    //TODO: command line constraints / maybe tests?
  }

  /**
   * scallop configuration object representing command line arguments
   */
  lazy val params = new Conf(args)

  /**
   * property file configuration options
   */
  lazy val options : Map[String,String]  = {

    try{
      val p = new BigSensePropertyLocation().properties
      requiredProperties.foreach( req => {
        if(p.getProperty(req) == null) { throw new Exception("Required property %s is missing".format(req)) }
      })
      p.asScala.toMap
    }
    catch {
      case e:Exception => {
        Exit.configError(e.getMessage)
        Map[String,String]() //makes the compiler happy
      }
    }
  }

}
