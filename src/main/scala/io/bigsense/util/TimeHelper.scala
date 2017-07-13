package io.bigsense.util
import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone}

object TimeHelper {


  def convertDateArgument(date : String, parameters : Map[String,Array[Any]] ) : java.sql.Timestamp = {

    val df1 : SimpleDateFormat = new SimpleDateFormat()
    df1.applyPattern("yyyyMMdd");
    df1.setTimeZone(TimeZone.getTimeZone("UTC"))

    val df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    //We should really error on multiple TimeZones, but
    // we're just going to chose the last one
    if(parameters.contains("Timezone")) {
      df2.setTimeZone(TimeZone.getTimeZone(parameters("Timezone").last.toString))
      val parts = df2.format(df1.parse(date)).split("-")

      cal.set(
        Integer.parseInt(parts(0)),
        Integer.parseInt(parts(1)) -1,
        Integer.parseInt(parts(2)),
        Integer.parseInt(parts(3)),
        Integer.parseInt(parts(4)),
        Integer.parseInt(parts(5))
      )

    }
    else {

      cal.set(
        //yyyyMMdd
        Integer.parseInt( date.substring(0,4)) ,
        Integer.parseInt(date.substring(4,6)) -1 ,
        Integer.parseInt(date.substring(6,8)) ,
        0,0,0
      )

    }

    new java.sql.Timestamp(cal.getTimeInMillis)
  }

  def timestampToDate(unixTimeStamp: String) : java.sql.Timestamp = new java.sql.Timestamp(unixTimeStamp.toLong)

  /**
   * returns the year in UTC for a UNIX timestamp in milliseconds.
   * Used in the bulk loader for minimum year constraint
   * @param unixTimeStamp timestamp as a milliseconds (Long)
   * @return year in UTC
   */
  def yearFromTimestamp(unixTimeStamp: String) : Int = {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.setTimeInMillis(unixTimeStamp.toLong)
    cal.get(Calendar.YEAR)
  }
}