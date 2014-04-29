package io.bigsense.server

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.handler.HandlerCollection
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.webapp.WebAppContext
import io.bigsense.servlet.{DBUpdateListener, InitLoggingListener, MasterServlet}


class JettyServer extends ServerTrait {

  val server = new Server()
  val connector = new ServerConnector(server)
  connector.setPort(httpPort)
  server.setConnectors(Array(connector))

  val context = new ServletContextHandler()
  context.setContextPath(BigSenseServer.webRoot)
  context.addServlet(new MasterServlet().getClass, "/*")
  context.addEventListener(new InitLoggingListener())
  context.addEventListener(new DBUpdateListener())

  val fileContext = new WebAppContext()
  fileContext.setContextPath(BigSenseServer.contentRoot)
  fileContext.setResourceBase(BigSenseServer.getClass.getResource("/io/bigsense/web").toExternalForm)

  val handlers = new HandlerCollection()
  handlers.setHandlers(Array( fileContext, context, new DefaultHandler()))
  server.setHandler(handlers)

  override def startServer() {
    server.start
    server.join
  }

  override def stopServer() {
    server.stop
  }
}