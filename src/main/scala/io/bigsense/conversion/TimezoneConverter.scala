package io.bigsense.conversion

import scala.collection.mutable.Map
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Date

class TimezoneConverter extends ConverterTrait {

	def convertRow(row : scala.collection.mutable.Map[String,Any],arg: String) = {
	  
	  	val zone = row("timezone").toString()
	  	val time = row("time").toString()
	
	  	//Taken From:
	  	// http://www.coderanch.com/t/328797/java/java/TimeZone-Conversion
	  	
	  	val  df1 : DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
		  df1.setTimeZone(TimeZone.getTimeZone(zone))
		
		  val dt : Date = df1.parse(time)
	  	
		  val  df2 : DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
	  	df2.setTimeZone(TimeZone.getTimeZone(arg))
	  	
	  	row("timezone") = arg
	  	row("time") = df2.format(dt)
	  
	}

}
