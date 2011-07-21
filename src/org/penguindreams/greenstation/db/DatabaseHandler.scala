package org.penguindreams.greenstation.db

import javax.sql.DataSource
import org.penguindreams.greenstation.spring.MySpring
import java.sql.Connection

class DatabaseHandler {

  private var ds : DataSource = null
  
  def DatabaseHandler() {
    this.ds = MySpring.getObject("mainDataSource").asInstanceOf[DataSource]
  }
  
  def runQuery(qName : String, args : String*) {
    var conn : Connection = ds.getConnection()
    
  }
  
}