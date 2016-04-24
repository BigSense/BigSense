/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.model

case class SensorModel(  uniqueId : String , stype : String, units : String, data : String, timestamp : String) extends ModelTrait {

  override def toString(): String = "[SensorID: %s, Type: %s, Units: %s, Data: %s, Timestamp: %s]".format(uniqueId,stype,units,data,timestamp)
  override def clone() = new SensorModel(uniqueId, stype, units, data, timestamp)
}