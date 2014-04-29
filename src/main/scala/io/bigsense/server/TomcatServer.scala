package io.bigsense.server

import org.apache.catalina.startup.{ContextConfig, Tomcat}
import io.bigsense.servlet.{DBUpdateListener, InitLoggingListener, MasterServlet}
import java.io.File
import org.apache.catalina.Context
import org.apache.catalina.deploy.ApplicationListener
import org.apache.catalina.core.{StandardContext, AprLifecycleListener}
import org.apache.catalina.startup.Tomcat.FixContextListener


/**
 * Created by sumit on 4/28/14.
 */
class TomcatServer extends ServerTrait {

  val tomcat = new Tomcat()
  tomcat.setPort(BigSenseServer.config.options("httpPort").toInt)
  val tmp = new File(System.getProperty("java.io.tmpdir"))


  val ctx = tomcat.addContext(BigSenseServer.webRoot,tmp.getAbsolutePath)
  Tomcat.addServlet(ctx,"bigsense",new MasterServlet())
  ctx.addServletMapping("/*","bigsense")

  //Can't figure out how to attach them correctly, so
  // just run them manually. They don't use the event anyway
  new InitLoggingListener().contextInitialized(null)
  new DBUpdateListener().contextInitialized(null)

  println(BigSenseServer.getClass.getResource("/io/bigsense/web").toExternalForm)
  val content = tomcat.addContext(BigSenseServer.contentRoot,BigSenseServer.getClass.getResource("/io/bigsense/web").toExternalForm)


  override def startServer() = {
    tomcat.start()
    tomcat.getServer().await()
  }

  override def stopServer() = tomcat.stop
}
