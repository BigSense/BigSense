package org.penguindreams.greenstation.db
import spring.MySpring
import javax.sql.DataSource

class DatabaseHandler {

  private var ds : DataSource = null
  
  def DatabaseHandler() {
    this.ds = MySpring.getObject("mainDataSource")
  }
  
  def runQuery(qName : String, args : String*) {
    
  }
  
}