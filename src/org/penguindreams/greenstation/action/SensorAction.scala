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
		        this.dbHandler.loadData(model)
		        resp.status = HttpServletResponse.SC_CREATED
	        }
	      }
	      case "GET" => {}
	      case "PUT" => {}
	      case "DELETE" => {}
	      case _ => {}
	    }
	    
	    //TODO remove
	    resp
	  }
	}

}