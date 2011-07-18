package org.penguindreams.greenstation.action {

  import scala.collection.Map
import org.penguindreams.greenstation.model.{ModelTrait => Model}
import org.penguindreams.greenstation.format.{FormatTrait => Format}
  
	trait ActionTrait {

	  
	  def runAction(method: String, args : Array[String], parameters : Map[String,Array[String]], model : Model, format : Format) : ActionResponse
	  

	}
}