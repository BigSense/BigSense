package org.bigsense.action
import org.bigsense.model.FlatModel
import scalaj.collection.Imports._
import scala.collection.JavaConversions._
import javax.servlet.http.HttpServletResponse

class StatusAction extends ActionTrait {

  private def listMapToJava(l:List[Map[String,Any]]):java.util.List[java.util.Map[String,Object]] = l.map(_.asInstanceOf[Map[String,Object]].asJava).asJava
  
  private def setParameters(aReq : ActionRequest, resp : ActionResponse) = {
    //TODO: defaults in web.xml
    for( i <- List("threshold","refresh")  ) {
    	resp.viewData(i) = if (aReq.parameters.contains(i)) aReq.parameters(i)(0) else "10"
    }    
  }
  
  def runAction(aReq: ActionRequest): ActionResponse = {
    	    
        var resp = new ActionResponse()
	    	    
	    aReq.method match {
	      case "GET" => {
	        if(aReq.args.length == 1 ) {   	  
	    	  setParameters(aReq,resp)
	    	  resp.view = Some("status")
	    	  resp.status = HttpServletResponse.SC_OK	    	  
	        }
	        else {
	          aReq.args(1) match {
	            case "ajaxStatusTable" => {	                          
	              
	              setParameters(aReq,resp)
	              
		    	  var dbStatus : FlatModel = dbHandler.sensorAliveStatus()
		    	  
		    	  val t : java.util.List[String] = dbStatus.headers	    	  
		    	  resp.viewData("StatusModelHeaders") = t
		    	  
		    	  val l : java.util.List[String] = dbStatus.cols
		    	  resp.viewData("StatusModelCols") = l
		    	  
		    	  resp.viewData("StatusModelRows") = listMapToJava(dbStatus.rows)
		    	  
		    	  resp.view = Some("ajaxStatusTable")
		    	  resp.status = HttpServletResponse.SC_OK
	            }
	          }
	        }
	      }
	    }
	    resp  
  }

}