package io.bigsense.db

import scala.collection.immutable.List
import scala.collection.immutable.Map
import io.bigsense.model.FlatModel
import io.bigsense.model.DataModel

trait ServiceDataHandlerTrait extends DataHandlerTrait {

  def loadData(sets : List[DataModel]) : List[Int]
  
  def retrieveData(ids : List[Int]): List[DataModel]
  
  def retrieveDateRange(start: java.sql.Timestamp, end : java.sql.Timestamp, constraints : Map[String,Array[Any]]) : List[FlatModel]

  def retrieveLatestImageInfo(limit : Int, constraints : Map[String,Array[Any]]) : List[FlatModel]

  def retrieveImageInfoRange(start: java.sql.Timestamp, end: java.sql.Timestamp, constraints : Map[String,Array[Any]] ) : List[FlatModel]

  def retrieveImage(id : Int) : Option[Array[Byte]]

  def listRelays() : List[FlatModel]

  def listSensors() : List[FlatModel]

  def retrieveLatestSensorData(limit : Int, constraints: Map[String,Array[Any]]) : List[FlatModel]

  def sensorAliveStatus() : FlatModel

  def aggregate(start: java.sql.Timestamp, end : java.sql.Timestamp, stepping : Int, aggType : AggregateType, constraints : Map[String,Array[Any]]) : List[FlatModel]
  
  def retrievePemForRelay(relayId : String) : Option[String]
  
  //def addPhoto(package_id : Int, photo : Image) : Int

}