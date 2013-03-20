package org.bigsense.action
import javax.servlet.http.HttpServletResponse

class RegisterAction extends ActionTrait {
	  
	  def runAction(aReq : ActionRequest): ActionResponse = {
	    
	    var resp = new ActionResponse()
	    	    
	    aReq.method match {
	      case "POST" => {
	    	  aReq.args(1) match {
	    	    case "ValidateToken" => {
	    	      
	    	    }
	    	  }
	      }
	      case "GET" => {
	    	  aReq.args(1) match {
	    	    case "RequestToken" => {
	    	      
	    	    }
	    	  }
	      }
	    }
	    resp
	  }
}