package org.bigsense.action
import javax.servlet.http.HttpServletResponse
import org.bigsense.util.TimeHelper

class ImageAction extends ActionTrait {

  def runAction(aReq: ActionRequest): ActionResponse = { 
    var resp = new ActionResponse()
     
    aReq.method match {
      case "GET" => {
		  aReq.args(1) match {
		    case "List" => {
			    aReq.args(2) match {
				    case "Latest" => {
				      var limit : Int = aReq.args(3).toInt 
				      resp.output = aReq.format.renderModels(dbHandler.retrieveLatestImageInfo(limit,aReq.parameters))
				      resp.status = HttpServletResponse.SC_OK
				    }
				    case "TimestampRange" => {
				        		
				        var start :String = TimeHelper.timestampToDate(aReq.args(3))
				        var end   :String = TimeHelper.timestampToDate(aReq.args(4))
				        
				        resp.output = aReq.format.renderModels( dbHandler.retrieveImageInfoRange(start,end,aReq.parameters) )
				        resp.status = HttpServletResponse.SC_OK
				    }
				    case "DateRange" => {
				      
				      //Convert Timezone if nescessary
				      var start = TimeHelper.convertDateArgument(aReq.args(3),aReq.parameters)
				      var end = TimeHelper.convertDateArgument(aReq.args(4),aReq.parameters)
				      
		             // resp.output = aReq.format.renderModels( dbHandler.retrieveDateRange(start,end,aReq.parameters) )
				      resp.status = HttpServletResponse.SC_OK
				    }
			    }//End match List types      
		    }
		    case "Pull" => {
		      val image = dbHandler.retrieveImage(aReq.args(2).toInt)
		      image match {
		        case Some(imgBytes : Array[Byte]) => {
			      resp.contentType = Some("image/jpeg")
			      resp.binaryOutput = true
			      resp.binary = imgBytes
			      resp.status = HttpServletResponse.SC_OK		          
		        }
		        case None => {
		          resp.output = "Image Not Found"
		          resp.status = HttpServletResponse.SC_NOT_FOUND
		        }
		      }
		    }		    
		  }
      }
    }
    resp
  }
}