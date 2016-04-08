package io.bigsense.format

import io.bigsense.model.ModelTrait

/**
  * Created by cassius on 6/04/16.
  */
class SenseJsonFormat extends FormatTrait {

  override def renderModels(model: List[ModelTrait]): String = ""
  override def loadModels(data: String): List[ModelTrait] = null

  override def mimeType = "application/io.bigsense.sensedata+json"
}
