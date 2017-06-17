package io.bigsense.conversion

class UnitsConverter extends ConverterTrait {

  private def convert(units : String, data : String, direction : String) : (String,String) = {
    direction match {
      case "Standard" =>
				units.toLowerCase() match {
					case "c" =>
						//Multiply by 9, then divide by 5, then add 32
						((((data.toFloat * 9) / 5) + 32).toString, "F")
					case "mm" => ((data.toFloat / 25.4).toString, "in")
					case "mm/s" => ((data.toFloat / 25.4).toString, "in/s")
					case "l/s" => ((data.toFloat * 0.2641).toString, "gal/s")
					case "l" => ((data.toFloat * 0.2641).toString, "gal")
					case "kph" => ((data.toFloat * 0.6213).toString, "mph")
					case "ml" => ((data.toFloat / 29.573).toString, "oz")
					case "ml/s" => ((data.toFloat / 29.573).toString, "oz/s")
					case _ => (data, units)
				}
      case "Metric" =>
        units.toLowerCase() match {
          case "f" => (((data.toFloat - 32) / 1.8).toString, "C")
          case "in" => ((data.toFloat * 25.4).toString, "mm")
          case "in/s" => ((data.toFloat * 25.4).toString, "mm/s")
          case "gal/s" => ((data.toFloat / 0.2641).toString, "l/s")
          case "gal" => ((data.toFloat / 0.2641).toString, "l")
          case "mph" => ((data.toFloat / 0.6213).toString, "kph")
          case "oz" => ((data.toFloat * 29.573).toString, "ml")
          case "oz/s" => ((data.toFloat * 29.573).toString, "ml/s")
          case _ => (data, units)
        }
    }
  }

  
  def convertRow(row : scala.collection.mutable.Map[String,Any],arg: String) = {
  	val (u,d) = convert(row("sensor_units").toString,row("sensor_data").toString,arg)
  	row("sensor_units") = u
  	row("sensor_data") = d
  }
  
}