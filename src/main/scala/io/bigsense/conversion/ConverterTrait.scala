package io.bigsense.conversion
import io.bigsense.model.ModelTrait

trait ConverterTrait {

  def convertRow(row : scala.collection.mutable.Map[String,Any],arg: String) 
  
}