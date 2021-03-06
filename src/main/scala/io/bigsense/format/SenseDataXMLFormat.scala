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


  private def ndeDouble(o : NodeSeq) = if (o.toString.isEmpty) None else Some(o.toString.toDouble)
  private def optDouble(o : Option[Double]) = o match {
    case Some(o) => o.toString
    case None => ""
  }

  override def mimeType = "text/xml"

  def renderModels(model: List[ModelTrait]): String = {

    if (model.length > 0) {
      model.head match {
        case x: DataModel => {
          return <sensedata> {
            for (pack <- model.asInstanceOf[List[DataModel]]) yield {
              <package id={pack.id} timestamp={pack.timestamp.toString}> {
              }{
                pack.gps match {
                  case Some(gps: GPSModel) => <gps>
                    {
                    gps.location match {
                      case Some(loc: LocationModel) => <location longitude={optDouble(loc.longitude)} latitudqe={optDouble(loc.latitude)} altitude={optDouble(loc.altitude)}/>
                      case None => {}
                    }
                  }{
                    gps.delta match {
                      case Some(del: DeltaModel) => <delta speed={optDouble(del.speed)} climb={optDouble(del.climb)} track={optDouble(del.track)}/>
                      case None => {}
                    }
                  }{
                    gps.accuracy match {
                      case Some(acc : AccuracyModel) => <accuracy longitude={optDouble(acc.longitudeError)} latitude={optDouble(acc.latitudeError)}
                                                              altitude={optDouble(acc.altitudeError)} speed={optDouble(acc.speedError)}
                                                              climb={optDouble(acc.speedError)} track={optDouble(acc.trackError)} />
                      case None => {}
                    }
                  }
                  </gps>
                  case None => {}
                }
              }{
                <sensors> {
                  for (sensor <- pack.sensors) yield {
                  <sensor id={sensor.id} type={sensor.`type`} units={sensor.units}>
                    <data>
                      {sensor.data}
                    </data>
                  </sensor>
                  }
                }
                </sensors>
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

    for (pack <- xml \\ "package") yield {

      val sensors = pack \ "sensors"
      val errors = pack \ "errors"
      val gps = pack \ "gps"

      val timestamp = (pack \ "@timestamp").text.trim()
      val uniqueId = (pack \ "@id").text.trim()

      val gpsmodel = gps.size match {
        case 0 => None
        case 1 => Some(GPSModel(
              (gps \ "location" ).size match {
                case 0 => None
                case 1 => Some(LocationModel(
                ndeDouble(gps \ "location" \ "@longitude"),
                ndeDouble(gps \ "location" \ "@latitude"),
                ndeDouble(gps \ "location" \ "@altitude")
              ))
              },
              (gps \ "delta" ).size match {
                case 0 => None
                case 1 => Some(DeltaModel(
                  ndeDouble(gps \ "delta" \ "@speed"),
                  ndeDouble(gps \ "delta" \ "@climb"),
                  ndeDouble(gps \ "delta" \ "@track")
                ))
              },
              (gps \ "accuracy" ).size match {
                case 0 => None
                case 1 => Some(AccuracyModel(
                  ndeDouble(gps \ "accuracy" \ "@longitude_error"),
                  ndeDouble(gps \ "accuracy" \ "@latitude_error"),
                  ndeDouble(gps \ "accuracy" \ "@altitude_error"),
                  ndeDouble(gps \ "accuracy" \ "@speed_error"),
                  ndeDouble(gps \ "accuracy" \ "@climb_error"),
                  ndeDouble(gps \ "accuracy" \ "@track_error")
                ))
              }))
        case _ => None //TODO .. return an error?
                       // We can't have multiple locations,
                       // but validation should be taken care of elsewhere
      }

      val smodels = for (node <- sensors \ "sensor") yield {
        SensorModel(
          (node \ "@id").text.trim(),
          (node \ "@type").text.trim(),
          (node \ "@units").text.trim(),
          (node \ "data").text.trim()
        )
      }

      DataModel(timestamp, uniqueId, smodels.toList, gpsmodel)
    }
  }.toList

}
