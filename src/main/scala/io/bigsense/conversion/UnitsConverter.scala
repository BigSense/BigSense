package io.bigsense.conversion
import io.bigsense.model.ModelTrait
import io.bigsense.model.RelayModel
import io.bigsense.model.FlatModel
import io.bigsense.model.DataModel
import scala.collection.mutable.ListBuffer

class UnitsConverter extends ConverterTrait {

  private def convert(units : String, data : String, direction : String) : (String,String) = {
    
    var retdata : String = units
    var retunits : String = data
    
    direction match {
      case "Standard" => {    
	    units.toLowerCase() match {
		    case "c" => {
		    	//Multiply by 9, then divide by 5, then add 32
		    	retdata = (((data.toFloat * 9) / 5) + 32).toString()
		    	retunits = "F"
		    }
		    case "mm" => {
		      retunits = "in"
		      retdata  = (data.toFloat / 25.4).toString()
		    }
		    case "mm/s" => {
		      retunits = "in/s"
		      retdata  = (data.toFloat / 25.4).toString()
		    }	    
		    case "l/s" => {
		      retunits = "gal/s"
		      retdata = (data.toFloat * 0.2641).toString()
		    }
		    case "l" => {
		      retunits = "gal"
		      retdata = (data.toFloat * 0.2641).toString()
		    }
		    case "kph" => {
		      retunits = "mph"
		      retdata = (data.toFloat * 0.6213).toString()
		    }	 
		    case "ml" => {
		      retunits = "oz"
		      retdata = (data.toFloat / 29.573).toString()
		    }
		    case "ml/s" => {
		      retunits = "oz/s"
		      retdata = (data.toFloat / 29.573).toString()
		    }
		    case _ => {}
	    }
      }
      case "Metric" => {
	    units.toLowerCase() match {
		    case "f" => {
		    	retdata = ((data.toFloat - 32) / 1.8).toString()
		    	retunits = "C"
		    }
		    case "in" => {
		      retunits = "mm"
		      retdata  = (data.toFloat * 25.4).toString()
		    }
		    case "in/s" => {
		      retunits = "mm/s"
		      retdata  = (data.toFloat * 25.4).toString()
		    }	    
		    case "gal/s" => {
		      retunits = "l/s"
		      retdata = (data.toFloat / 0.2641).toString()
		    }
		    case "gal" => {
		      retunits = "liters"
		      retdata = (data.toFloat / 0.2641).toString()
		    }
		    case "mph" => {
		      retunits = "kph"
		      retdata = (data.toFloat / 0.6213).toString()
		    }
		    case "oz" => {
		      retunits = "ml"
		      retdata = (data.toFloat * 29.573).toString()
		    }
		    case "oz/s" => {
		      retunits = "ml/s"
		      retdata = (data.toFloat * 29.573).toString()
		    }
		    case _ => {}
	    }        
      }
    }
    return (retdata,retunits)
  }

  
  def convertRow(row : scala.collection.mutable.Map[String,Any],arg: String) = {
    
  	val (u,d) = convert(row("sensor_units").toString(),row("sensor_data").toString(),arg)
  	row("sensor_units") = u
  	row("sensor_data") = d

  }
  
}