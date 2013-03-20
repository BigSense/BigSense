/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package org.bigsense.action {

import scala.collection.Map
import org.bigsense.format.{FormatTrait => Format}
import org.bigsense.model.DataModel
import org.bigsense.db.ServiceDataHandlerTrait
import scala.reflect.BeanProperty
import org.bigsense.db.DataHandlerTrait
import org.bigsense.validation.ValidatorTrait
  
	trait ActionTrait {
	  
  	  @BeanProperty var dbHandler : ServiceDataHandlerTrait = _
  	  
  	  @BeanProperty var validator : ValidatorTrait = _
  
	  def runAction(aReq : ActionRequest) : ActionResponse	  

	}
}