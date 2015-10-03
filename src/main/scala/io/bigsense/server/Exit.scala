package io.bigsense.server

import org.slf4j.LoggerFactory

/**
 * Centralized location for all exit error codes.
 * Created by Sumit Khanna on 5/09/15.
 */
object Exit {

  private def exit(code : Int, message : String, exception : Option[Throwable] = None) {
    val log = LoggerFactory.getLogger(new Exception().getStackTrace()(2).getClassName)
    exception match {
      case Some(t : Throwable) => log.error(message, t)
      case None => log.error(message)
    }
    System.exit(code)
  }

  def clean() = System.exit(0)
  def configError(msg : String) = exit(2, "Error loading configuration %s".format(msg))
  def invalidHttpPort() = exit(3, "httpPort must be an integer")
  def unknownServer(server : String) = exit(4, s"Unknown server type: %s. (Was expecting tomcat or jetty)".format(server))
  def existingKeys(relayName : String, command : String) = exit(12, s"Relay $relayName has existing keys. force (-f) flag required for $command.")
  def invalidKeyCommand(command : String) = exit(13, s"Invalid key command $command")
  def noRelayNameForKey() = exit(14, "RelayName must be specified for -k|--key operations")
  def pemNoKeysFound(relayName: String) = exit(17, s"No keys found for Relay $relayName")
  def pemContainsNoPublicPrivate(relayName: String) = exit(18, s"Keys found for $relayName, but PEM does not contain a public or private key.")
  def unexpected(exception : Throwable) = exit(99, "Unexpected Error", Some(exception))

}
