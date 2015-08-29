package io.bigsense.security
import java.io.StringReader
import java.security.{KeyPair, PublicKey, Security, Signature}
import javax.xml.bind.DatatypeConverter

import io.bigsense.action.ActionRequest
import io.bigsense.model.DataModel
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMReader

class SignatureSecurityManager extends SecurityManagerTrait {

  
    def loadPublicKey(relayId : String) : PublicKey = {
      
      val pemKey = dbHandler.retrievePemForRelay(relayId)
      pemKey match {
        case None => throw new SecurityManagerException("No Key Found for Relay ID " + relayId)
        case Some(string) => {}
      }

	    val pemReader = new PEMReader(new StringReader(pemKey.get))
	    pemReader.readObject().asInstanceOf[KeyPair].getPublic()
    }
      
    def securityFilter(req : ActionRequest) : Boolean = {
      
      if( req.method.equals("POST") ) {
     
        if(req.models.length != 1) {
          throw new SecurityManagerException("Only single model POST requests are supported with signature verification")
        }
        if(!req.models(0).isInstanceOf[DataModel]) {
          throw new SecurityManagerException("Only data models are supported for verification")
        }
        
        val sg = Signature.getInstance("SHA1withRSA");
        sg.initVerify(loadPublicKey(req.models(0).uniqueId))

        req.signature match {
          case None => throw new SecurityManagerException("No Signature Found")
          case Some(sig : String) => {
            sg.update(req.data.trim().getBytes())
            return sg.verify(DatatypeConverter.parseBase64Binary(sig))
          }
        }

       }
      //no security for GET/queries yet
      true
    }
}