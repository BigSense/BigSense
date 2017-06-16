package io.bigsense.validation

import io.bigsense.action.ActionRequest

class SensorActionValidator extends ValidatorTrait {

  def validateRequest(aReq: ActionRequest): Option[ValidationError] = {
	    aReq.method match {
	      case "POST" =>
					if(aReq.args.length > 1) {
            Some(ValidationError("Invalid argument %s".format(aReq.args(2)),BAD_REQUEST))
          } else None
				case "GET" =>
					if(aReq.args.length == 2) {
            if(!checkInt(aReq.args(1))) {
              Some(ValidationError("Invalid Package ID",BAD_REQUEST))
            } else None
          }
          else {
            Some(ValidationError("GET Sensor Requires a Single Package ID",BAD_REQUEST))
          }
				case "PUT" => Some(ValidationError("Updating Sensor Data via PUT Not Allowed",METHOD_NOT_ALLOWED))
				case "DELETE" => Some(ValidationError("Deleting Sensor Data Not Allowed",METHOD_NOT_ALLOWED))
	      case _ => Some(ValidationError("Unknown Request Type",METHOD_NOT_ALLOWED))
			}
  }

}