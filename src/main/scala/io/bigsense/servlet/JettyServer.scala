package io.bigsense.servlet

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.handler.HandlerCollection
import org.eclipse.jetty.servlet.ServletContextHandler
import org.apache.log4j.Logger
import io.bigsense.spring.BigSensePropertyLocation
import org.eclipse.jetty.webapp.WebAppContext


object JettyServer extends App {
  //println("Netty Version: " + io.netty.util.Version.identify())

  val log : Logger = Logger.getLogger(JettyServer.getClass)

  lazy val config = new Configuration(args)

  lazy val webRoot = config.options("webRoot")

  lazy val contentRoot = config.options("contentRoot")

  if(config.params.listConfig.isSupplied) {
    new BigSensePropertyLocation().printProperties
    System.exit(0)
  }


  val server = new Server()
  val connector = new ServerConnector(server)
  connector.setPort(config.options("httpPort").toInt)
  server.setConnectors(Array(connector))

  val context = new ServletContextHandler()
  context.setContextPath(webRoot)
  context.addServlet(new MasterServlet().getClass, "/*")
  context.addEventListener(new InitLoggingListener())
  context.addEventListener(new DBUpdateListener())

  val fileContext = new WebAppContext()
  fileContext.setContextPath(contentRoot)
  fileContext.setResourceBase(JettyServer.getClass.getResource("/io/bigsense/web").toExternalForm)

  val handlers = new HandlerCollection()
  handlers.setHandlers(Array( fileContext, context, new DefaultHandler()))
  server.setHandler(handlers)
  server.start()
  server.join()
}