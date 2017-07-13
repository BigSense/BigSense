/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.db


import io.bigsense.model._
import org.postgis.{PGgeometry, Point}
import scala.collection.mutable.ListBuffer
import java.sql.Timestamp
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.SimpleTimeZone
import io.bigsense.conversion.ConverterTrait
import net.jmatrix.eproperties.EProperties
import java.awt.Image
import java.io.ByteArrayInputStream
import org.apache.commons.codec.binary.Base64
import io.bigsense.util.{Numbers, WebAppInfo, TimeHelper}
import java.sql.Blob
import java.sql.Clob
import java.sql.Types
import io.bigsense.util.IO.using

class ServiceDataHandler extends ServiceDataHandlerTrait {
  

  
  private val standardFlatHeaders = List("PackageID","TimeStamp","TimeZone","RelayID","SensorID","SensorType","Units","Data",
    "Longitude","Latitude","Altitude","Speed","Climb", "Track",
    "LongitudeError", "LatitudeError", "AltitudeError", "SpeedError", "ClimbError", "TrackError")
  private val standardFlatColumns = List("package_id","time","timezone","relay","sensor","sensor_type","sensor_units","sensor_data",
    "longitude","latitude","altitude","speed","climb","track","longitude_error","latitude_error","altitude_error",
    "speed_error","climb_error","track_error")
  
  private val imageFlatHeaders = List("ImageID","PackageID","TimeStamp","TimeZone","RelayID","SensorID","PhotoURL","SensorType","Units","Data")
  private val imageFlatColumns = List("image_id","package_id","time","timezone","relay","sensor","photo_url","sensor_type","sensor_units","sensor_data")
  

  
  def listRelays() : List[FlatModel] = {
    using( ds.getConnection() ) { conn =>
      val req = new DBRequest(conn,"listRelays")
      req.constraints = req.constraints ++ Map("Keys" -> Array[Any](""))
      List(FlatModel(List("RelayID","KeyPem"), List("unique_id","key_pem"), runQuery(req).results))
    }
  }

  def listSensors() : List[FlatModel] = {
    using(ds.getConnection()) { conn =>
        val req = new DBRequest(conn,"listSensors")
    	List(FlatModel(List("SensorID","RelayID","Units","SensorType"), List("sensor_id","relay_id","units","sensor_type"), runQuery(req).results))
    }
  }  
  
  def sensorAliveStatus() : FlatModel = {
    using(ds.getConnection()) {
      conn => {
        val req = new DBRequest(conn,"sensorAliveStatus")
        FlatModel(List("Relay","Last Report Time","Minutes Since Last Report", "Sensor","Type"),
          List("relay_id","last_report_time","time_since_last_report","sensor_unique_id","sensor_type"),
          runQuery(req).results)
      }
    }
   }
  
  def retrieveLatestSensorData(limit : Int, constraints: Map[String,Array[Any]]) : List[FlatModel] = {
    using(ds.getConnection()) { conn =>
       val req : DBRequest = new DBRequest(conn,"readPackages")
       req.maxRows = limit
       req.constraints = constraints
       req.order = Some("orderByPackDesc")
       List(FlatModel(standardFlatHeaders, standardFlatColumns, runQuery(req).results))
    }
  }
  
  def retrieveDateRange(start : java.sql.Timestamp, end : java.sql.Timestamp, constraints: Map[String,Array[Any]]) : List[FlatModel] = {
    using(ds.getConnection()) { conn =>
       val req : DBRequest = new DBRequest(conn,"readPackages")
              
       val htemp = scala.collection.mutable.Map(constraints.toSeq: _*)
       htemp("PackTimeAbove") = Array(start)
       htemp("PackTimeBelow") = Array(end)
       
       req.constraints = htemp.toMap
       req.order = Some("orderByPackDesc")
      List(FlatModel(standardFlatHeaders, standardFlatColumns, runQuery(req).results))
    }
  }
  
  def retrieveLatestImageInfo(limit : Int, constraints : Map[String,Array[Any]]) : List[FlatModel] = {
    using(ds.getConnection()) { conn =>
      val req : DBRequest = new DBRequest(conn,"listImages")
      
      req.maxRows = limit      
      req.constraints = constraints
      req.args = List(WebAppInfo.servletPath + '/')
      
      req.order = Some("orderByPackDesc")
      List(FlatModel(imageFlatHeaders, imageFlatColumns, runQuery(req).results))
    }
  }
  
  def retrieveImageInfoRange(start: java.sql.Timestamp, end: java.sql.Timestamp, constraints : Map[String,Array[Any]] ) : List[FlatModel] = {
    using(ds.getConnection()) { conn =>
       val req : DBRequest = new DBRequest(conn,"listImages")
              
       val htemp = scala.collection.mutable.Map(constraints.toSeq: _*)
       htemp("PackTimeAbove") = Array(start)
       htemp("PackTimeBelow") = Array(end)
       
       req.constraints = htemp.toMap
       req.args = List(WebAppInfo.servletPath + '/')
       req.order = Some("orderByPackDesc")
       List(FlatModel(imageFlatHeaders, imageFlatColumns, runQuery(req).results))
    }
  }

