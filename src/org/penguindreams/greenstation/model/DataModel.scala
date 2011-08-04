package org.penguindreams.greenstation.model

class DataModel {

  var timestamp : String = null
  var timezone : String = null
  var uniqueId : String = null
  var sensors : List[SensorModel] = List() 
  
  override def toString() : String = "[Relay ID: %s, Timestamp: %s, Timezone: %s, Sensors: %s]".format(
      uniqueId,timestamp,timezone,sensors)
  
}