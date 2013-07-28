package io.bigsense.format
import io.bigsense.model.ModelTrait

class NoFormat extends FormatTrait {

  def renderModels(model : List[ModelTrait]) : String = { throw new UnsupportedFormatException("This action requries a format") }
  def loadModels(data: String) : List[ModelTrait] = { throw new UnsupportedFormatException("This action does not support a format") }
  
}