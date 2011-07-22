package org.penguindreams.greenstation.format
import org.penguindreams.greenstation.model.ModelTrait
import scala.xml._
import org.apache.log4j.Logger

class GreenOvenXMLFormat extends FormatTrait {

  def renderModel(model : ModelTrait) = {
  
  }
  
  def loadModel(data : String) : ModelTrait = {
    
    var timestamp : String = null
    var xml : Elem = null
    xml = XML.loadString(data)
    
    var log : Logger = Logger.getLogger(this.getClass())
    /*xml match {
      case Elem(prefix, label, attribs, scope, Text(text)) => log.trace("Only text children: "+text)
    }*/
    
    var sensors = xml \\ "Sensors"
    
    log.trace("Loop")
    sensors.foreach( x => log.trace("Hi"+x))
    
    log.trace("SENSORS"+sensors)
    
    log.trace("timestamp" + timestamp)
    
    null
  }
  
}