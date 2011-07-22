package org.penguindreams.greenstation.format
import org.penguindreams.greenstation.model.ModelTrait
import scala.xml._
import org.apache.log4j.Logger

class GreenOvenXMLFormat extends FormatTrait {

  def renderModel(model : ModelTrait) = {
  
  }
  
  def loadModel(data : String) : ModelTrait = { 
    
    var xml : Elem = XML.loadString(data)
    
    var log : Logger = Logger.getLogger(this.getClass())
        
    var sensors = xml \\ "Sensors"
    var timestamp = (xml \\"timestamp").text.trim()
    var timezone = (xml \\"timestamp" \ "@zone" ).text.trim()

    //for( node <- xml \ "GreenData" \ "timestamp" ) yield log.trace("each"+node.text)
    
    var id : String = null
    var stype : String = null
    var units : String = null
    var sdata : String = null

    
    for( node <- xml \\"Sensors"\"Sensor") yield {
      id = (node\"id").text.trim()
      stype = (node\"type").text.trim()
      units = (node\"units").text.trim()
      sdata = (node\"data").text.trim()
    }
    
    null
  }
  
}