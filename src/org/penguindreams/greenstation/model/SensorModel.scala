package org.penguindreams.greenstation.model

class SensorModel {

  var uniqueId : String = null
  var stype : String = null
  var units : String = null
  var data : String = null
  
  override def toString(): String = "[SensorID: %s, Type: %s, Units: %s, Data: %s]".format(uniqueId,stype,units,data)
  
}