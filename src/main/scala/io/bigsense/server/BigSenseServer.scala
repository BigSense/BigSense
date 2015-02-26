package io.bigsense.server

import java.net.InetAddress

import org.apache.log4j.Logger
import io.bigsense.spring.BigSensePropertyLocation
import io.bigsense.util.BulkBZip2DataLoader
import io.bigsense.servlet.DBUpdateListener

/**
 * Created by sumit on 4/28/14.
 */
object BigSenseServer extends App {

  val log : Logger = Logger.getLogger(BigSenseServer.getClass)

  lazy val config = new Configuration(args)

  lazy val webRoot = config.options("webRoot")

  lazy val contentRoot = config.options("contentRoot")

  new LoggingConfiguration()

  if(config.params.showDDL.isSupplied) {

    print((config.options("dbms") match {
      case "pgsql" => txt.pgsql.apply _
      case "mssql" => txt.mssql.apply _
      case "mysql" => txt.mysql.apply(InetAddress.getLocalHost().getCanonicalHostName,
                                      _:String, _:String, _:String,_:String, _:String)
    })(
        config.options("dbDatabase"),
        config.options("dboUser"),
        config.options("dboPass"),
        config.options("dbUser"),
        config.options("dbPass")
    ))

    System.exit(0)
  }

  if(config.params.bulkLoad.isSupplied) {
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
