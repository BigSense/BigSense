package org.penguindreams.greenstation.action {

	import scala.collection.Map
import org.penguindreams.greenstation.format.FormatTrait
import org.penguindreams.greenstation.model.ModelTrait
  
	class SensorAction extends ActionTrait {	
	  def runAction(method: String, args: Array[String], parameters: Map[String,Array[String]], model : ModelTrait, format : FormatTrait): ActionResponse = {
	    
	    if(args.length > 1) {
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
	    }
	    else {
	      
	    }

	    
	    new ActionResponse()
	    null
	  }
	}

}