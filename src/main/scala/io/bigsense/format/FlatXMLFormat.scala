package io.bigsense.format
import io.bigsense.model.ModelTrait
import io.bigsense.model.FlatModel
import io.bigsense.model.DataModel

class FlatXMLFormat extends FormatTrait {

  
  def renderModels(model : List[ModelTrait]) : String = {
    if(model.length > 0) {
      model.head match {
        case x:FlatModel => {
          return <FlatData>{
            for( m <- model ) yield { 
              <dataset>
                 {
                   var fmod = m.asInstanceOf[FlatModel]
                   for(row <- fmod.rows) yield {
                     <row>{
                       for( i <- 0 until fmod.cols.length) yield {
                    	 <x>
	                	   {row(fmod.cols(i))}
                         </x>.copy(label = fmod.headers(i))                         
                       }                 
                     }</row>
                   }
                 }
              </dataset>
            }
          }</FlatData>.toString()          
        }
        case _ => {          
          throw new UnsupportedFormatException("Model type Not Supported with FlatXMLFormat")
        }
      }
    }
    ""
  }
  
  def loadModels(data: String) : List[ModelTrait] = {
    throw new UnsupportedFormatException("Importing From FlatXMLFormat Not Supported")
  }
  
}