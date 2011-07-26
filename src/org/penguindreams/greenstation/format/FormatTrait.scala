package org.penguindreams.greenstation.format
import org.penguindreams.greenstation.model.DataModel

trait FormatTrait {

  def renderModels(model : List[DataModel])
  def loadModels(data: String) : List[DataModel]
}