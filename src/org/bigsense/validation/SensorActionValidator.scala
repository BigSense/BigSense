package org.bigsense.validation

import org.bigsense.action.ActionRequest

class SensorActionValidator extends ValidatorTrait {

  def validateRequest(aReq: ActionRequest): Option[ValidationError] = {
	    aReq.method match {
	      case "POST" => {
	        if(aReq.args.length > 1) {
	          return Some(new ValidationError("Invalid argument %s".format(aReq.args(2)),BAD_REQUEST))
	        }
	      }
	      case "GET" => {
	        if(aReq.args.length == 2) {
	          if(!checkInt(aReq.args(1))) {
	            return Some(new ValidationError("Invalid Package ID",BAD_REQUEST))
	          }
	        }
	        else {
	          return Some(new ValidationError("GET Sensor Requires a Single Package ID",BAD_REQUEST))
	        }
	      }
	      case "PUT" => { 
	        return Some(new ValidationError("Updating Sensor Data via PUT Not Allowed",METHOD_NOT_ALLOWED))
	      }
	      case "DELETE" => {
	        return Some(new ValidationError("Deleting Sensor Data Not Allowed",METHOD_NOT_ALLOWED))
	      }
	      case _ => {
	        return Some(new ValidationError("Unknown Request Type",METHOD_NOT_ALLOWED))
	      }
	    }    
    None 
  }

}