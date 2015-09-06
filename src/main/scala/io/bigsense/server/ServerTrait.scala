package io.bigsense.server

/**
 * Created by sumit on 4/28/14.
 */
trait ServerTrait {

  lazy val httpPort = try {
    BigSenseServer.config.options("httpPort").toInt
  }
  catch {
    case e: NumberFormatException => Exit.invalidHttpPort
    0 //makes compiler happy
  }

  def startServer()
  def stopServer()

}
