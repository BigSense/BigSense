package org.penguindreams.greenstation.action {

	import scala.collection.Map
import org.penguindreams.greenstation.format.FormatTrait
import org.penguindreams.greenstation.model.DataModel
  
	class SensorAction extends ActionTrait {	
	  def runAction(method: String, args: Array[String], parameters: Map[String,Array[String]], model : List[DataModel], format : FormatTrait): ActionResponse = {
	    
	    var resp = new ActionResponse()
	    
	    if(args.length > 1) {
		    if(args(1) == "data" && method == "POST") {
		      //add new XML data
		      if(method == "POST") {
		        resp.status = 201 
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
	    
	    resp
	  }
	}

}