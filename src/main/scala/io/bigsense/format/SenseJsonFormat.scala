package io.bigsense.format

import io.bigsense.model.{RelayModel, SensorModel, ModelTrait}
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import scala.collection.immutable.List

/**
  * Created by cassius on 6/04/16.
  */
class SenseJsonFormat extends FormatTrait {

  implicit val modelEncoder: Encoder[ModelTrait] = Encoder.instance(a => a match {
    case s:SensorModel => s.asJson
    case r:RelayModel => r.asJson
    case _ => null
  })

  override def renderModels(model: List[ModelTrait]): String = model.asJson.noSpaces

  def loadModels(data: String): List[ModelTrait] = {
    throw new UnsupportedFormatException("Importing From JSON Not Supported")
  }

  override def mimeType = "application/io.bigsense.sensedata+json"
}
