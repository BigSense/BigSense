package io.bigsense.server

import org.apache.catalina.startup.Tomcat
import io.bigsense.servlet.MasterServlet

/**
 * Created by sumit on 4/28/14.
 */
class TomcatServer extends ServerTrait {

  val tomcat = new Tomcat()
  tomcat.setPort(BigSenseServer.config.options("httpPort").toInt)
  tomcat.addServlet(BigSenseServer.webRoot, "bigsense", new MasterServlet())
  tomcat.addWebapp(BigSenseServer.contentRoot, BigSenseServer.getClass.getResource("/io/bigsense/web").toExternalForm)


  override def startServer() = {
    tomcat.start()
    tomcat.getServer().await()
  }

  override def stopServer() = tomcat.stop
}
