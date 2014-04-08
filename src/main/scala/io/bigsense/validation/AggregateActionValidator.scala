package io.bigsense.validation

import io.bigsense.action.ActionRequest

class AggregateActionValidator extends ValidatorTrait {

  def validateRequest(aReq: ActionRequest): Option[ValidationError] = {
    
    aReq.method match {
      case "GET" => {
        
        if(aReq.args.length != 6) {
          return Some(new ValidationError("Aggregate Queries Require Five Arguments",BAD_REQUEST))
        }
        
        aReq.args(1) match {
	      case "SumVolume" => {}
	      case "AvgTemp" => {}
	      case "AvgFlow" => {}
	      case _ => return Some(new ValidationError("Invalid Aggregate Function",BAD_REQUEST))
        }
        
        aReq.args(2) match {
		  case "TimestampRange" => {
	        if(!checkLong(aReq.args(3)) || !checkLong(aReq.args(4))) {
	          return Some(new ValidationError("Invalid Timestamp(s)",BAD_REQUEST))
	        }		    
		  }
		  case "DateRange" => { 
	    	if(!checkDate(aReq.args(3)) || !checkDate(aReq.args(4))) {
	    	  return Some(new ValidationError("Invalid Date(s)",BAD_REQUEST))
	    	}		    
		  }
		  case _ => {
		    return Some(new ValidationError("Invalid Range Function",BAD_REQUEST))
		  }
        }
        
        if(!checkInt(aReq.args(5))) {
          return Some(new ValidationError("Invalid Interval",BAD_REQUEST))
        }
        
      }
      case _ => {
    	  return Some(new ValidationError("%s Not Implemented".format(aReq.method),METHOD_NOT_ALLOWED))
      }      		
    }    
    None
  }

}