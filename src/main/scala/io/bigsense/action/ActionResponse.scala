/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.action {

	class ActionResponse {	
	  var output : String = ""
	  var binary : Array[Byte] = _
	  var binaryOutput = false
	  var view : Option[String] = None
	  var status : java.lang.Integer = -1
	  var newLocations : List[Int] = List()
	  var viewData : scala.collection.mutable.Map[String,Any] = scala.collection.mutable.Map()
	  var contentType : Option[String] = None
	}

}