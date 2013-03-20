package org.bigsense.action
import scala.reflect.BeanProperty
import org.bigsense.conversion.ConverterTrait


class ReportAction extends ActionTrait {

  @BeanProperty var converters : scala.collection.mutable.Map[String,ConverterTrait] = _
  
  def runAction(aReq: ActionRequest): ActionResponse = { 
	    var resp = new ActionResponse()
	    	    
	    aReq.method match {
	      case "GET" => {
	    	  resp.view = Some("report")
	    	  resp.viewData("converters") = converters
	      }
	    }
	    resp    
  }

}