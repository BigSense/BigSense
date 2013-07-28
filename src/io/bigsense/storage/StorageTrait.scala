package io.bigsense.storage

trait StorageTrait {

  def storeFile(packageId : String, sensorId : String, refNo : Int, data : Array[Byte])
  
}