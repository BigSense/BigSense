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
import java.util.Date

class DatabaseHandler extends DatabaseHandlerTrait {

  this.sqlCommands = Properties.loadFile(  getClass().getResource("mssql.commands").getFile() )
  
  @BeanProperty 
  var ds : DataSource = _
  
  private var sqlCommands : Map[String,String] = _
  
  def runQuery(qName : String, args : String*) : DBResult = {
    
    var retval = new DBResult()
    
    var conn : Connection = ds.getConnection()
    var stmt : PreparedStatement = conn.prepareStatement(sqlCommands(qName))
    var x = 1
    args.foreach( a => { stmt.setObject(x,a); x += 1 })
    
    stmt.execute()
    var ret : ResultSet = stmt.getResultSet()
      
    var meta = ret.getMetaData()
    
    var keys = stmt.getGeneratedKeys()
    var keybuf = new ListBuffer[Any]();
    while(keys.next()) {
      keybuf += keys.getObject(0)
    }
    retval.generatedKeys = keybuf.toList
    
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
    //cleanup
    ret.close()
    stmt.close()
    conn.close()
    retval
    
  }
  
  def loadData(sets : List[DataModel]) {
    
    val log = Logger.getLogger(this.getClass())
    //TOOD: Start Transaction
    log.trace("Start TRansaction")
    sets.foreach( set => {
      val rid : DBResult = runQuery("getRelayId",set.uniqueId)
      
      var relayId : String = null
      if(rid.results.length == 0) {
        relayId = runQuery("registerRelay",set.uniqueId).generatedKeys(0).asInstanceOf[String]
      }
      else {
        relayId = rid.results(0).get("id").toString();
      }
      
      val packageId = runQuery("addDataPackage","2011-01-01 01:01:00.00".toString(),relayId).generatedKeys(0).asInstanceOf[String]
      
      set.sensors.foreach( sensor => {
         var sensorId : String = null
         val sid : DBResult = runQuery("getSensorRecord",relayId,sensor.uniqueId)
         if(sid.results.length == 0) {
           sensorId = runQuery("addSensorRecord",sensor.uniqueId,relayId,sensor.stype,sensor.units).generatedKeys(0).asInstanceOf[String]
         }
         else {
           sensorId = sid.results(0).get("id").toString();
         }
         
         runQuery("addSensorData",packageId,sensor.data)
      })
    })    
  }
  
  
  
}