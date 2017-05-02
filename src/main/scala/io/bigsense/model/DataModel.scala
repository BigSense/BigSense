/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.model

case class DataModel(timestamp : String, id : String, sensors : List[SensorModel], gps : Option[GPSModel]) extends ModelTrait