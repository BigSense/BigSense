package org.penguindreams.greenstation.format
import org.penguindreams.greenstation.model.ModelTrait

trait FormatTrait {

  def renderModel(model : ModelTrait)
  def loadModel(data: String) : ModelTrait
}