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

class DatabaseHandler extends DatabaseHandlerTrait {

  @BeanProperty 
  var ds : DataSource = _
  
  private var sqlCommands : Map[String,String] = _
  
  def DatabaseHandler() {    
    this.sqlCommands = Properties.loadFile(  getClass().getResource("mssql.commands").getFile() )
  }
  
  def runQuery(qName : String, args : String*) : DBResult = {
    
    var retval = new DBResult()
    
    var conn : Connection = ds.getConnection()
    var stmt : PreparedStatement = conn.prepareStatement(sqlCommands(qName))
    var x = 1
    args.foreach( a => { stmt.setObject(x,a); x += 1 })
    var ret  = stmt.executeQuery()
    var meta = ret.getMetaData()
    
    var keys = stmt.getGeneratedKeys()
    var keybuf = new ListBuffer[Any]();
    while(keys.next()) {
      keybuf += keys.getObject(0)
    }
    retval.generatedKeys = keybuf.toList
    
    var retbuf = new ListBuffer()
    while(ret.next) {
      
    }
    
    //cleanup
    ret.close()
    stmt.close()
    conn.close()
    retval
    
  }
  
  def loadData(sets : List[DataModel]) {
    
    //TOOD: Start Transaction
    /*sets.foreach( set => {
      runQuery("getRelayId",set.uniqueId)
      
      set.sensors.foreach( sensor => {
        
      })
    })*/    
  }
  
  
  
}