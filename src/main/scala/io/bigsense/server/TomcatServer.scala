package io.bigsense.server

import org.apache.catalina.startup.Tomcat
import io.bigsense.servlet.{StaticContentServlet, DBUpdateListener, MasterServlet}
import java.io.File


/**
 * Created by sumit on 4/28/14.
 */
class TomcatServer extends ServerTrait {

  val tomcat = new Tomcat()
  tomcat.setPort(httpPort)
  val tmp = new File(System.getProperty("java.io.tmpdir"))

  val ctx = tomcat.addContext(BigSenseServer.webRoot,tmp.getAbsolutePath)
  Tomcat.addServlet(ctx,"bigsense",new MasterServlet())
  ctx.addServletMapping("/*","bigsense")

  new DBUpdateListener().contextInitialized(null)

  val cCtx = tomcat.addContext(BigSenseServer.contentRoot,tmp.getAbsolutePath)
  Tomcat.addServlet(cCtx,"static",new StaticContentServlet)
  cCtx.addServletMapping("/*","static")

  override def startServer() = {
    tomcat.start()
    tomcat.getServer().await()
  }

  override def stopServer() = tomcat.stop
}
