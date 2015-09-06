package io.bigsense.util

import java.io.{StringWriter, StringReader}
import java.security.{Key, KeyPairGenerator, KeyPair}
import io.bigsense.db.ServiceDataHandlerTrait
import io.bigsense.server.{Exit, BigSenseServer}
import io.bigsense.util.IO.using
import org.bouncycastle.jce.provider.{JCERSAPublicKey, JCEDHPublicKey, JCEDHPrivateKey}
import org.bouncycastle.openssl.{PEMWriter, PEMReader}
import org.slf4j.LoggerFactory

class CommandLineSignatureManager(val db : ServiceDataHandlerTrait) {

  val log = LoggerFactory.getLogger(CommandLineSignatureManager.super.getClass)

  def runCommand(cmd : String, relayName : String, pem : String, force : Boolean) {

    val existing = db.retrievePemForRelay(relayName)

    if( cmd != "export" && (existing.nonEmpty && !force)) {
      Exit.existingKeys(relayName, cmd)
    }

    cmd match {
      case "generate" => generateKey(relayName)
      case "import" => importPrivateKey(relayName, pem)
      case "export" => printKey(relayName)
      case _ => Exit.invalidKeyCommand(cmd)
    }
    Exit.clean()
  }

  /**
   * prints private key if available, otherwise prints the pubic key
   * @param relayName
   */
  def printKey(relayName : String): Unit = {
    keyPairForRelay(relayName) match {
      case Some(keys : KeyPair) => {
        if(keys.getPrivate != null) {
          Console.println(keyToPem(keys.getPrivate))
        }
        else if(keys.getPublic != null) {
          Console.println(keyToPem(keys.getPublic))
        }
        else Exit.pemContainsNoPublicPrivate(relayName)
      }
      case None => Exit.pemNoKeysFound(relayName)
    }
  }

  def keyPairFromPem(pemKey : String) : Option[KeyPair] = {
    using(new StringReader(pemKey)) { sReader =>
      using(new PEMReader(sReader)) { pemReader =>
        pemReader.readObject() match {
          case k : KeyPair => Some(k)
          case k : JCERSAPublicKey => Some(new KeyPair(k,null))
        }
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
    Console.println(pemOut)
  }

  def importPrivateKey(relayName : String, privatePem : String) = {
    using(new StringReader(privatePem)) { sReader =>
      using(new PEMReader(sReader)) { pemReader =>
        using(new StringWriter()) { sWriter =>
          using(new PEMWriter(sWriter)) { pemWriter =>
            pemWriter.writeObject(pemReader.readObject())
            pemWriter.flush
            db.setPemForRelay(relayName, sWriter.toString)
            log.info(s"Key successfully imported for $relayName")
            Console.println(s"Key successfully imported for $relayName")
          }
        }
      }
    }
  }
  
}