package org.penguindreams.greenstation.db

import javax.sql.DataSource
import org.penguindreams.greenstation.spring.MySpring
import java.sql.Connection
import org.penguindreams.greenstation.model.DataModel

class DatabaseHandler {

  private var ds : DataSource = null
  private var commands : Properties = null
  
  def DatabaseHandler() {
    this.ds = MySpring.getObject("mainDataSource").asInstanceOf[DataSource]
    Properties
    getClass().getResource("mssql.commands");
  }
  
  def runQuery(qName : String, args : String*) {
    var conn : Connection = ds.getConnection()
    
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