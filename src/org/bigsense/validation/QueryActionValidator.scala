package org.bigsense.validation

import org.bigsense.action.ActionRequest
import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer

class QueryActionValidator extends ValidatorTrait {

  def validateRequest(aReq: ActionRequest): Option[ValidationError] = { 
    
    aReq.method match {
      case "GET" => {
    	if(aReq.args.length >= 2) {
    	  aReq.args(1) match {
    	    case "Latest" => {
    	      if(aReq.args.length != 3) {
    	        return Some(new ValidationError("Latest Requires a Limit Argument",BAD_REQUEST))
    	      }
    	      else if(!checkInt(aReq.args(2))) {
    	        return Some(new ValidationError("Limit Argument Must be an Integer",BAD_REQUEST))
    	      }
    	    }
    	    case "TimestampRange" => {
    	      if(aReq.args.length != 4) {
    	        return Some(new ValidationError("TimestampRange Requires 2 Arguments",BAD_REQUEST)) 
    	      } else {
    	        if(!checkInt(aReq.args(2)) || !checkInt(aReq.args(3))) {
    	          return Some(new ValidationError("Invalid Timestamp(s)",BAD_REQUEST))
    	        }
    	      }
    	    }
    	    case "DateRange" => {
    	      if(aReq.args.length != 4) {
    	        return Some(new ValidationError("DateRange Requires Two Arguments",BAD_REQUEST))
    	      } else {
    	    	if(!checkInt(aReq.args(2)) || !checkInt(aReq.args(3))) {
    	    	  return Some(new ValidationError("Dates Must Be In The Format YYYYMMDD",BAD_REQUEST))
    	    	}
    	    	if(!checkDate(aReq.args(2)) || !checkDate(aReq.args(3))) {
    	    	  return Some(new ValidationError("Invalid Date(s)",BAD_REQUEST))
    	    	}
    	      }
    	    }
    	    case "Relays" => {
    	    	if(aReq.args.length > 2 ) {
    	    	  return Some(new ValidationError("Relays Takes No Arguments",BAD_REQUEST))
    	    	}
    	    }
    	    case "Sensors" => {
    	    	if(aReq.args.length > 2 ) {
    	    	  return Some(new ValidationError("Sensors Takes No Arguments",BAD_REQUEST))
    	    	}
    	    }
    	    case _ => {
	    		return Some(new ValidationError("Unknown command %s".format(aReq.args(1)),BAD_REQUEST))    	      
    	    }
    	  }
    	}
    	else {
    	  return Some(new ValidationError("Query Requires One Argument",BAD_REQUEST))
    	}
      }
      case _ => {
	    return Some(new ValidationError("%s Not Implemented".format(aReq.method),METHOD_NOT_ALLOWED))
      }
    }
    None
  }

}