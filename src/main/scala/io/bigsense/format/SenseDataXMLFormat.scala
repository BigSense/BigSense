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
                pack.location match {
                  case Some(loc: LocationModel) => <location x={loc.x.toString} y={loc.y.toString}
                                                             accurary={loc.accuracy.toString}
                                                             altitude={loc.altitude.toString} />
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
      val location = pack \ "location"

      model.timestamp = (pack \ "@timestamp").text.trim()
      model.uniqueId = (pack \ "@id").text.trim()

      model.location = location.size match {
        case 0 => None
        case 1 => Some(new LocationModel((location \ "@x").toString.toDouble,
          (location \ "@y").toString.toDouble,
          (location \ "@accuracy").toString.toDouble,
          (location \ "@altitude").toString.toDouble))
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