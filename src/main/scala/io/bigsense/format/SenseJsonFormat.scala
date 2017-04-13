package io.bigsense.format

import io.bigsense.model._
import play.api.libs.json._

import scala.collection.immutable.List

/**
  * Created by Sumit Khanna<sumit@penguindreams.org> on 6/04/16.
  */
class SenseJsonFormat extends FormatTrait {


  private def parseJValue(in : Any) : JsValue = in match {
    case null => JsNull
    case s:String => JsString(s)
    case i:Int => JsNumber(i)
    case l:Long => JsNumber(l)
    case d:Double => JsNumber(d)
    case f:Float => JsNumber(f.toDouble)
    case b:Boolean => JsBoolean(b)
    case t:java.sql.Timestamp => JsString(t.toString)
  }

  override def renderModels(model: List[ModelTrait]): String = (model.head match {
      case relay: RelayModel => Json.parse("""{"todo":"not_implemented}""") //TODO implement -- Json.obj( "id" -> relay.id , "identifier" -> relay.identifier, "public_key" -> relay.publicKey)
      case data: DataModel => Json.parse("""{"todo":"not_implemented}""") //TODO implement -- Json.obj( "package" ->  data.uniqueId )
      case flat : FlatModel => JsArray(flat.rows.map( row => JsObject(row.flatMap { case(k,v) => Seq(k -> parseJValue(v)) }) ))
    }).toString

  def loadModels(data: String): List[ModelTrait] = {
    val json = Json.parse(data)
    throw new UnsupportedFormatException("Importing From JSON Not Supported")
  }

  override def mimeType = "application/io.bigsense.sensedata+json"
}
