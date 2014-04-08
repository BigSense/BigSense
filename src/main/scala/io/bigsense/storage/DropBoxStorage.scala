package io.bigsense.storage
import com.dropbox.client2.DropboxAPI
import com.dropbox.client2.session.AppKeyPair

class DropBoxStorage extends StorageTrait {

  
  def storeFile(packageId : String, sensorId : String, refNo : Int, data : Array[Byte]) = {
    
  }
  
  def test() = {
    
    //var db : DropboxAPI = new DropBoxAPI();
    var keys : AppKeyPair = new AppKeyPair("","")
  }
  

}