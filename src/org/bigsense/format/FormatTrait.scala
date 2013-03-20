/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package org.bigsense.format
import org.bigsense.model.ModelTrait

trait FormatTrait {

  def renderModels(model : List[ModelTrait]) : String
  def loadModels(data: String) : List[ModelTrait]
  
}