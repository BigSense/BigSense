package org.penguindreams.greenstation.db
import org.penguindreams.greenstation.model.DataModel
import java.sql.Connection

trait DataHandlerTrait {

  //def runQuery(conn: Connection, qName : String, args : Any*) : DBResult
  def loadData(sets : List[DataModel]) : List[Int]
  
  def retrieveData(ids : List[Int]): List[DataModel]
}