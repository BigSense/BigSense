package io.bigsense.servlet

import java.io.{FileReader, File}
import java.util.Properties

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.handler.HandlerCollection
import org.eclipse.jetty.servlet.ServletContextHandler
import org.rogach.scallop.{ScallopOption, ScallopConf}
import org.apache.log4j.Logger


object JettyServer extends App {
  //println("Netty Version: " + io.netty.util.Version.identify())

  val log : Logger = Logger.getLogger(JettyServer.getClass)

  object Conf extends ScallopConf(args) {
    val configFile:ScallopOption[String] = opt[String]("config",descr="BigSense Configuration Property File",required = true)
  }

  lazy val c : Properties = {
    try{
      val p = new Properties()
      p.load(new FileReader(Conf.configFile.get.get))
      p
    }
    catch {
      case e : Exception => log.fatal("Error loading configuration %s".format(e.getMessage))
      System.exit(2)
      new Properties() //makes compiler happy
    }
  }

  val configFile = new File(Conf.configFile.get.get)
  if(configFile.canRead) {
    new Properties().load(new FileReader(configFile))
  }

  val server = new Server()
  val connector = new ServerConnector(server)
  connector.setPort(8080)
  server.setConnectors(Array(connector))
  val context = new ServletContextHandler()
  context.setContextPath("/bigsense")
  //context.addServlet(MasterServlet.class, "/api")
  val handlers = new HandlerCollection()
  handlers.setHandlers(Array( context,new DefaultHandler()))
  server.setHandler(handlers)
  server.start()
  server.join()
}