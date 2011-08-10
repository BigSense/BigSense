package org.penguindreams.greenstation.format
import scala.xml._
import org.apache.log4j.Logger
import org.penguindreams.greenstation.model.DataModel
import org.penguindreams.greenstation.spring.MySpring
import org.penguindreams.greenstation.model.SensorModel
import scala.collection.mutable.ListBuffer

class GreenOvenXMLFormat extends FormatTrait {

  def renderModels(model : List[DataModel]) = {      
      <GreenData>{
	      for( pack <- model) {
	        <package id={pack.uniqueId} timestamp={pack.timestamp} timezone={pack.timezone}>
        <sensors>{
	          for( sensor <- pack.sensors) {
	            <sensor id={sensor.uniqueId} type={sensor.stype} units={sensor.units}>
            <data>{sensor.data}</data></sensor>
	          }
	        }</sensors></package>
	      }
      }</GreenData>
  }

  
  def loadModels(data : String) : List[DataModel] = { 
    
    var xml : Elem = XML.loadString(data)
    
    var log : Logger = Logger.getLogger(this.getClass())
        
    var models = new ListBuffer[DataModel]
    
    for( pack <- xml \\ "package") yield {
        
    	var model = new DataModel()
        
	    var sensors = pack \ "sensors"
	    model.timestamp = (pack \"@timestamp").text.trim()
	    model.timezone = (pack \"@timezone"  ).text.trim()
	    model.uniqueId = (pack \"@id"  ).text.trim()
	       	    
	    var sbList = new ListBuffer[SensorModel]()
	    
	    for( node <- sensors \"sensor") yield {
	      var sensorData = new SensorModel()
	      sensorData.uniqueId = (node\"@id").text.trim()
	      sensorData.stype = (node\"@type").text.trim()
	      sensorData.units = (node\"@units").text.trim()
	      sensorData.data = (node\"data").text.trim()
	      sbList += sensorData
	    } 
    	model.sensors = sbList.toList
    	models += model
    } 
    models.toList
  }
  
}