package io.bigsense.action

import javax.servlet.http.HttpServletResponse

/**
 * Created by sumit on 4/11/14.
 */

class Response (
  val status : Int = HttpServletResponse.SC_OK,
  val contentType : Option[String] = None,
  val newLocations : List[Int] = List()
)

class BinaryResponse(val output : Array[Byte],
                     status : Int = HttpServletResponse.SC_OK,
                     contentType : Option[String] = Some("application/octet-stream"),
                     newLocations: List[Int] = List())
  extends Response(status,contentType,newLocations)

class StringResponse(val output : String,
                     status : Int = HttpServletResponse.SC_OK,
                     contentType : Option[String] = Some("text/plain"),
                     newLocations: List[Int] = List())
  extends Response(status,contentType,newLocations)

class ViewResponse(val view : String, val viewData : Map[String,Any],
                   status : Int = HttpServletResponse.SC_OK,
                   contentType : Option[String] = Some("text/html"),
                   newLocations: List[Int] = List())
  extends Response(status,contentType,newLocations)