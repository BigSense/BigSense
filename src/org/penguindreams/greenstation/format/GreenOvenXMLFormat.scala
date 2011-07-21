package org.penguindreams.greenstation.format
import org.penguindreams.greenstation.model.ModelTrait
import scala.xml._

class GreenOvenXMLFormat extends FormatTrait {

  def renderModel(model : ModelTrait) = {
  
  }
  
  def loadModel(data : String) : ModelTrait = {
    
    var timestamp : String = null
    var xml : Elem = null
    xml = XML.loadString(data)
    
    //var timestamp
    
    /*xml match {
      case <timestamp>{ stamp @ _* }</timestamp> => {timestamp = NodeSeq.fromSeq(stamp)}
    }*/
    
    null
  }
  
}