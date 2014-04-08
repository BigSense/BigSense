package io.bigsense.db
import java.sql.Connection

class DBRequest(val conn : Connection, val queryName : String) {

  //def runQuery(conn : Connection, qName : String, maxRows : Int, constraints : Map[String,Array[String]], args : Any*) : DBResult = {}

  var maxRows : Int = -1
  var constraints : Map[String,Array[Any]] = Map()
  var args : List[Any] = List()
  var order : Option[String] = None
  var group : Option[String] = None
}