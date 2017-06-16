package io.bigsense.validation

import io.bigsense.action.ActionRequest

class AggregateActionValidator extends ValidatorTrait {

  def validateRequest(aReq: ActionRequest): Option[ValidationError] = {
    
    aReq.method match {
      case "GET" =>
        if(aReq.args.length != 6) {
          Some(ValidationError("Aggregate Queries Require Five Arguments", BAD_REQUEST))
        }
        else {
          (aReq.args(1) match {
            case "Sum" => None
            case "Average" => None
            case _ => Some(ValidationError("Invalid Aggregate Function", BAD_REQUEST))
          }).orElse {
            aReq.args(2) match {
              case "TimestampRange" =>
                if (!checkLong(aReq.args(3)) || !checkLong(aReq.args(4))) {
                  Some(ValidationError("Invalid Timestamp(s)", BAD_REQUEST))
                }
                else None
              case "DateRange" =>
                if (!checkDate(aReq.args(3)) || !checkDate(aReq.args(4))) {
                  Some(ValidationError("Invalid Date(s)", BAD_REQUEST))
                }
                else None
              case _ =>
                Some(ValidationError("Invalid Range Function", BAD_REQUEST))
            }
          }.orElse {
            if (!checkInt(aReq.args(5))) {
              Some(ValidationError("Invalid Interval", BAD_REQUEST))
            }
            else None
          }
        }
      case _ =>
        Some(ValidationError("%s Not Implemented".format(aReq.method), METHOD_NOT_ALLOWED))
    }
  }

}