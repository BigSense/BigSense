package org.penguindreams.greenstation.action
import org.penguindreams.greenstation.model.DataModel
import org.penguindreams.greenstation.format.FormatTrait


class ActionRequest {

  var method: String = null
  var args : Array[String] = null
  var parameters : Map[String,Array[String]] = null
  var models : List[DataModel] = null
  var format : FormatTrait = null
  
}