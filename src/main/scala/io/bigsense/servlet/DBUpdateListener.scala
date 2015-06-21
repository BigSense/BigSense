package io.bigsense.servlet

import javax.servlet.ServletContextListener
import javax.servlet.ServletContextEvent
import io.bigsense.spring.MySpring
import io.bigsense.db.DBOHandlerTrait

class DBUpdateListener extends ServletContextListener {

  
  /**
   * Checks the current version of the database schema and update it if necessary.
   */
  def contextInitialized(context: ServletContextEvent) {
    MySpring.dboDataHandler.updateSchema()
  }

  def contextDestroyed(content: ServletContextEvent) {}

}