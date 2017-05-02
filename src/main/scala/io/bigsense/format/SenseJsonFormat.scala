package io.bigsense.format

import io.bigsense.model._
import rapture.json._, jsonBackends.jawn._
import formatters.compact

import scala.collection.immutable.List

/**
  * Created by Sumit Khanna<sumit@penguindreams.org> on 6/04/16.
  */
class SenseJsonFormat extends FormatTrait {


  override def renderModels(model: List[ModelTrait]): String = model match {
    case d : List[DataModel] => Json.format(Json(d))
    case _ => """{"error" : "unknown model"}"""
  }

  def loadModels(data: String): List[ModelTrait] =
    Json.parse(data).as[List[DataModel]]


  override def mimeType = "application/io.bigsense.sensedata+json"
}
