package org.penguindreams.greenstation.model

class DataModel {

  var timestamp : String = null
  var timezone : String = null
  var uniqueId : String = null
  var sensors : List[SensorModel] = List() 
  
  override def toString() : String = "Relay ID: %s\nTimestamp: %s\nTimezone: %s\n Sensors:\n%s\n".format(
      uniqueId,timestamp,timezone,sensors)
  
}