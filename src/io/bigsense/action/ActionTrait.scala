/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.action {

import scala.collection.Map
import io.bigsense.format.{FormatTrait => Format}
import io.bigsense.model.DataModel
import io.bigsense.db.ServiceDataHandlerTrait
import scala.reflect.BeanProperty
import io.bigsense.db.DataHandlerTrait
import io.bigsense.validation.ValidatorTrait
  
	trait ActionTrait {
	  
  	  @BeanProperty var dbHandler : ServiceDataHandlerTrait = _
  	  
  	  @BeanProperty var validator : ValidatorTrait = _
  
	  def runAction(aReq : ActionRequest) : ActionResponse	  

	}
}