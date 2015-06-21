/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.action {

import io.bigsense.spring.MySpring

import io.bigsense.db.ServiceDataHandlerTrait
import io.bigsense.validation.ValidatorTrait

trait ActionTrait  {

    val dbHandler : ServiceDataHandlerTrait = MySpring.serviceDataHandler

    var validator : ValidatorTrait = _
  
    def runAction(aReq : ActionRequest) : Response

	}
}