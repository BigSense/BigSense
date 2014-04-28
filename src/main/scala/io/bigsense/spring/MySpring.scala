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
    
  
}

class BigSensePropertyLocation extends PropertyPlaceholderConfigurer {
  setLocations(Array(new ClassPathResource("/io/bigsense/spring/defaults.properties"),new FileSystemResource(BigSenseServer.config.params.configFile())))

  def printProperties = properties.list(System.out)

  def properties = mergeProperties()
}
