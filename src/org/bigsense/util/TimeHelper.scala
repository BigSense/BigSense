package org.bigsense.util
import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone, SimpleTimeZone}
import java.sql.Timestamp

object TimeHelper {
  
  /*def convertDateArgument(date : String, parameters : Map[String,Array[String]] ) : String = {
     
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
  }*/

  def convertDateArgument(date : String, parameters : Map[String,Array[Any]] ) : java.sql.Date = {

    val df1 : SimpleDateFormat = new SimpleDateFormat()
    df1.applyPattern("yyyyMMdd");
    df1.setTimeZone(TimeZone.getTimeZone("UTC"))

    val df2 = new SimpleDateFormat("yyyy-MM-dd")

    //We should really error on multiple TimeZones, but
    // we're just going to chose the last one
    var retval : String = ""
    if(parameters.contains("Timezone")) {
      df2.setTimeZone(TimeZone.getTimeZone(parameters("Timezone").last.toString))
      retval = df2.format(df1.parse(date))
    }
    else {
      retval = date
    }

    val parts = retval.split("-")
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.set(
       Integer.parseInt(parts(0)),
       Integer.parseInt(parts(1)),
       Integer.parseInt(parts(2))
    )
    new java.sql.Date(cal.getTimeInMillis())
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