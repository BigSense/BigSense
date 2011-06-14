package org.penguindreams.greenstation.action {

  import scala.collection.Map
  
	trait ActionTrait {
	
	  def runAction(method: String, args : Array[String], parameters : Map[String,Array[String]]) : ActionResponse
	  
	}
}