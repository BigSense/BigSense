package io.bigsense.validation

import io.bigsense.action.ActionRequest

class QueryActionValidator extends ValidatorTrait {

  def validateRequest(aReq: ActionRequest): Option[ValidationError] =
    aReq.method match {
      case "GET" =>
        if (aReq.args.length >= 2) {
          aReq.args(1) match {
            case "Latest" =>
              if (aReq.args.length != 3) {
                Some(ValidationError("Latest Requires a Limit Argument", BAD_REQUEST))
              }
              else if (!checkInt(aReq.args(2))) {
                Some(ValidationError("Limit Argument Must be an Integer", BAD_REQUEST))
              }
              else None
            case "TimestampRange" =>
              if (aReq.args.length != 4) {
                Some(ValidationError("TimestampRange Requires 2 Arguments", BAD_REQUEST))
              } else if (!checkLong(aReq.args(2)) || !checkLong(aReq.args(3))) {
                Some(ValidationError("Invalid Timestamp(s)", BAD_REQUEST))
              }
              else None
            case "DateRange" =>
              if (aReq.args.length != 4) {
                Some(ValidationError("DateRange Requires Two Arguments", BAD_REQUEST))
              }
              else {
                if (!checkInt(aReq.args(2)) || !checkInt(aReq.args(3))) {
                  Some(ValidationError("Dates Must Be In The Format YYYYMMDD", BAD_REQUEST))
                }
                else if (!checkDate(aReq.args(2)) || !checkDate(aReq.args(3))) {
                  Some(ValidationError("Invalid Date(s)", BAD_REQUEST))
                }
                else None
              }
            case "Relays" =>
              if (aReq.args.length > 2) {
                Some(ValidationError("Relays Takes No Arguments", BAD_REQUEST))
              }
              else None
            case "Sensors" =>
              if (aReq.args.length > 2) {
                Some(ValidationError("Sensors Takes No Arguments", BAD_REQUEST))
              }
              else None
            case _ =>
              Some(ValidationError("Unknown command %s".format(aReq.args(1)), BAD_REQUEST))
          }
        }
        else {
           Some(ValidationError("Query Requires One Argument", BAD_REQUEST))
        }
      case _ =>
        Some(ValidationError("%s Not Implemented".format(aReq.method), METHOD_NOT_ALLOWED))
    }

}