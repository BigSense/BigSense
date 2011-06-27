package org.penguindreams.greenstation.model {

  import scala.xml._
  
	class GreenXMLModel extends ModelTrait {  
	  
	  var _xml : Elem
    
	  override def setModelData(data : String) = {
	    _xml = XML.loadString(data)
	  }
	  
	}

}