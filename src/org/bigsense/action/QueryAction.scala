package org.bigsense.action 
import javax.servlet.http.HttpServletResponse
import org.bigsense.model.ModelTrait
import org.bigsense.util.TimeHelper
import java.text.SimpleDateFormat



class QueryAction extends ActionTrait {

  def runAction(aReq: ActionRequest): ActionResponse = { 
    var resp = new ActionResponse()
     
    aReq.method match {
      case "GET" => {
		  aReq.args(1) match {
		    case "Latest" => {
		      var limit : Int = aReq.args(2).toInt 
		      resp.output = aReq.format.renderModels(dbHandler.retrieveLatestSensorData(limit,aReq.parameters))
		      resp.status = HttpServletResponse.SC_OK
		    }
		    case "TimestampRange" => {
		        		
		        var start :String = TimeHelper.timestampToDate(aReq.args(2))
		        var end   :String = TimeHelper.timestampToDate(aReq.args(3))
		        
		        resp.output = aReq.format.renderModels( dbHandler.retrieveDateRange(start,end,aReq.parameters) )
		        resp.status = HttpServletResponse.SC_OK
		    }
		    case "DateRange" => {
		      
		      //Convert Timezone if nescessary
		      var start = TimeHelper.convertDateArgument(aReq.args(2),aReq.parameters)
		      var end = TimeHelper.convertDateArgument(aReq.args(3),aReq.parameters)
		      
              resp.output = aReq.format.renderModels( dbHandler.retrieveDateRange(start,end,aReq.parameters) )
		      resp.status = HttpServletResponse.SC_OK
		    }
		    case "Relays" => {
		    	resp.output = aReq.format.renderModels( dbHandler.listRelays() )
		    	resp.status = HttpServletResponse.SC_OK
		    }
		    case "Sensors" => {
		        resp.output = aReq.format.renderModels( dbHandler.listSensors() )
		        resp.status = HttpServletResponse.SC_OK
		    }
		  }
      }
    }    
    resp
  }
}
