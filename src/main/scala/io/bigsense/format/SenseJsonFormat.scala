package io.bigsense.format

import io.bigsense.model._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import scala.collection.immutable.List

/**
  * Created by Sumit Khanna<sumit@penguindreams.org> on 6/04/16.
  */
class SenseJsonFormat extends FormatTrait {


  override def renderModels(model: List[ModelTrait]): String = model match {
    case d : List[DataModel] => d.asJson.noSpaces
    case _ => """{"error" : "unknown model"}"""
  }

  def loadModels(data: String): List[ModelTrait] =
    //todo: deal with unsafe get
    decode[List[DataModel]](data).toOption.get


  override def mimeType = "application/io.bigsense.sensedata+json"
}
