package org.bigsense.validation
import org.bigsense.action.ActionRequest
import org.bigsense.format.NoFormat

class StatusActionValidator extends ValidatorTrait {
  
  def validateRequest(aReq: ActionRequest) : Option[ValidationError] = {
	    aReq.method match {
	      case "GET" => {
	        
	        if(!aReq.format.isInstanceOf[NoFormat]) {
	          return Some(new ValidationError("Status Does Not Support a Format",BAD_REQUEST))
	        }

	        aReq.parameters foreach {
	            case (key,list) => {
	              key match {
	                // _ is used by jQuery to avoid caching
	                case "threshold" | "refresh" | "_" => {
	                  list.foreach {
	                    x => { 
	                      if( key != "_" && !checkInt(x)) { 
	                        return Some(new ValidationError("Parameters Must be Integers",BAD_REQUEST))  
	                      }  
	                    }
	                  }
	                }
	    	        case _ => { return Some(new ValidationError("Invalid parameter %s".format(key),BAD_REQUEST)) }
	              }
	            }	            
	        }
	        
	        if( (aReq.args.length == 2 && aReq.args(1) != "ajaxStatusTable") || aReq.args.length > 2 ) {
	          return Some(new ValidationError("Invalid argument(s)",BAD_REQUEST))
	        }
	        
	      }
	      case _ => {
	        return Some(new ValidationError("%s Not Implemented".format(aReq.method),METHOD_NOT_ALLOWED))
	      }
	    }    
    None 
  }
}