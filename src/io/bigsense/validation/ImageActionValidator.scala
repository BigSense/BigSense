package io.bigsense.validation
import io.bigsense.action.ActionRequest

class ImageActionValidator extends ValidatorTrait {

    def validateRequest(aReq: ActionRequest): Option[ValidationError] = {
      None
    }
  
}