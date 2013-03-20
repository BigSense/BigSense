package org.bigsense.action
import javax.servlet.http.HttpServletResponse
import java.text.SimpleDateFormat
import org.bigsense.util.TimeHelper
import org.bigsense.db._

class AggregateAction extends ActionTrait {

    def runAction(aReq: ActionRequest): ActionResponse = { 
      var resp = new ActionResponse()
     
      aReq.method match {
        case "GET" => {
          
          var start : String = ""
          var end : String = ""
	      aReq.args(2) match {
		    case "TimestampRange" => {		        		
		        start = TimeHelper.timestampToDate(aReq.args(3))
		        end = TimeHelper.timestampToDate(aReq.args(4))
		    }
		    case "DateRange" => {		      
		      //Convert Timezone if nescessary
		      start = TimeHelper.convertDateArgument(aReq.args(3),aReq.parameters)
		      end = TimeHelper.convertDateArgument(aReq.args(4),aReq.parameters)
		    }
          }

	      resp.output = aReq.format.renderModels( dbHandler.aggregate(start,end,aReq.args(5),{
	        aReq.args(1) match {
		      case "SumVolume" => AggVolumeOverTime
		      case "AvgTemp" => AggAverageTemperature
		      case "AvgFlow" => AggAverageFlow
	        }
	      },aReq.parameters) )
	      resp.status = HttpServletResponse.SC_OK
      }
    }    
    resp
  }
  
}