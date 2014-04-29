package io.bigsense.server

import org.apache.log4j.Logger
import io.bigsense.spring.BigSensePropertyLocation
import io.bigsense.util.BulkBZip2DataLoader

/**
 * Created by sumit on 4/28/14.
 */
object BigSenseServer extends App {

  val log : Logger = Logger.getLogger(BigSenseServer.getClass)

  lazy val config = new Configuration(args)

  lazy val webRoot = config.options("webRoot")

  lazy val contentRoot = config.options("contentRoot")

  if(config.params.bulkLoad.isSupplied) {
    BulkBZip2DataLoader.load(config.params.bulkLoad())
    System.exit(0)
  }

  if(config.params.listConfig.isSupplied) {
    new BigSensePropertyLocation().printProperties
    System.exit(0)
  }

  val server = config.options("server") match {
    case "tomcat" => new TomcatServer().startServer()
    case "jetty" => new JettyServer().startServer()
    case _ => {
      log.fatal("Unknown server type: %s. (Was expecting tomcat or jetty)")
      System.exit(4)
    }
  }
}
