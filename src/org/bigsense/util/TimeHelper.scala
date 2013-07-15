package org.bigsense.util
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.SimpleTimeZone
import java.sql.Timestamp

object TimeHelper {
  
  def convertDateArgument(date : String, parameters : Map[String,Array[String]] ) : String = {
     
    val df1 : SimpleDateFormat = new SimpleDateFormat()
	df1.applyPattern("yyyyMMdd");
    df1.setTimeZone(TimeZone.getTimeZone("UTC"))

    val df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")  
    
    //We should really error on multiple TimeZones, but 
    // we're just going to chose the last one
    if(parameters.contains("Timezone")) {
      df2.setTimeZone(TimeZone.getTimeZone(parameters("Timezone").last))
      return df2.format(df1.parse(date))
    }    
    date
  }
  
  //def timestampToDate(unixTimeStamp: String) : String = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  //    .format(new java.sql.Date(unixTimeStamp.toLong * 1000))

  def timestampToDate(unixTimeStamp: String) : java.sql.Date = new java.sql.Date(unixTimeStamp.toLong * 1000)


  def timestampToSQLString(stamp : Long) : String = {
	  var sdf : SimpleDateFormat = new SimpleDateFormat()
      sdf.setTimeZone(new SimpleTimeZone(0, "UTC"));
      sdf.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
      sdf.format(new Timestamp(stamp))
  }
}