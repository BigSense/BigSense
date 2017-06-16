package io.bigsense.validation
import io.bigsense.action.ActionRequest
import io.bigsense.format.NoFormat

class StatusActionValidator extends ValidatorTrait {
  
  def validateRequest(aReq: ActionRequest) : Option[ValidationError] = {
	    aReq.method match {
	      case "GET" =>
	        if(!aReq.format.isInstanceOf[NoFormat]) {
            Some(ValidationError("Status Does Not Support a Format",BAD_REQUEST))
	        }
          else {
            aReq.parameters.flatMap {
              case (key: String, list: Array[Any]) =>
                key match {
                  // _ is used by jQuery to avoid caching
                  case "threshold" | "refresh" | "_" =>
                    list.flatMap {
                      x: Any => {
                        if (key != "_" && !checkInt(x)) {
                          Some(ValidationError("Parameters Must be Integers", BAD_REQUEST))
                        }
                        else None
                      }
                    }
                  case _ => Some(ValidationError("Invalid parameter %s".format(key), BAD_REQUEST))
                }
            }.toList.headOption.orElse {
              if ((aReq.args.length == 2 && aReq.args(1) != "ajaxStatusTable") || aReq.args.length > 2) {
                Some(ValidationError("Invalid argument(s)", BAD_REQUEST))
              }
              else None
            }
          }
	      case _ =>
          Some(ValidationError("%s Not Implemented".format(aReq.method),METHOD_NOT_ALLOWED))
      }
  }
}