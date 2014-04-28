package io.bigsense.server

import org.apache.catalina.startup.Tomcat
import io.bigsense.servlet.MasterServlet
import java.io.File


/**
 * Created by sumit on 4/28/14.
 */
class TomcatServer extends ServerTrait {

  val tomcat = new Tomcat()
  tomcat.setPort(BigSenseServer.config.options("httpPort").toInt)
  val tmp = new File(System.getProperty("java.io.tmpdir"))
  /*tomcat.setBaseDir(tmp)
  tomcat.getHost.setAppBase(tmp)
  tomcat.getHost.setDeployOnStartup(true)
  tomcat.getHost.setAutoDeploy(true)     */

  //can't find tomcat.8181/webapps/.
  val ctx = tomcat.addContext(BigSenseServer.webRoot,tmp.getAbsolutePath)
  Tomcat.addServlet(ctx,"bigsense",new MasterServlet())
  ctx.addServletMapping("/*","bigsense")

  //this one throws a NullPointer from within the addServlet function
  //tomcat.addServlet(BigSenseServer.webRoot, "bigsense", new MasterServlet())
  //tomcat.addWebapp(BigSenseServer.contentRoot, BigSenseServer.getClass.getResource("/io/bigsense/web").toExternalForm)


  override def startServer() = {
    tomcat.start()
    tomcat.getServer().await()
  }

  override def stopServer() = tomcat.stop
}
