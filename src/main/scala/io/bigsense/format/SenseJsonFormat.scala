package io.bigsense.format

import io.bigsense.model.ModelTrait
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

/**
  * Created by cassius on 6/04/16.
  */
class SenseJsonFormat extends FormatTrait {

  override def renderModels(model: List[ModelTrait]): String = model.asJson.noSpaces
  override def loadModels(data: String): List[ModelTrait] = decode[List[ModelTrait]](data) match {
    case Left(a:String) => List[ModelTrait]() //todo: validation and error checking across all formats?
    case Right(a: List[ModelTrait]) => a
  }

  override def mimeType = "application/io.bigsense.sensedata+json"
}
