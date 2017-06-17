package io.bigsense.action
import javax.servlet.http.HttpServletResponse

class RegisterAction extends ActionTrait {
	  
	  def runAction(aReq : ActionRequest): Response = {
	    	    
	    aReq.method match {
	      case "POST" => {
	    	  aReq.args(1) match {
	    	    case "ValidateToken" => {
	    	      StringResponse("Not Implemented",HttpServletResponse.SC_NOT_IMPLEMENTED)
	    	    }
	    	  }
	      }
	      case "GET" => {
	    	  aReq.args(1) match {
	    	    case "RequestToken" => {
              StringResponse("Not Implemented",HttpServletResponse.SC_NOT_IMPLEMENTED)
	    	    }
	    	  }
	      }
	    }
	  }
}