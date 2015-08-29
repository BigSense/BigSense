package io.bigsense.util

import java.io.{StringWriter, StringReader}
import java.security.{Key, KeyPairGenerator, KeyPair}
import io.bigsense.db.ServiceDataHandlerTrait
import io.bigsense.server.BigSenseServer
import io.bigsense.util.IO.using
import org.bouncycastle.openssl.{PEMWriter, PEMReader}
import org.slf4j.LoggerFactory

class CommandLineSignatureManager(val db : ServiceDataHandlerTrait) {

  val log = LoggerFactory.getLogger(CommandLineSignatureManager.super.getClass)

  def runCommand(cmd : String, relayName : String, pem : String, force : Boolean) {

    val existing = db.retrievePemForRelay(relayName)

    if( cmd != "export" && (existing.nonEmpty && !force)) {
      log.error(s"Relay $relayName has existing keys. force (-f) flag required for $cmd.")
      System.exit(12)
    }

    cmd match {
      case "generate" => generateKey(relayName)
      case "import" => importPrivateKey(relayName, pem)
      case "export" => printKey(relayName)
      case _ => {
        log.error(s"Invalid key command $cmd")
        System.exit(13)
      }

    }
    System.exit(0)
  }

  /**
   * prints private key if available, otherwise prints the pubic key
   * @param relayName
   */
  def printKey(relayName : String): Unit = {
    keyPairForRelay(relayName) match {
      case Some(keys : KeyPair) => {
        if(keys.getPrivate != null) {
          log.info(keyToPem(keys.getPrivate))
        }
        else if(keys.getPublic != null) {
          log.info(keyToPem(keys.getPublic))
        }
        else {
          log.error(s"Keys found for $relayName, but PEM does not contain a public or private key.")
          System.exit(18)
        }
      }
      case None => {
        log.error(s"No keys found for Relay $relayName")
        System.exit(17)
      }
    }
  }

  def keyPairFromPem(pemKey : String) : Option[KeyPair] = {
    using(new StringReader(pemKey)) { sReader =>
      using(new PEMReader(sReader)) { pemReader =>
        Some(pemReader.readObject().asInstanceOf[KeyPair])
      }
    }
  }

  def keyPairForRelay(relayName : String) : Option[KeyPair] = {
    db.retrievePemForRelay(relayName) match {
      case Some(s : String) => keyPairFromPem(s)
      case None => None
    }
  }

  def keyToPem(key : Key) = {
    using(new StringWriter()) { writer =>
      using(new PEMWriter(writer)) { pWriter =>
        pWriter.writeObject(key)
        pWriter.flush
        writer.toString
      }
    }
  }
  
  def generateKey(relayId : String) = {
    val keyGen = KeyPairGenerator.getInstance(BigSenseServer.config.options("keyType"))
    keyGen.initialize(BigSenseServer.config.options("keySize").toInt)
    val keys = keyGen.generateKeyPair()
    val pemOut = keyToPem(keys.getPrivate)
    db.setPemForRelay(relayId,pemOut)
    log.info(pemOut)
  }

  def importPrivateKey(relayName : String, privatePem : String) = {
    using(new StringReader(privatePem)) { sReader =>
      using(new PEMReader(sReader)) { pemReader =>
        using(new StringWriter()) { sWriter =>
          using(new PEMWriter(sWriter)) { pemWriter =>
            pemWriter.writeObject(pemReader.readObject().asInstanceOf[KeyPair].getPrivate)
            pemWriter.flush
            db.setPemForRelay(relayName, sWriter.toString)
            log.info(s"Key successfully imported for $relayName")
          }
        }
      }
    }
  }
  
}