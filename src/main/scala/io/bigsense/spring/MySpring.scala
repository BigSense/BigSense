/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */


package io.bigsense.spring

import java.io.FileInputStream
import java.util.Properties
import com.jolbox.bonecp.BoneCPDataSource
import io.bigsense.action._
import io.bigsense.conversion.{KeyPemConverter, ConverterTrait, TimezoneConverter, UnitsConverter}
import io.bigsense.db.{DBOHandler, ServiceDataHandler}
import io.bigsense.format._
import io.bigsense.security.{DisabledSecurityManager, SignatureSecurityManager}
import io.bigsense.util.{CommandLineSignatureManager, SQLProperties}
import io.bigsense.validation._
import io.bigsense.server.BigSenseServer


object MySpring {

  private val config = BigSenseServer.config.options

  // Database

  private def dataSource(username: String, password: String) = {
    val ds = new BoneCPDataSource()
    ds.setDriverClass(dbDriver(config("dbms")))
    ds.setJdbcUrl(dbConnectionString(
      config("dbms"),
      config("dbHostname"),
      config("dbDatabase"),
      config("dbPort")
    ))
    ds.setMaxConnectionsPerPartition(config("dbPoolMaxPerPart").toInt)
    ds.setMinConnectionsPerPartition(config("dbPoolMinPerPart").toInt)
    ds.setPartitionCount(config("dbPoolPartitions").toInt)
    ds.setUsername(username)
    ds.setPassword(password)
    ds
  }

  private lazy val sqlCommandProps = new SQLProperties(s"/io/bigsense/db/${config("dbms")}.commands")

  private lazy val converterMap: Map[String, ConverterTrait] = Map("Units" -> new UnitsConverter, "Timezone" -> new TimezoneConverter, "Keys" -> new KeyPemConverter)

  lazy val commandLineSignatureManager = new CommandLineSignatureManager(serviceDataHandler)

  lazy val serviceDataHandler = new ServiceDataHandler {
    ds = dataSource(config("dbUser"), config("dbPass"))
    sqlCommands = sqlCommandProps
    dbDialect = config("dbms")
    converters = converterMap
  }

  lazy val dboDataHandler = new DBOHandler {
    ds = dataSource(config("dboUser"), config("dboPass"))
    ddlResource = s"/io/bigsense/db/ddl/${config("dbms")}/*"
    sqlCommands = sqlCommandProps
    dbDialect = config("dbms")
    converters = converterMap
  }

  // Actions

  lazy val aggregateAction = new AggregateAction {validator = new AggregateActionValidator}
  lazy val imageAction = new ImageAction {validator = new ImageActionValidator}
  lazy val queryAction = new QueryAction {validator = new QueryActionValidator}
  lazy val sensorAction = new SensorAction {validator = new SensorActionValidator}
  lazy val statusAction = new StatusAction {validator = new StatusActionValidator}

  def getAction(action: String): Option[ActionTrait] = {
    action match {
      case "Aggregate" => Some(aggregateAction)
      case "Image" => Some(imageAction)
      case "Query" => Some(queryAction)
      case "Sensor" => Some(sensorAction)
      case "Status" => Some(statusAction)
      case _ => None
    }
  }

  private def dbDriver(dbms: String) = {
    dbms match {
      case "pgsql" => "org.postgis.DriverWrapper"
      case "mssql" => "net.sourceforge.jtds.jdbc.Driver"
      case "mysql" => "com.mysql.jdbc.Driver"
    }
  }

  private def dbConnectionString(dbms: String, hostname: String, database: String, port: String) = {
    dbms match {
      case "pgsql" => s"jdbc:postgresql://$hostname:$port/$database"
      case "mssql" => s"jdbc:jtds:sqlserver://$hostname:$port/$database"
      case "mysql" => s"jdbc:mysql://$hostname:$port/$database?useLegacyDatetimeCode=false&serverTimezone=UTC"
    }
  }

  // Formats

  lazy val senseXMLFormat = new SenseDataXMLFormat
  lazy val txtFormat = new TabDelimitedFormat
  lazy val csvFormat = new CSVFormat
  lazy val tableHTMLFormat = new TableHTMLFormat
  lazy val senseJsonFormat = new SenseJsonFormat

  def getFormat(format: String): Option[FormatTrait] = {
    format match {
      case "sense.xml" => Some(senseXMLFormat)
      case "txt" => Some(txtFormat)
      case "csv" => Some(csvFormat)
      case "table.html" => Some(tableHTMLFormat)
      case "sense.json" => Some(senseJsonFormat)
      case _ => None
    }
  }

  // Security

  lazy val securityManager = config("securityManager") match {
    case "Signature" => new SignatureSecurityManager {dbHandler = serviceDataHandler}
    case "Disabled" => new DisabledSecurityManager
  }
}

class BigSensePropertyLocation {

  def printProperties = properties.list(System.out)

  lazy val properties = {
    val p = new Properties()
    p.load(getClass.getResourceAsStream("/io/bigsense/spring/defaults.properties"))
    p.load(new FileInputStream(BigSenseServer.config.params.configFile()))
    p
  }

}
