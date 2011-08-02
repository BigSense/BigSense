package org.penguindreams.greenstation.db

import javax.sql.DataSource
import org.penguindreams.greenstation.spring.MySpring
import java.sql.Connection
import org.penguindreams.greenstation.model.DataModel
import org.penguindreams.greenstation.util.Properties
import java.sql.PreparedStatement
import scala.collection.mutable.ListBuffer
import scala.reflect.BeanProperty
import org.apache.log4j.Logger
import java.sql.ResultSet
import java.sql.Date
import java.sql.Statement

class DatabaseHandler extends DatabaseHandlerTrait {

  this.sqlCommands = Properties.loadFile(  getClass().getResource("mssql.commands").getFile() )
  
  @BeanProperty 
  var ds : DataSource = _
  
  private var sqlCommands : Map[String,String] = _
  
  def runQuery(conn : Connection, qName : String, args : Any*) : DBResult = {
    
    var retval = new DBResult()
    
    //prepare statement
    var stmt : PreparedStatement = conn.prepareStatement(sqlCommands(qName),Statement.RETURN_GENERATED_KEYS)
    var x = 1
    args.foreach( a => { 
      a.asInstanceOf[AnyRef] match {
        case s: Integer => { stmt.setInt(x,s) }
        case s: String => { stmt.setString(x,s) }
        case s: Date => { stmt.setDate(x,s) }
        case s => { stmt.setObject(x,s) }
      }
      x += 1 
    })
    
    //run statement
    if( sqlCommands(qName).toLowerCase().startsWith("inert")) {
      stmt.executeUpdate();
    }
    else {
      stmt.execute()
    }
    
    //get auto-insert keys
	var keys = stmt.getGeneratedKeys()
	if(keys != null) {
	    var keybuf = new ListBuffer[Any]();
	    while(keys.next()) {
	      Logger.getLogger(this.getClass()).trace("Key: " +keys.getInt(1))
	      keybuf += keys.getInt(1)
	    }
	    retval.generatedKeys = keybuf.toList
	    Logger.getLogger(this.getClass()).trace("Keys: " + keybuf.toList)
	}
    
    //pull results
    var ret : ResultSet = stmt.getResultSet()
    if(ret != null) {
	      
	    var meta = ret.getMetaData()
	        
	    var retbuf = new ListBuffer[Map[String,Any]]()
	    while(ret.next) {
	      val rMap = scala.collection.mutable.Map[String,Any]()
	
	      for(i <- 1 to meta.getColumnCount()) {
	        rMap += ( meta.getColumnName(i) -> ret.getObject(i))
	      }
	      retbuf += Map(rMap.toSeq: _*)
	    }
	    
	    Logger.getLogger(this.getClass()).trace("Results: " + retbuf.toList)
	    
	    retval.results = retbuf.toList
	    ret.close()
    }
    
    //cleanup
    stmt.close()
    retval
  }
  
  def loadData(sets : List[DataModel]) {
    
    val log = Logger.getLogger(this.getClass())

    //Start Transaction
    var conn : Connection = ds.getConnection()

    conn.setAutoCommit(false)
    
    sets.foreach( set => {
      val rid : DBResult = runQuery(conn,"getRelayId",set.uniqueId)
      
      var relayId : java.lang.Integer = null
      if(rid.results.length == 0) {
        relayId = runQuery(conn,"registerRelay",set.uniqueId).generatedKeys(0).asInstanceOf[Int]
      }
      else {
        relayId = rid.results(0)("id").toString().toInt;
      }
      
      val packageId = runQuery(conn,"addDataPackage",new Date(math.round(set.timestamp.toDouble)),relayId)
         .generatedKeys(0)
         .asInstanceOf[Int]
      
      set.sensors.foreach( sensor => {
         var sensorId : java.lang.Integer = null
         val sid : DBResult = runQuery(conn,"getSensorRecord",relayId,sensor.uniqueId)
         if(sid.results.length == 0) {
           sensorId = runQuery(conn,"addSensorRecord",sensor.uniqueId,relayId,sensor.stype,sensor.units).generatedKeys(0).toString().toInt
         }
         else {
           sensorId = sid.results(0)("id").toString().toInt;
         }
         
         runQuery(conn,"addSensorData",packageId,sensor.data)
      })
    })

    conn.commit()
    conn.close()
  }
  
  
  
}