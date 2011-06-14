package org.penguindreams.greenstation.action {

	import scala.collection.Map
  
	class SensorAction extends ActionTrait {	
	  def runAction(method: String, args: Array[String], parameters: Map[String,Array[String]]): ActionResponse = {
	    
	    if(args(1) == "data" && method == "POST") {
	      //add new XML data
	      if(method == "POST") {
	        
	      }
	      else if (method == "GET") {
	        
	      }
	      else {
	        
	      }
	    }
	    else {
	      
	    }

	    
	    new ActionResponse()
	    null
	  }
	}

}