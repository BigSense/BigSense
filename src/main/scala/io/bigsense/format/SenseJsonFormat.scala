package io.bigsense.format

import io.bigsense.model.{DataModel, RelayModel, SensorModel, ModelTrait}
import play.api.libs.json._

import scala.collection.immutable.List

/**
  * Created by cassius on 6/04/16.
  */
class SenseJsonFormat extends FormatTrait {


  override def renderModels(model: List[ModelTrait]): String = {
    model.map( m =>
      m match {
        case relay : RelayModel => Json.obj( "id" -> relay.id , "identifier" -> relay.identifier, "public_key" -> relay.publicKey)
        case data : DataModel => Json.obj( "package" ->  data.uniqueId )
      }
    )
    ""
  }

  def loadModels(data: String): List[ModelTrait] = {
    val json = Json.parse(data)
    throw new UnsupportedFormatException("Importing From JSON Not Supported")
  }

  override def mimeType = "application/io.bigsense.sensedata+json"
}
