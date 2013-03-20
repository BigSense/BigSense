/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */


package org.bigsense.spring

import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.apache.log4j.Logger




object MySpring {

  private var context : Option[ApplicationContext] = None
  
  
  def getObject(name: String) : Any = {
    context match {
      case None => context = Some(new ClassPathXmlApplicationContext("/org/bigsense/spring/spring.xml"))
      case _ => {}
    }    
    context.get.getBean(name)    
  }
    
  
}
