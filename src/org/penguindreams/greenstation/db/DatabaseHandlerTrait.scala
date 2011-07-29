package org.penguindreams.greenstation.db
import org.penguindreams.greenstation.model.DataModel

trait DatabaseHandlerTrait {

  def runQuery(qName : String, args : String*) : DBResult
  def loadData(sets : List[DataModel])
}