 def retrieveImage(id : Int) : Option[Array[Byte]] = {
   
   using(ds.getConnection()) { conn =>
     val req : DBRequest = new DBRequest(conn,"pullImage")
     req.args = List (id)
     val results = runQuery(req).results
     results.length match {
       case 1 => {
         val blob = results(0)("image").asInstanceOf[Blob]
    	 Some(blob.getBytes(1,blob.length().toInt)) 
       }
       case 0 => { None }       
       case _ => { throw new DatabaseException("Multiple Results Found For Unique ID") }
     }
   }
 }
 
  def retrievePemForRelay(relayName : String) : Option[String] = {
    using(ds.getConnection()) { conn =>
      val req : DBRequest = new DBRequest(conn,"getRelayPem")
      req.args = List(relayName)
      val results = runQuery(req).results
      results.length match {
        case 1 => {
          results(0)("key_pem") match {
            //MS SQL returns TEXT fields as CLOBs
            case clob:Clob => {
              Some(clob.getSubString(1,clob.length().asInstanceOf[Int]))
            }
            //MySQL returns TEXT fields as Strings
            case str:String => {
              Some(str)
            }
            case null => None
          }

        }
        case 0 => { None }
        case _ => { throw new DatabaseException("Multiple Results For Unique ID") }
      }
    }
  }

  def setPemForRelay(relayName: String, privatePem: String)  {
    using(ds.getConnection()) { conn =>
      conn.setAutoCommit(false)

      val req = new DBRequest(conn,"getRelayId")
      req.args = List(relayName)

      if(runQuery(req).results.length == 0) {
        val req : DBRequest = new DBRequest(conn,"insertRelayPem")
        req.args = List(relayName, privatePem)
        runQuery(req).results
      }
      else {
        val req : DBRequest = new DBRequest(conn,"updateRelayPem")
        req.args = List(privatePem, relayName)
        runQuery(req).results
      }

      conn.setAutoCommit(true)
    }
  }
  
  def retrieveData(ids : List[Int]): List[DataModel] = {
    val retlist = new ListBuffer[DataModel]
    using(ds.getConnection()) { conn =>
	   ids.foreach( id => {
	      val req : DBRequest = new DBRequest(conn,"readPackage")
          req.args = List(id)
	      val ret : DBResult = runQuery(req)
	      retlist.appendAll((getDataModels(ret)))
	    })
    }
    retlist.toList
  }
  
