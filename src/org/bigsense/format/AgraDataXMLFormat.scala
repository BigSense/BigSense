/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package org.bigsense.format
import scala.xml._
import org.apache.log4j.Logger
import org.bigsense.model.DataModel
import org.bigsense.spring.MySpring
import org.bigsense.model.SensorModel
import scala.collection.mutable.ListBuffer
import org.bigsense.model.ModelTrait
import org.bigsense.model.RelayModel

class AgraDataXMLFormat extends FormatTrait {

  def renderModels(model : List[ModelTrait]) : String = {

    if(model.length > 0) {
      model.head match {
        case x:DataModel => {
		  return <AgraData>{ 
		      for( pack <- model.asInstanceOf[List[DataModel]]) yield {
		        <package id={pack.uniqueId} timestamp={pack.timestamp}>
		        <sensors>{
		          for( sensor <- pack.sensors) yield {
		            <sensor id={sensor.uniqueId} type={sensor.stype} units={sensor.units} timestamp={sensor.timestamp}>
		            <data>{sensor.data}</data></sensor>
		          }
		        }</sensors><errors>{ for(error <- pack.errors) yield {
		          <error>{error}</error>
		        }}
                </errors></package>
		      }
	      }</AgraData>.toString()	    
        }
        case x:RelayModel => {
		  return <AgraRelays>{ 
		      for( r <- model.asInstanceOf[List[RelayModel]]) yield {
		        /* TODO Get this working */
		        /* <relay id={r.id} identifier={r.identifier} publicKey={r.publicKey} />*/
		      }
	      }</AgraRelays>.toString()
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

  
  def loadModels(data : String) : List[ModelTrait] = { 
    
    var xml : Elem = XML.loadString(data)
    
    var log : Logger = Logger.getLogger(this.getClass())
        
    var models = new ListBuffer[DataModel]
    
    for( pack <- xml \\ "package") yield {
        
    	var model = new DataModel()
        
	    var sensors = pack \ "sensors"
	    var errors = pack \ "errors"
	    model.timestamp = (pack \"@timestamp").text.trim()
	    model.uniqueId = (pack \"@id"  ).text.trim()
	       	    
	    var sbList = new ListBuffer[SensorModel]()
	    var sbErr = new ListBuffer[String]()
	    
	    for( node <- sensors \"sensor") yield {
	      var sensorData = new SensorModel()
	      sensorData.uniqueId = (node\"@id").text.trim()
	      sensorData.stype = (node\"@type").text.trim()
	      sensorData.units = (node\"@units").text.trim()
	      sensorData.timestamp  = (node\"@timestamp").text.trim()
	      sensorData.data = (node\"data").text.trim()
	      sbList += sensorData
	    }
    	for( err <- errors \"error") yield {
    	  model.errors.append(err.text.trim())
    	}
    	model.sensors = sbList.toList
    	models += model
    } 
    models.toList
  }
  
}