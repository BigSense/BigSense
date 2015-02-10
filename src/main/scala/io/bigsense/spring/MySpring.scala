/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */


package io.bigsense.spring

import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import org.springframework.core.io.{ClassPathResource, FileSystemResource}
import io.bigsense.server.BigSenseServer


object MySpring {

  private var context : Option[ApplicationContext] = None
  
  
  def getObject(name: String) : Any = {
    context match {
      case None => context = Some(new ClassPathXmlApplicationContext("/io/bigsense/spring/spring.xml"))
      case _ => {}
    }    
    context.get.getBean(name)    
  }
    
  def dbDriver(dbms : String) = {
    dbms match {
      case "pgsql" => "org.postgresql.Driver"
      case "mssql" => "net.sourceforge.jtds.jdbc.Driver"
      case "mysql" => "com.mysql.jdbc.Driver"
    }
  }

  def dbConnectionString(dbms: String, hostname: String, database: String, port: String) = {
    dbms match {
      case "pgsql" => s"jdbc:postgresql://$hostname:$port/$database"
      case "mssql" => s"jdbc:jtds:sqlserver://$hostname:$port/$database"
      case "mysql" => s"jdbc:mysql://$hostname:$port/$database?useLegacyDatetimeCode=false&serverTimezone=UTC"
    }
  }
}

class BigSensePropertyLocation extends PropertyPlaceholderConfigurer {
  setLocations(Array(new ClassPathResource("/io/bigsense/spring/defaults.properties"),new FileSystemResource(BigSenseServer.config.params.configFile())))

  def printProperties = properties.list(System.out)

  def properties = mergeProperties()
}
