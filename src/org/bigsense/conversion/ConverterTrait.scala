package org.bigsense.conversion
import org.bigsense.model.ModelTrait

trait ConverterTrait {

  def convertRow(row : scala.collection.mutable.Map[String,Any],arg: String) 
  
}