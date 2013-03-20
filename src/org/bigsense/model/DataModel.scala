/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package org.bigsense.model
import scala.collection.mutable.ListBuffer

class DataModel extends ModelTrait {

  var timestamp : String = _
  var uniqueId : String = _
  var sensors : List[SensorModel] = List() 
  var processed : ListBuffer[SensorModel] = new ListBuffer()
  var errors : ListBuffer[String] = new ListBuffer()
  
  override def toString() : String = "[Relay ID: %s, Timestamp: %s, Sensors: %s]".format(
      uniqueId,timestamp,sensors)
  
}