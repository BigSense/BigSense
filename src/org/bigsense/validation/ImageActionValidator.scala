package org.bigsense.validation
import org.bigsense.action.ActionRequest

class ImageActionValidator extends ValidatorTrait {

    def validateRequest(aReq: ActionRequest): Option[ValidationError] = {
      None
    }
  
}