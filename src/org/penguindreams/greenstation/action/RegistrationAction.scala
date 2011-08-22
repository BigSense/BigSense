package org.penguindreams.greenstation.action
import javax.servlet.http.HttpServletResponse

class RegistrationAction extends ActionTrait {

  def runAction(aReq: ActionRequest): ActionResponse = { 
    	var resp = new ActionResponse()
	    	    
	    aReq.method match {
	      case "POST" => {
	        resp.status = HttpServletResponse.SC_NOT_IMPLEMENTED
	        resp.output = "Registration Must Be Done Using PUT"
	      }
	      case "GET" => {
	        
	      }
	      case "PUT" => { 

	      }
	      case "DELETE" => {
	        resp.status = HttpServletResponse.SC_NOT_IMPLEMENTED
	        resp.output = "Deleting Registration Not Implemented"
	      }
	      case _ => {
	        resp.status = HttpServletResponse.SC_METHOD_NOT_ALLOWED
	        resp.output = "Unknown Request Type"
	      }
	    }
	    
	    resp
  }

}