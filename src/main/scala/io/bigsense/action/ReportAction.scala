package io.bigsense.action
import scala.reflect.BeanProperty
import io.bigsense.conversion.ConverterTrait


class ReportAction extends ActionTrait {

  @BeanProperty var converters : scala.collection.mutable.Map[String,ConverterTrait] = _
  
  def runAction(aReq: ActionRequest): Response = {
	  aReq.method match {
	    case "GET" => new ViewResponse("report",Map("converters" -> converters))
	  }
  }
}