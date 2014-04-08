/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.model

class SensorModel extends ModelTrait {

  var uniqueId : String = _
  var stype : String = _
  var units : String = _
  var data : String = _
  var timestamp : String = _
  
  override def toString(): String = "[SensorID: %s, Type: %s, Units: %s, Data: %s, Timestamp: %s]".format(uniqueId,stype,units,data,timestamp)
  
  override def clone() : AnyRef = {
    val clone = new SensorModel()
    clone.uniqueId = uniqueId
    clone.stype = stype
    clone.units = units
    clone.data = data
    clone.timestamp = timestamp
    clone
  }
  
}