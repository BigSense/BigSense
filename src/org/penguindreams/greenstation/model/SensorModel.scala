package org.penguindreams.greenstation.model

class SensorModel {

  var uniqueId : String = null
  var stype : String = null
  var units : String = null
  var data : String = null
  
  override def toString(): String = "SensorID: %s\nType: %s\nUnits: %s\nData: %s".format(uniqueId,stype,units,data)
  
}