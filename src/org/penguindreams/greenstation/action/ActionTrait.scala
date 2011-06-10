package org.penguindreams.greenstation.action {

	trait ActionTrait {
	
	  def runAction(method: String, args : Array[String], postData : String) : ActionResponse
	  
	}
}