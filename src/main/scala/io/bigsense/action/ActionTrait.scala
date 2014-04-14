/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.action {

import scala.reflect.BeanProperty
import io.bigsense.db.ServiceDataHandlerTrait
import io.bigsense.validation.ValidatorTrait
//import com.escalatesoft.subcut.inject.Injectable

trait ActionTrait  {

    @BeanProperty var dbHandler : ServiceDataHandlerTrait = _
    //val dbHandler = inject[ServiceDataHandlerTrait]
  	  
    @BeanProperty var validator : ValidatorTrait = _
    //qval validator = inject[ValidatorTrait]
  
    def runAction(aReq : ActionRequest) : Response

	}
}