package org.bigsense.db

import scala.collection.immutable.List
import scala.collection.immutable.Map
import org.bigsense.model.FlatModel
import org.bigsense.model.DataModel

trait ServiceDataHandlerTrait extends DataHandlerTrait {

  def loadData(sets : List[DataModel]) : List[Int]
  
  def retrieveData(ids : List[Int]): List[DataModel]
  
  def retrieveDateRange(start: String, end : String, constraints : Map[String,Array[String]]) : List[FlatModel]
  
  def retrieveLatestImageInfo(limit : Int, constraints : Map[String,Array[String]]) : List[FlatModel]
  
  def retrieveImageInfoRange(start: String, end: String, constraints : Map[String,Array[String]] ) : List[FlatModel]
  
  def retrieveImage(id : Int) : Option[Array[Byte]]
  
  def listRelays() : List[FlatModel]
  
  def listSensors() : List[FlatModel]
  
  def retrieveLatestSensorData(limit : Int, constraints: Map[String,Array[String]]) : List[FlatModel]
  
  def sensorAliveStatus() : FlatModel
  
  def aggregate(start: String, end : String, stepping : String, aggType : AggregateType, constraints : Map[String,Array[String]]) : List[FlatModel]
  
  def retrievePemForRelay(relayId : String) : Option[String]
  
  //def addPhoto(package_id : Int, photo : Image) : Int

}