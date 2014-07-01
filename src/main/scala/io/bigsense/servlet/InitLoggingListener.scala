package io.bigsense.servlet

import javax.servlet.ServletContextListener
import javax.servlet.ServletContextEvent
import java.util.Properties
import org.apache.log4j.PropertyConfigurator
import java.io.FileInputStream

class InitLoggingListener extends ServletContextListener {


  def contextInitialized(event: ServletContextEvent): Unit = {
    val props = new Properties();

    //load log4j from log4jFile system property if it is set

    Option(System.getProperty("bigsense.log4jconfig")) match {
      case (Some(log4jconfig)) => props.load(new FileInputStream(log4jconfig))
      case None => props.load(this.getClass().getResourceAsStream("/log4j.defaults.properties"))
    }

    PropertyConfigurator.configure(props)    
  }

  def contextDestroyed(event: ServletContextEvent): Unit = {}

}
