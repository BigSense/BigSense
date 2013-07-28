/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.format
import io.bigsense.model.ModelTrait

trait FormatTrait {

  def renderModels(model : List[ModelTrait]) : String
  def loadModels(data: String) : List[ModelTrait]
  
}