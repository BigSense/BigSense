package org.penguindreams.greenstation.action {

	import scala.collection.Map
import org.penguindreams.greenstation.format.FormatTrait
import org.penguindreams.greenstation.model.DataModel
import scala.reflect.BeanProperty
import org.penguindreams.greenstation.db.DatabaseHandler
import org.penguindreams.greenstation.db.DatabaseHandlerTrait
  
	class SensorAction extends ActionTrait {	
	  
	  @BeanProperty var dbHandler : DatabaseHandlerTrait = null
	  
	  def runAction(method: String, args: Array[String], parameters: Map[String,Array[String]], model : List[DataModel], format : FormatTrait): ActionResponse = {
	    
	    var resp = new ActionResponse()
	    
	    if(args.length > 1) {
		    if(args(1) == "data" && method == "POST") {
		      //add new XML data
		      if(method == "POST") {
		        this.dbHandler.loadData(model)
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