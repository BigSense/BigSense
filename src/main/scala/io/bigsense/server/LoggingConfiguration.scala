package io.bigsense.server

import java.util.Properties
import java.io.FileInputStream

class LoggingConfiguration  {


  val props = new Properties();

  //load log4j from log4jFile system property if it is set

  /*Option(System.getProperty("bigsense.log4jconfig")) match {
    case (Some(log4jconfig)) => props.load(new FileInputStream(log4jconfig))
    case None => props.load(this.getClass().getResourceAsStream("/log4j.defaults.properties"))
  }

  PropertyConfigurator.configure(props)
    */
}
