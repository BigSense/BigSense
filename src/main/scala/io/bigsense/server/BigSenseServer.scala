package io.bigsense.server

import java.net.InetAddress
import java.security.Security
import java.util.TimeZone

import io.bigsense.spring.{BigSensePropertyLocation, MySpring}
import io.bigsense.util.BulkBZip2DataLoader
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._

/**
 * Created by sumit on 4/28/14.
 */
object BigSenseServer extends App {

  val log = LoggerFactory.getLogger(BigSenseServer.getClass)

  TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

  Security.addProvider(new BouncyCastleProvider())

  lazy val config = new Configuration(args)

  config.params.verify()

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

    Exit.clean()
  }

  if(config.params.key.isSupplied) {

    val relayName : String = config.params.relayName.toOption match {
      case Some(rn : String) => rn
      case None => {
        Exit.noRelayNameForKey()
        "" //makes compiler happy
      }
    }

    val cmd = config.params.key.toOption.getOrElse("")

    val lines = cmd match {
      case "import" => scala.io.Source.stdin.getLines.mkString("\n")
      case _ => ""
    }

    MySpring.commandLineSignatureManager.runCommand(cmd,relayName,lines,config.params.forceKey.toOption.getOrElse(false))
    Exit.clean()
  }

  if(config.params.bulkLoad.isSupplied) {
    //TODO migrations in their own def?
    BulkBZip2DataLoader.load(config.params.bulkLoad(),config.params.chunkSize(),config.params.minYear.toOption)
    Exit.clean()
  }

  if(config.params.listConfig.isSupplied) {
    BigSensePropertyLocation.printProperties
    Exit.clean()
  }

  //database migrations
  try {
    Flyway.configure().
      locations(s"classpath:io/bigsense/db/ddl/${config.options("dbms")}").
      baselineOnMigrate(config.params.upgradeDb.isSupplied).
      placeholders(config.options.asJava).
      dataSource(MySpring.jdbcURL, config.options("dboUser"), config.options("dboPass")).
      load().
      migrate()
  }
  catch {
    case e:Exception => {
      e.getMessage match {
        case msg if msg.contains("non-empty schema") =>
          Exit.migrationFailure("No schema migration table detected. Are you upgrading from BigSense <= 0.3.0? Run with --upgradedb.")
        case ex =>
          Exit.migrationFailure("Unexpected Migration Failure", Option(e))
      }
    }
  }

  try {
    config.options("server") match {
      case "tomcat" => new TomcatServer().startServer()
      case "jetty" => new JettyServer().startServer()
      case _ => Exit.unknownServer(_)
    }
  }
  catch {
    case e:Exception => Exit.unexpected(e)
  }

}
