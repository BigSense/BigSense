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
    val ret = new FlatModel()
    using( ds.getConnection() ) { conn =>
      ret.headers = List("RelayID","PublicKey")
      ret.cols = List("unique_id","public_key")
      val req = new DBRequest(conn,"listRelays")
      ret.rows = runQuery(req).results  
    }
    List(ret)
  }

  def listSensors() : List[FlatModel] = {
    val ret = new FlatModel()
    ret.headers = List("SensorID","RelayID","Units","SensorType")
    ret.cols = List("sensor_id","relay_id","units","sensor_type")
    using(ds.getConnection()) { conn =>
        val req = new DBRequest(conn,"listSensors")
    	ret.rows = runQuery(req).results  
    }
    List(ret)
  }  
  
  def sensorAliveStatus() : FlatModel = {
    val ret = new FlatModel()
    ret.headers = List("Relay","Last Report Time","Minutes Since Last Report", "Sensor","Type")
    ret.cols = List("relay_id","last_report_time","time_since_last_report","sensor_unique_id","sensor_type")
    using(ds.getConnection()) {
      conn => {
        val req = new DBRequest(conn,"sensorAliveStatus")
        ret.rows = runQuery(req).results
      }
    }
    ret
   }
  
  def retrieveLatestSensorData(limit : Int, constraints: Map[String,Array[Any]]) : List[FlatModel] = {
    val model = new FlatModel()
    using(ds.getConnection()) { conn =>
       model.headers = standardFlatHeaders 
       model.cols = standardFlatColumns
       val req : DBRequest = new DBRequest(conn,"readPackages")
       req.maxRows = limit
       req.constraints = constraints
       req.order = Some("orderByPackDesc")
       model.rows =  runQuery(req).results
    }
    List(model)
  }
  
  def retrieveDateRange(start : java.sql.Timestamp, end : java.sql.Timestamp, constraints: Map[String,Array[Any]]) : List[FlatModel] = {
    val model = new FlatModel()
    using(ds.getConnection()) { conn =>
       model.headers = standardFlatHeaders
       model.cols = standardFlatColumns
       val req : DBRequest = new DBRequest(conn,"readPackages")
              
       val htemp = scala.collection.mutable.Map(constraints.toSeq: _*)
       htemp("PackTimeAbove") = Array(start)
       htemp("PackTimeBelow") = Array(end)
       
       req.constraints = htemp.toMap
       req.order = Some("orderByPackDesc")
       model.rows = runQuery(req).results
    }   
    List(model)
  }
  
  def retrieveLatestImageInfo(limit : Int, constraints : Map[String,Array[Any]]) : List[FlatModel] = {
    val model = new FlatModel()
    using(ds.getConnection()) { conn =>
      model.headers = imageFlatHeaders
      model.cols = imageFlatColumns
      val req : DBRequest = new DBRequest(conn,"listImages")
      
      req.maxRows = limit      
      req.constraints = constraints
      req.args = List(WebAppInfo.servletPath + '/')
      
      req.order = Some("orderByPackDesc")
      model.rows = runQuery(req).results
    }
    List(model)
  }
  
  def retrieveImageInfoRange(start: java.sql.Timestamp, end: java.sql.Timestamp, constraints : Map[String,Array[Any]] ) : List[FlatModel] = {
    val model = new FlatModel()
    using(ds.getConnection()) { conn =>
       model.headers = imageFlatHeaders
       model.cols = imageFlatColumns
       val req : DBRequest = new DBRequest(conn,"listImages")
              
       val htemp = scala.collection.mutable.Map(constraints.toSeq: _*)
       htemp("PackTimeAbove") = Array(start)
       htemp("PackTimeBelow") = Array(end)
       
       req.constraints = htemp.toMap
       req.args = List(WebAppInfo.servletPath + '/')
       req.order = Some("orderByPackDesc")
       model.rows = runQuery(req).results      
    }
    List(model)
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
 
  def retrievePemForRelay(relayId : String) : Option[String] = {
    using(ds.getConnection()) { conn =>
      var req : DBRequest = new DBRequest(conn,"pemForRelay")
      req.args = List(relayId)
      val results = runQuery(req).results
      results.length match {
        case 1 => {
          results(0)("data_keys_pem") match {
            //MS SQL returns TEXT fields as CLOBs
            case clob:Clob => {
              Some(clob.getSubString(1,clob.length().asInstanceOf[Int]))
            }
            //MySQL returns TEXT fields as Strings
            case str:String => {
              Some(str)
            }
          }

        }
        case 0 => { None }
        case _ => { throw new DatabaseException("Multiple Results For Unique ID") }
      }
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
    val model = new FlatModel()
    using(ds.getConnection()) { conn => 
      model.headers = List("TimeZone","RelayID","SensorID","SensorType","Interval","Total","Units")
      model.cols = List("timezone","relay","sensor","sensor_type","time","sensor_data","sensor_units")
      
      
      val req : DBRequest = new DBRequest(conn,aggType.sql)
      
      val htemp = scala.collection.mutable.Map(constraints.toSeq: _*)
      htemp("IntervalAbove") = Array(start)
      htemp("IntervalBelow") = Array(end)
      htemp("NumericData") = Array(true)
      req.constraints = htemp.toMap
      
      req.group = Some("groupAggregation")
      req.order = Some("orderByAggregation")
      
      req.args = List(stepping,stepping)
      model.rows = runQuery(req).results
    }
    List(model)
  }
  
  
  private def getDataModels(results : DBResult) : List[DataModel] = {
        
    val ret = new ListBuffer[DataModel]
    var prev : Long = -1;
    if( results.results.length > 0) {
      prev = Numbers.toLong(results.results.head("package_id"))
    }
    var dmodel : DataModel = new DataModel()
	  for( row <- results.results) {
      //TODO: Should these three really be Ints instead of Longs? Why did this break anyway?
	    if( Numbers.toLong(row("package_id")) != prev) {
	      ret.append(dmodel)
	      dmodel = new DataModel()
	      log.debug("Changing Model from relay %s to %s".format(prev,row("package_id").asInstanceOf[Long]))
	    }
	    prev = Numbers.toLong(row("package_id"))
	  
	    dmodel.timestamp = row("time") match {
        case e:Timestamp => e.getTime().toString()
        case s:String => s
      }
	    dmodel.uniqueId  = row("relay").toString()

      // GPS/Location
      val location_map = mapRowDoubleList(List("latitude","longitude","altitude"), row)
      val delta_map    = mapRowDoubleList(List("speed","climb","track"), row)
      val acc_map      = mapRowDoubleList(List("latitude_error","longitude_error","altitude_error","speed_error","climb_error","track_error"), row)

      dmodel.gps = if( location_map.values.exists(_ == Some) || delta_map.values.exists(_ == Some) || acc_map.values.exists(_ == Some)) {
          Some(new GPSModel(
            if (location_map.values.exists(_ == Some)) {
              Some(new LocationModel(
                location_map("longitude"),
                location_map("latitude"),
                location_map("altitude")
              ))
            }
            else None,
            if (delta_map.values.exists(_ == Some)) {
              Some(new DeltaModel(
                delta_map("speed"),
                delta_map("climb"),
                delta_map("track")
              ))
            }
            else None,
            if (acc_map.values.exists(_ == Some)) {
              Some(new AccuracyModel(
                acc_map("longitude_error"),
                acc_map("latitude_error"),
                acc_map("altitude_error"),
                acc_map("speed_error"),
                acc_map("climb_error"),
                acc_map("track_error")
              ))
            }
            else None
          ))
        }
        else None


	    val sensorListBuf = new ListBuffer[SensorModel]
	    for( senrow <- results.results) {
	      var smodel : SensorModel = new SensorModel()
	      smodel.uniqueId = senrow("sensor").toString()
	      smodel.units = senrow("sensor_units").toString()
	      smodel.stype = senrow("sensor_type").toString()
	      smodel.data = senrow("sensor_data").toString()
	      sensorListBuf.append(smodel)
	    }
	    dmodel.sensors = sensorListBuf.toList
	  }
    ret.append(dmodel)
    ret.toList
  }
  
  
  def loadData(sets : List[DataModel]) : List[Int] = {

    var generatedIds : ListBuffer[Int] = new ListBuffer()
    
    using(ds.getConnection()) { conn =>

	    //Start Transaction
	    conn.setAutoCommit(false)
	    try{
		    var req : DBRequest = null
		    
		    sets.foreach( set => {
		      req = new DBRequest(conn,"getRelayId")
		      req.args = List(set.uniqueId)
		      val rid : DBResult = runQuery(req)
		      
		      var relayId : java.lang.Integer = null
		      if(rid.results.length == 0) {
		        req = new DBRequest(conn,"registerRelay")
		        req.args = List(set.uniqueId)
		        relayId = runQuery(req).generatedKeys(0).asInstanceOf[Int]
		      }
		      else {
		        relayId = rid.results(0)("id").toString().toInt;
		      }
		      
		      req = new DBRequest(conn,"addDataPackage")
          req.args = List(TimeHelper.timestampToDate(set.timestamp),relayId)
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
                    loc.longitude match {
                      case Some(long : Double) => {
                        loc.latitude match {
                          case Some(lat : Double) => {
                            List(dbDialect match {
                              case DB_PGSQL => new PGgeometry(new Point(long, lat))
                              case DB_MYSQL => s"POINT(${long} ${lat})"
                              case DB_MSSQL => s"POINT(${long} ${lat})"
                            },loc.altitude)
                          }
                          case None => None
                        }
                      }
                      case None => None
                    }
                  }
                  case None => None
                }) ++
              (gps.delta match {
                case Some(d : DeltaModel) => List(d.speed, d.climb, d.track)
                case None => List(NullParameter(Types.DOUBLE), NullParameter(Types.DOUBLE), NullParameter(Types.DOUBLE))
              }) ++
              (gps.accuracy match {
                case Some(acc : AccuracyModel) => List(acc.longitudeError, acc.latitudeError, acc.altitudeError,
                acc.speedError, acc.climbError, acc.trackError)
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
		         req.args = List(relayId,sensor.uniqueId)
		         val sid : DBResult = runQuery(req)
		         if(sid.results.length == 0) {
		           req = new DBRequest(conn,"addSensorRecord")
		           req.args = List(sensor.uniqueId,relayId,sensor.stype,sensor.units)
		           sensorId = runQuery(req).generatedKeys(0).toString().toInt
		         }
		         else {
		           sensorId = sid.results(0)("id").toString().toInt;
		         }
		         req = new DBRequest(conn,"addSensorData")
		         req.args = List(packageId,sensorId,sensor.data)
		         runQuery(req)
		      })
		      set.processed.foreach( pro => {
		        pro.units match {
		          case "NImageU" => {
		            req = new DBRequest(conn,"addImage")
		            req.args = List(packageId, sensorId, new ByteArrayInputStream(Base64.decodeBase64(pro.data)))
		            runQuery(req)
		          }
		          case "NCounterU" => { /*TODO: Implement Me*/}
		          case _ => { set.errors.append("Unknown Processed Unit Type %s for Sensor %s With Data %s at Time %s"
		              .format(pro.units,pro.uniqueId,pro.data,pro.timestamp)) }
		        }
		      })	      
		      set.errors.foreach( error => {
		        req = new DBRequest(conn,"addError")
		        req.args = List(packageId,error)
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
