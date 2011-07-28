package org.penguindreams.greenstation.db

import javax.sql.DataSource
import org.penguindreams.greenstation.spring.MySpring
import java.sql.Connection
import org.penguindreams.greenstation.model.DataModel
import org.penguindreams.greenstation.util.Properties
import java.sql.PreparedStatement
import java.util.Properties

class DatabaseHandler {

  private var ds : DataSource = null
  private var commands : Properties = null
  
  private var sqlCommands : Map[String,String] = null
  
  def DatabaseHandler() {
    this.ds = MySpring.getObject("mainDataSource").asInstanceOf[DataSource]
    
    this.sqlCommands = Properties.loadFile(  getClass().getResource("mssql.commands").getFile() )

  }
  
  def runQuery(qName : String, args : String*) {
    var conn : Connection = ds.getConnection()
    var stmt : PreparedStatement = conn.prepareStatement(sqlCommands(qName))
    var x = 1
    args.foreach( a => { stmt.setObject(x,a); x += 1 })
    var ret  = stmt.executeQuery()
    var meta = ret.getMetaData()
    
    while(ret.next) {
      
    }
  }
  
  def loadData(sets : List[DataModel]) {
    //TOOD: Start Transaction
    sets.foreach( set => {
      runQuery("getRelayId",set.uniqueId)
      
      set.sensors.foreach( sensor => {
        
      })
    })    
  }
  
  
  
}