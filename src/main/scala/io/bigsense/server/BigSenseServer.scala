package io.bigsense.server

import java.net.InetAddress
import java.security.Security

import io.bigsense.spring.{MySpring, BigSensePropertyLocation}
import io.bigsense.util.BulkBZip2DataLoader
import io.bigsense.servlet.DBUpdateListener
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.slf4j.LoggerFactory

/**
 * Created by sumit on 4/28/14.
 */
object BigSenseServer extends App {

  val log = LoggerFactory.getLogger(BigSenseServer.getClass)

  Security.addProvider(new BouncyCastleProvider())

  lazy val config = new Configuration(args)

  lazy val webRoot = config.options("webRoot")

  lazy val contentRoot = config.options("contentRoot")

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

  if(config.params.key.isSupplied) {

    val relayName : String = config.params.relayName.get match {
      case Some(rn : String) => rn
      case None => {
        log.error("RelayName must be specified for -k|--key operations")
        System.exit(14)
        "" //makes compiler happy
      }
    }

    val cmd = config.params.key.get.getOrElse("")

    val lines = cmd match {
      case "import" => scala.io.Source.stdin.getLines.mkString("\n")
      case _ => ""
    }

    MySpring.commandLineSignatureManager.runCommand(cmd,relayName,lines,config.params.forceKey.get.getOrElse(false))
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
        log.error("Unknown server type: %s. (Was expecting tomcat or jetty)")
        System.exit(4)
      }
    }
  }
  catch {
    case e:Exception => {
      log.error("Unexpected error",e)
      System.exit(5)
    }
  }

}
