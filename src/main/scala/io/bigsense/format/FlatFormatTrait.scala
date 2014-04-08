package io.bigsense.format

import scala.collection.immutable.List
import io.bigsense.model.ModelTrait
import io.bigsense.model.DataModel
import io.bigsense.model.RelayModel
import io.bigsense.model.FlatModel
import scala.collection.mutable.ListBuffer

trait FlatFormatTrait extends FormatTrait {

  /**
   * overridden in subclasses to render a flat row
   */
  protected def renderRow(row :List[String]) : String = "Warning Unimplemented"
    
  protected def renderHeader(row : List[String]) : String = renderRow(row)
  
  def renderModels(model : List[ModelTrait]) : String = {

    var ret = new StringBuilder()
    
    if(model.length > 0) {
      model.head match {
        case x:DataModel => {
          ret.append( renderRow( List("TimeStamp","TimeZone","RelayID","SensorID","SensorType","Units","Data") ))
          for( m <- model) {
	          for( sen <- m.asInstanceOf[DataModel].sensors) {
	            ret.append( renderRow( List(x.timestamp,"UTC",x.uniqueId,sen.uniqueId,sen.stype,sen.units,sen.data)))
	          }            
          }
        }
        case x:FlatModel => {
          for( m <- model) {
            var cast = m.asInstanceOf[FlatModel]
            //headers
            ret.append(renderHeader(cast.headers))
            //rows
            for(row <- cast.rows) {
              var ll = new ListBuffer[String]
              for( c <- cast.cols) {
                //row mapping
                if(row(c) != null) ll.append(row(c).toString()) else ll.append("")
              }
              ret.append(renderRow(ll.toList))
            }
            //mutiple float models are seperated by double new lines
            ret.append("\n\n")
          }
        }
        case _ => {
          //TODO: Exception 400 response
          ret.append("Unknown Model Type")
        }
      }      
    }
    ret.toString()
  }

  def loadModels(data: String): List[ModelTrait] = { 
    throw new UnsupportedFormatException("Importing From FlatFormat Not Supported") 
  }

}