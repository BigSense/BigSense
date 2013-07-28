/**
 * ActionRequest
 * 
 * Parameters pulled in from MasterServlet and given to ever Action Object
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.action
import io.bigsense.model.DataModel
import io.bigsense.format.FormatTrait
import io.bigsense.model.ModelTrait

/**
 * object cons
 */
class ActionRequest {

  var method: String = "INVALID"
  var args : Array[String] = Array()
  var parameters : Map[String,Array[Any]] = Map()
  var models : List[DataModel] = List()
  var data : String = _
  var format : FormatTrait = _
  var signature : Option[String] = None
  
  override def toString() : String =  {
    var out : StringBuilder = new StringBuilder("Action Request:\n")
    out.append("\tMethod: %s\n".format(method))
    out.append("\tFormat: %s\n".format(format))
    out.append("\tData: %s\n".format(data))
    out.append("\tSignature: %s\n".format(signature))
    out.append("\tModels: %s\n".format(models))
    args.addString(out,"\tArgs: ",", ","\n");
    for( (para,list) <- parameters ) {
      list.addString(out,"\tParam: %s\tValues: ".format(para),",","\n")
    }
    out.toString()
  }
  
}