package org.penguindreams.greenstation.action {

  import scala.collection.Map
import org.penguindreams.greenstation.format.{FormatTrait => Format}
import org.penguindreams.greenstation.model.DataModel
import org.penguindreams.greenstation.db.DatabaseHandler
import scala.reflect.BeanProperty
import org.penguindreams.greenstation.db.DatabaseHandlerTrait
  
	trait ActionTrait {
	  
	  def runAction(method: String, args : Array[String], parameters : Map[String,Array[String]], models : List[DataModel], format : Format) : ActionResponse
	  

	}
}