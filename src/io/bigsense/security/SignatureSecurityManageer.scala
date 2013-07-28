package io.bigsense.security
import io.bigsense.action.ActionRequest
import java.io.BufferedInputStream
import java.security.spec.X509EncodedKeySpec
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import org.apache.log4j.Logger
import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMReader
import java.io.InputStreamReader
import java.security.KeyPair
import javax.xml.bind.DatatypeConverter
import io.bigsense.model.DataModel
import java.io.StringReader

class SignatureSecurityManageer extends SecurityManagerTrait {

  
    def loadPublicKey(relayId : String) : PublicKey = {
      
      val pemKey = dbHandler.retrievePemForRelay(relayId)
      pemKey match {
        case None => throw new SecurityManagerException("No Key Found for Relay ID " + relayId)
        case Some(string) => {}
      }
      
	  Security.addProvider(new BouncyCastleProvider());
	  val pemReader : PEMReader = new PEMReader(new StringReader(pemKey.get))
	  pemReader.readObject().asInstanceOf[KeyPair].getPublic();
      
      
    }
      
    def securityFilter(req : ActionRequest) : Boolean = {
      
      if( req.method.equals("POST") ) {
     
        if(req.models.length != 1) {
          throw new SecurityManagerException("Only single model POST requests are supported with signature verificaiton")
        }
        if(!req.models(0).isInstanceOf[DataModel]) {
          throw new SecurityManagerException("Only data models are supported for verification")
        }
        
        var sg = Signature.getInstance("SHA1withRSA");  
        sg.initVerify(loadPublicKey(req.models(0).uniqueId))
        
        req.signature match {
          case None => { throw new SecurityManagerException("No Signature Found") }
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