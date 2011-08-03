package org.penguindreams.greenstation.action {

	import scala.collection.Map
import org.penguindreams.greenstation.format.FormatTrait
import org.penguindreams.greenstation.model.DataModel
import scala.reflect.BeanProperty
import org.penguindreams.greenstation.db.DatabaseHandler
import org.penguindreams.greenstation.db.DatabaseHandlerTrait
import javax.servlet.http.HttpServletResponse
  
	class SensorAction extends ActionTrait {	
	  
	  def runAction(method: String, args: Array[String], parameters: Map[String,Array[String]], model : List[DataModel], format : FormatTrait): ActionResponse = {
	    
	    var resp = new ActionResponse()
	    
	    
	    method match {
	      case "POST" => {
	        if(args.length > 1) {
	          resp.status = HttpServletResponse.SC_BAD_REQUEST
	          resp.output = "Invalid argument %s".format(args(2))
	        }
	        else {
		        resp.newLocations = this.dbHandler.loadData(model)
		        resp.status = HttpServletResponse.SC_CREATED
	        }
	      }
	      case "GET" => {
	        
	      }
	      case "PUT" => { 
	        resp.status = HttpServletResponse.SC_NOT_IMPLEMENTED
	        resp.output = "Updating Sensor Data via PUT Not Implemented"
	      }
	      case "DELETE" => {
	        resp.status = HttpServletResponse.SC_NOT_IMPLEMENTED
	        resp.output = "Deleting Sensor Data Not Implemented"
	      }
	      case _ => {
	        resp.status = HttpServletResponse.SC_METHOD_NOT_ALLOWED
	        resp.output = "Unknown Request Type"
	      }
	    }
	    
	    resp
	  }
	}

}