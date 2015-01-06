package io.bigsense.server

import org.apache.log4j.Logger
import io.bigsense.spring.BigSensePropertyLocation
import io.bigsense.util.BulkBZip2DataLoader
import io.bigsense.servlet.{DBUpdateListener, InitLoggingListener}

/**
 * Created by sumit on 4/28/14.
 */
object BigSenseServer extends App {

  val log : Logger = Logger.getLogger(BigSenseServer.getClass)

  lazy val config = new Configuration(args)

  lazy val webRoot = config.options("webRoot")

  lazy val contentRoot = config.options("contentRoot")

  if(config.params.bulkLoad.isSupplied) {
    new InitLoggingListener().contextInitialized(null)
    new DBUpdateListener().contextInitialized(null)
    BulkBZip2DataLoader.load(config.params.bulkLoad(),config.params.chunkSize(),config.params.minYear.get)
    System.exit(0)
  }

  if(config.params.listConfig.isSupplied) {
    new BigSensePropertyLocation().printProperties
    System.exit(0)
  }

  try {
    config.options("server") match {
      case "tomcat" => new TomcatServer().startServer()
      case "jetty" => new JettyServer().startServer()
      case _ => {
        log.fatal("Unknown server type: %s. (Was expecting tomcat or jetty)")
        System.exit(4)
      }
    }
  }
  catch {
    case e:Exception => {
      log.fatal("Unexpected error",e)
      System.exit(5)
    }
  }

}
