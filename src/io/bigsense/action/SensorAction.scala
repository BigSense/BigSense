/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.action {

import scala.collection.Map
import io.bigsense.format.FormatTrait
import io.bigsense.model.DataModel
import scala.reflect.BeanProperty
import io.bigsense.db.ServiceDataHandlerTrait
import io.bigsense.db.DataHandlerTrait
import javax.servlet.http.HttpServletResponse
import io.bigsense.spring.MySpring
import io.bigsense.processor.ProcessorTrait
import io.bigsense.model.SensorModel
import io.bigsense.processor.ProcessorException
  
	class SensorAction extends ActionTrait {	

	  def runAction(aReq : ActionRequest): ActionResponse = {
	    
	    var resp = new ActionResponse()
	    	    
	    aReq.method match {
	      case "POST" => {
	        
	        /*
	         * This entire section is for N*Us or "Non-Processed <Type> Units"
	         */
	        aReq.models.foreach( packs => {
	          packs.sensors.foreach( sens => {	            
	            val nPat = "N(.*)U".r //Regex for NImageU, NCounterU, etc.

	            sens.units match {
	              case nPat(processor) => {
	                try {
	                  packs.processed.append( MySpring.getObject(processor + "Processor").asInstanceOf[ProcessorTrait].processModels(sens) )
	                }
	                catch {
	                  case e:ProcessorException => {
	                    packs.processed.append(sens.clone().asInstanceOf[SensorModel])
	                    sens.data = ""
	                    packs.errors.append(e.getMessage())
	                  } 
	                }
	              }
	              case _ => {}
	            }	            	          
	          })
	        })
	        
	        
	    	resp.newLocations = this.dbHandler.loadData(aReq.models)
	    	resp.status = HttpServletResponse.SC_CREATED
	      }
	      case "GET" => {
	          var models = dbHandler.retrieveData(List( aReq.args(1).toInt ))
	          if(models.length == 0) {
	        	  resp.status = HttpServletResponse.SC_NOT_FOUND
	        	  resp.output = "Record could not be found"
	          }
	          else {
	        	  resp.output = aReq.format.renderModels( models );
	        	  resp.status = HttpServletResponse.SC_OK
	          }
	      }
	    }
	    resp
	  }
	}
}