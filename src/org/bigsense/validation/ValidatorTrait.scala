package org.bigsense.validation
import org.bigsense.action.ActionRequest
import javax.servlet.http.HttpServletResponse
import java.text.SimpleDateFormat

trait ValidatorTrait {

  val BAD_REQUEST = HttpServletResponse.SC_BAD_REQUEST
  val NOT_IMPLEMENTED = HttpServletResponse.SC_NOT_IMPLEMENTED
  val METHOD_NOT_ALLOWED = HttpServletResponse.SC_METHOD_NOT_ALLOWED
  
  protected def checkInt(obj : Any) : Boolean = {
    try {
      obj.toString().toInt
      return true
    }
    catch {
      case e:NumberFormatException => return false
    }
  }
  
  protected def checkDate(obj : String) : Boolean = {
    
    var sdf : SimpleDateFormat = new SimpleDateFormat()
	sdf.applyPattern("yyyyMMdd");
    try {
      sdf.parse(obj)
      return true
    }
    catch {
      case e:java.text.ParseException => return false
    }
  }
  
  def validateRequest(req : ActionRequest) : Option[ValidationError]
  
}