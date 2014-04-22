package io.bigsense.servlet

import org.apache.log4j.Logger
import org.rogach.scallop.{ScallopOption, ScallopConf}
import java.util.Properties
import java.io.FileReader
import scala.collection.JavaConverters._

/**
 * Created by sumit on 4/22/14.
 */
class Configuration(args : Array[String]) {

  val log : Logger = Logger.getLogger(JettyServer.getClass)

  val requiredProperties = List("httpPort","dbms","connectionString","environment",
    "dbDriver","dbUser","dbPass","dboUser","dboPass",
    "securityManager","dbPoolMaxPerPart","dbPoolMinPerPart","dbPoolPartitions")

  class Conf(args : Array[String]) extends ScallopConf(args) {
    val configFile:ScallopOption[String] = opt[String]("config",descr="BigSense Configuration Property File",required = true)
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
      val p = new Properties()
      p.load(new FileReader(params.configFile()))
      requiredProperties.foreach( req => {
        if(p.getProperty(req) == null) { throw new Exception("Required property %s is missing".format(req)) }
      })
      p.asScala.toMap
    }
    catch {
      case e : Exception => {
        log.fatal("Error loading configuration %s".format(e.getMessage))
        System.exit(2)
        Map[String, String]() //makes the compiler happy
      }
    }
  }



}
