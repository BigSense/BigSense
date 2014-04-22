package io.bigsense.servlet

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.handler.HandlerCollection
import org.eclipse.jetty.servlet.ServletContextHandler
import org.apache.log4j.Logger


object JettyServer extends App {
  //println("Netty Version: " + io.netty.util.Version.identify())

  val log : Logger = Logger.getLogger(JettyServer.getClass)

  lazy val config = new Configuration(args)


  val server = new Server()
  val connector = new ServerConnector(server)
  //TODO type check the port somewhere
  connector.setPort(config.options("httpPort").toInt)
  server.setConnectors(Array(connector))
  val context = new ServletContextHandler()
  //TODO: configurable paths
  context.setContextPath("/bigsense")
  context.addServlet(new MasterServlet().getClass, "/api/*")
  val handlers = new HandlerCollection()
  handlers.setHandlers(Array( context,new DefaultHandler()))
  server.setHandler(handlers)
  server.start()
  server.join()
}