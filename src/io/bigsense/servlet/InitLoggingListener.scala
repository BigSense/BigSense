package io.bigsense.servlet

import javax.servlet.ServletContextListener
import javax.servlet.ServletContextEvent
import java.util.Properties
import org.apache.log4j.PropertyConfigurator

class InitLoggingListener extends ServletContextListener {

  def contextInitialized(event: ServletContextEvent): Unit = {
    var props = new Properties();
    props.load(this.getClass().getResourceAsStream("/io/bigsense/spring/log4j.properties"))
    PropertyConfigurator.configure(props)    
  }

  def contextDestroyed(event: ServletContextEvent): Unit = {}

}
