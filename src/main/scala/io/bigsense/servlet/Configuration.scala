package io.bigsense.servlet

import org.apache.log4j.Logger
import org.rogach.scallop.{ScallopOption, ScallopConf}
import scala.collection.JavaConverters._
import io.bigsense.spring.BigSensePropertyLocation

/**
 * Created by sumit on 4/22/14.
 */
class Configuration(args : Array[String]) {

  val log : Logger = Logger.getLogger(JettyServer.getClass)

  val requiredProperties = List("dbms","connectionString","environment",
    "dbDriver","dbUser","dbPass","dboUser","dboPass")

  class Conf(args : Array[String]) extends ScallopConf(args) {
    version("BigSense X.X")
    banner("""Usage: bigsense [-c|--config <file>] [-l|--list-config]
             |
             |BigSense is web service for storing and retrieving sensor network data.
             |
             |Options:
             |""".stripMargin)
    footer("\nGNU GPL v3 :: http://bigsense.io")
    val configFile:ScallopOption[String] = opt[String]("config",descr="BigSense Configuration Property File",required = true, argName="file")
    val listConfig:ScallopOption[Boolean] = opt[Boolean]("list-config",descr="Prints current configuration and exists")
  }

  /**
   * scallop configuration object representing command line arguments
   */
  lazy val params = new Conf(args)

  /**
   * property file configuration options
   */
  lazy val options : Map[String,String]  = {

    def err(msg : String, exit : Int) = {
      log.fatal(msg)
      System.exit(exit)
      Map[String,String]() //makes the compiler happy
    }

    try{
      val p = new BigSensePropertyLocation().properties
      requiredProperties.foreach( req => {
        if(p.getProperty(req) == null) { throw new Exception("Required property %s is missing".format(req)) }
      })
      p.getProperty("httpPort").toInt
      p.asScala.toMap
    }
    catch {
      case e:NumberFormatException => err("httpPort must be an integer",3)
      case e:Exception => err("Error loading configuration %s".format(e.getMessage),2)
    }
  }



}
