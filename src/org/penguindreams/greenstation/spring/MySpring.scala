package org.penguindreams.greenstation.spring

import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext



object MySpring {

  private var context : ApplicationContext = null
  
  def loadApplicationContent() = { 
    if (context == null) {
      context = new ClassPathXmlApplicationContext("spring.xml"); 
    }
  }
    
  
  def getObject(name: String) : Any = {
    if(context == null) { this.loadApplicationContent(); }
    this.context.getBean(name)    
  }
    
  
}