  def aggregate(start: java.sql.Timestamp, end : java.sql.Timestamp, stepping : Int, aggType : AggregateType, constraints : Map[String,Array[Any]]) : List[FlatModel] = {
    using(ds.getConnection()) { conn => 
      val headers = List("TimeZone","RelayID","SensorID","SensorType","Interval","Total","Units")
      val cols = List("timezone","relay","sensor","sensor_type","time","sensor_data","sensor_units")

      val req : DBRequest = new DBRequest(conn,aggType.sql)
      
      val htemp = scala.collection.mutable.Map(constraints.toSeq: _*)
      htemp("IntervalAbove") = Array(start)
      htemp("IntervalBelow") = Array(end)
      htemp("NumericData") = Array(true)
      req.constraints = htemp.toMap
      
      req.group = Some("groupAggregation")
      req.order = Some("orderByAggregation")
      
      req.args = List(stepping,stepping)
      List(FlatModel(headers, cols, runQuery(req).results))
    }
  }
  
  
  private def getDataModels(results : DBResult) : List[DataModel] =
    results.results.groupBy( id => Numbers.toLong(id("package_id")) ).map { groupedRow =>

      // Table Join / these fields will be identical
      val row = groupedRow._2.head

      val timestamp = row("time") match {
        case e: Timestamp => e.getTime.toString
        case s: String => s
      }
      val uniqueId = row("relay").toString

      // GPS/Location
      val locationMap = mapRowDoubleList(List("latitude", "longitude", "altitude"), row)
      val deltaMap = mapRowDoubleList(List("speed", "climb", "track"), row)
      val accMap = mapRowDoubleList(List("latitude_error", "longitude_error", "altitude_error", "speed_error", "climb_error", "track_error"), row)

      val gps = if (locationMap.values.exists(_.isDefined) || deltaMap.values.exists(_.isDefined) || accMap.values.exists(_.isDefined)) {
        Some(GPSModel(
          if (locationMap.values.exists(_.isDefined)) {
            Some(LocationModel(
              locationMap("longitude"),
              locationMap("latitude"),
              locationMap("altitude")
            ))
          }
          else None,
          if (deltaMap.values.exists(_.isDefined)) {
            Some(DeltaModel(
              deltaMap("speed"),
              deltaMap("climb"),
              deltaMap("track")
            ))
          }
          else None,
          if (accMap.values.exists(_.isDefined)) {
            Some(AccuracyModel(
              accMap("longitude_error"),
              accMap("latitude_error"),
              accMap("altitude_error"),
              accMap("speed_error"),
              accMap("climb_error"),
              accMap("track_error")
            ))
          }
          else None
        ))
      }
      else None

      val sensors = groupedRow._2.map { row =>
        SensorModel(
          row("sensor").toString,
          row("sensor_units").toString,
          row("sensor_type").toString,
          row("sensor_data").toString
        )
      }
      
      DataModel(timestamp, uniqueId, sensors, gps)
    }.toList
  
  
  def loadData(sets : List[DataModel]) : List[Int] = {

    var generatedIds : ListBuffer[Int] = new ListBuffer()
    
    using(ds.getConnection()) { conn =>

	    //Start Transaction
	    conn.setAutoCommit(false)
	    try{
		    var req : DBRequest = null
		    
		    sets.foreach( set => {
		      req = new DBRequest(conn,"getRelayId")
		      req.args = List(set.id)
		      val rid : DBResult = runQuery(req)
		      
		      var relayId : java.lang.Integer = null
		      if(rid.results.length == 0) {
		        req = new DBRequest(conn,"registerRelay")
		        req.args = List(set.id)
		        relayId = runQuery(req).generatedKeys(0).asInstanceOf[Int]
		      }
		      else {
		        relayId = rid.results(0)("id").toString().toInt;
		      }
		      
		      req = new DBRequest(conn,"addDataPackage")
          val time = TimeHelper.timestampToDate(set.timestamp.toString)
          req.args = List(dbDialect match {
            case DB_PGSQL | DB_MYSQL => time
            case DB_MSSQL => time.toString //MSSQL Rounds the time incorrectly if using a Timestamp
          }, relayId)
		      val packageId = runQuery(req)
		         .generatedKeys(0)
		         .asInstanceOf[Int]
		      generatedIds += packageId //We will pull data in GET via packageId      

          //location
          set.gps match {
            case Some(gps : GPSModel) => {
              req = new DBRequest(conn,"addLocation")
              req.args = List(packageId) ++
                // Long and Latitude must be set ot make a point
                (gps.location match {
                  case Some(loc : LocationModel) => {
                    (loc.longitude match {
                      case Some(long : Double) => {
                        loc.latitude match {
                          case Some(lat : Double) => {
                            List(dbDialect match {
                              case DB_PGSQL => new PGgeometry(new Point(long, lat))
                              case DB_MYSQL | DB_MSSQL => s"POINT($long $lat)"
                            })
                          }
                          case None => List(NullParameter(Types.NULL))
                        }
                      }
                      case None => List(NullParameter(Types.NULL))
                    }) ++
                    List(doubleOrNone(loc.altitude))
                  }
                  case None => List(NullParameter(Types.NULL), NullParameter(Types.DOUBLE))
                }) ++
              (gps.delta match {
                case Some(d : DeltaModel) => List(
                  doubleOrNone(d.speed),
                  doubleOrNone(d.climb),
                  doubleOrNone(d.track))
                case None => List(NullParameter(Types.DOUBLE), NullParameter(Types.DOUBLE), NullParameter(Types.DOUBLE))
              }) ++
              (gps.accuracy match {
                case Some(acc : AccuracyModel) => List(
                  doubleOrNone(acc.longitudeError),
                  doubleOrNone(acc.latitudeError),
                  doubleOrNone(acc.altitudeError),
                  doubleOrNone(acc.speedError),
                  doubleOrNone(acc.climbError),
                  doubleOrNone(acc.trackError)
                )
                case None => List(NullParameter(Types.DOUBLE), NullParameter(Types.DOUBLE), NullParameter(Types.DOUBLE),
                  NullParameter(Types.DOUBLE), NullParameter(Types.DOUBLE), NullParameter(Types.DOUBLE))
              })
              runQuery(req)
            }
            case None => {}
          }

		      var sensorId : java.lang.Integer = -1
		      set.sensors.foreach( sensor => {
		         req = new DBRequest(conn,"getSensorRecord")
		         req.args = List(relayId,sensor.id)
		         val sid : DBResult = runQuery(req)
		         if(sid.results.length == 0) {
		           req = new DBRequest(conn,"addSensorRecord")
		           req.args = List(sensor.id,relayId,sensor.`type`,sensor.units)
		           sensorId = runQuery(req).generatedKeys(0).toString().toInt
		         }
		         else {
		           sensorId = sid.results(0)("id").toString().toInt;
		         }
		         req = new DBRequest(conn,"addSensorData")
		         req.args = List(packageId,sensorId,sensor.data)
		         runQuery(req)
		      })
		    })
		    conn.commit()
	    }
	    catch {
	      case e:Exception => { 
	        //make sure we unlock the transaction but pass the exception onward
	        conn.rollback() 
	        throw e
	      }
	    }
	    conn.setAutoCommit(true)
	    generatedIds.toList
    }
  }
  
  
  
}
