package org.penguindreams.greenstation.action {

	import scala.collection.Map
import org.penguindreams.greenstation.format.FormatTrait
import org.penguindreams.greenstation.model.DataModel
import scala.reflect.BeanProperty
import org.penguindreams.greenstation.db.DatabaseHandler
import org.penguindreams.greenstation.db.DataHandlerTrait
import javax.servlet.http.HttpServletResponse
  
	class SensorAction extends ActionTrait {	
	  
	  def runAction(aReq : ActionRequest): ActionResponse = {
	    
	    var resp = new ActionResponse()
	    
	    
	    aReq.method match {
	      case "POST" => {
	        if(aReq.args.length > 1) {
	          resp.status = HttpServletResponse.SC_BAD_REQUEST
	          resp.output = "Invalid argument %s".format(aReq.args(2))
	        }
	        else {
		        resp.newLocations = this.dbHandler.loadData(aReq.models)
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