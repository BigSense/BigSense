package io.bigsense.action

import javax.servlet.http.HttpServletResponse
import play.twirl.api.Html


/**
 * Created by sumit on 4/11/14.
 */

sealed trait Response {
  val status: Int = HttpServletResponse.SC_OK
  val contentType: Option[String] = None
  val newLocations: List[Int] = List()
}

case class BinaryResponse(
  output : Array[Byte],
  override val status : Int = HttpServletResponse.SC_OK,
  override val contentType : Option[String] = Some("application/octet-stream"),
  override val newLocations: List[Int] = List())
  extends Response

case class StringResponse(
  output : String,
  override val status : Int = HttpServletResponse.SC_OK,
  override val contentType : Option[String] = Some("text/plain"),
  override val newLocations: List[Int] = List())
  extends Response

case class ViewResponse(
  view : Html,
  override val status : Int = HttpServletResponse.SC_OK,
  override val contentType : Option[String] = Some("text/html"),
  override val newLocations: List[Int] = List())
  extends Response