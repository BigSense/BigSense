package io.bigsense.action

/**
 * Created by sumit on 4/11/14.
 */

class Response (
  val status : Int,
  val newLocations : List[Int],
  val contentType : Option[String] = None
)

class BinaryResponse(val output : Array[Byte], status : Int, newLocations: List[Int], contentType : Option[String])
  extends Response(status,newLocations,contentType)

class StringResponse(status : Int, newLocations: List[Int], contentType : Option[String], val output : String)
  extends Response(status,newLocations,contentType)

class ViewResponse(status : Int, newLocations: List[Int], contentType : Option[String], val view : String, val viewData : Map[String,Any])
  extends Response(status,newLocations,contentType)