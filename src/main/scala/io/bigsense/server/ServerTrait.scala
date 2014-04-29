package io.bigsense.server

/**
 * Created by sumit on 4/28/14.
 */
trait ServerTrait {

  lazy val httpPort = BigSenseServer.config.options("httpPort").toInt

  def startServer()
  def stopServer()

}
