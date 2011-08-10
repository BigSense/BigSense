package org.penguindreams.greenstation.db

import scala.collection.immutable.List
import java.sql.Connection
import org.penguindreams.greenstation.model.DataModel

class DataEventHandler extends DataHandlerTrait {  
  
  def loadData(sets: List[DataModel]): List[Int] = { null }

  def retrieveData(ids : List[Int]): List[DataModel] = { null }
}