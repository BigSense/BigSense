/**
 *
 *
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.format

import scala.xml._
import io.bigsense.model._
import scala.collection.mutable.ListBuffer

class SenseDataXMLFormat extends FormatTrait {

  def renderModels(model: List[ModelTrait]): String = {

    if (model.length > 0) {
      model.head match {
        case x: DataModel => {
          return <sensedata> {
            for (pack <- model.asInstanceOf[List[DataModel]]) yield {
              <package id={pack.uniqueId} timestamp={pack.timestamp}> {
                pack.gps match {
                  case Some(gps: GPSModel) => <gps>
                    <location longitude={gps.location.longitude.toString} latitudqe={gps.location.latitude.toString} altitude={gps.location.altitude.toString} />
                    {
                      gps.delta match {
                        case Some(del : DeltaModel) => <delta speed={del.speed.toString} climb={del.climb.toString} track={del.track.toString} />
                        case None => {}
                      }
                      gps.accuracy match {
                        case Some(acc : AccuracyModel) => <accuracy longitude={acc.longitudeError.toString} latitude={acc.latitudeError.toString}
                                                                    altitude={acc.altitudeError.toString} speed={acc.speedError.toString}
                                                                    climb={acc.speedError.toString} track={acc.trackError.toString} />
                        case None => {}
                      }
                    }
                  </gps>
                  case None => {}
                }
              }{
                <sensors> {
                  for (sensor <- pack.sensors) yield {
                  <sensor id={sensor.uniqueId} type={sensor.stype} units={sensor.units} timestamp={sensor.timestamp}>
                    <data>
                      {sensor.data}
                    </data>
                  </sensor>
                  }
                }
                </sensors> <errors>
                {
                  for (error <- pack.errors) yield {
                    <error>
                      {error}
                    </error>
                  }
                }
              </errors>
              } </package>
            }}
          </sensedata>.toString()
        }
        case x: RelayModel => {
          return <senserelays> {
            for (r <- model.asInstanceOf[List[RelayModel]]) yield {
              /* TODO Get this working */
              /* <relay id={r.id} identifier={r.identifier} publicKey={r.publicKey} />*/
            }
          }
          </senserelays>.toString()
        }
        case _ => {
          //TODO: This needs to be an exception to generate a 400 BAD RESPONSE
          "Format not implemented for given model Type"
        }
      }
    }
    //TODO throw exception? No...hmm
    ""
  }


  def loadModels(data: String): List[ModelTrait] = {

    val xml: Elem = XML.loadString(data)

    var models = new ListBuffer[DataModel]

    for (pack <- xml \\ "package") yield {

      var model = new DataModel()

      val sensors = pack \ "sensors"
      val errors = pack \ "errors"
      val gps = pack \ "gps"

      model.timestamp = (pack \ "@timestamp").text.trim()
      model.uniqueId = (pack \ "@id").text.trim()

      model.gps = gps.size match {
        case 0 => None
        case 1 => Some(new GPSModel(
              new LocationModel(
                (gps \ "location" \ "@longitude").toString.toDouble ,
                (gps \ "location" \ "@latitude").toString.toDouble ,
                (gps \ "location" \ "@altitude").toString.toDouble
              ),
              (gps \ "delta" ).size match {
                case 0 => None
                case 1 => Some(new DeltaModel(
                  (gps \ "delta" \ "@speed").toString.toDouble,
                  (gps \ "delta" \ "@climb").toString.toDouble,
                  (gps \ "delta" \ "@track").toString.toDouble
                ))
              },
              (gps \ "accuracy" ).size match {
                case 0 => None
                case 1 => Some(new AccuracyModel(
                  (gps \ "accuracy" \ "@longitude").toString.toDouble,
                  (gps \ "accuracy" \ "@latitude").toString.toDouble,
                  (gps \ "accuracy" \ "@altitude").toString.toDouble,
                  (gps \ "accuracy" \ "@speed").toString.toDouble,
                  (gps \ "accuracy" \ "@climb").toString.toDouble,
                  (gps \ "accuracy" \ "@track").toString.toDouble
                ))
              }))
        case _ => None //TODO .. return an error?
                       // We can't have multiple locations,
                       // but validation should be taken care of elsewhere
      }

      var sbList = new ListBuffer[SensorModel]()

      for (node <- sensors \ "sensor") yield {
        var sensorData = new SensorModel()
        sensorData.uniqueId = (node \ "@id").text.trim()
        sensorData.stype = (node \ "@type").text.trim()
        sensorData.units = (node \ "@units").text.trim()
        sensorData.timestamp = (node \ "@timestamp").text.trim()
        sensorData.data = (node \ "data").text.trim()
        sbList += sensorData
      }
      for (err <- errors \ "error") yield {
        model.errors.append(err.text.trim())
      }
      model.sensors = sbList.toList
      models += model
    }
    models.toList
  }

}
