package org.penguindreams.greenstation.action {

  import scala.collection.Map
import org.penguindreams.greenstation.format.{FormatTrait => Format}
import org.penguindreams.greenstation.model.DataModel
import org.penguindreams.greenstation.db.DatabaseHandler
import scala.reflect.BeanProperty
import org.penguindreams.greenstation.db.DataHandlerTrait
  
	trait ActionTrait {
	  
  	  @BeanProperty var dbHandler : DataHandlerTrait = null
  
	  def runAction(aReq : ActionRequest) : ActionResponse	  

	}
}