package io.bigsense.action

import io.bigsense.model.FlatModel
import scalaj.collection.Imports._
import scala.collection.JavaConversions._
import javax.servlet.http.HttpServletResponse

class StatusAction extends ActionTrait {

  private def listMapToJava(l: List[Map[String, Any]]): java.util.List[java.util.Map[String, Object]] = l.map(_.asInstanceOf[Map[String, Object]].asJava).asJava

  private def setParameters(aReq: ActionRequest): Map[String, Any] = {
    //TODO: defaults in web.xml
    List("threshold", "refresh").map(
      i => {
        i -> (if (aReq.parameters.contains(i)) aReq.parameters(i)(0) else "10")
      }
    ).toMap
  }

  def runAction(aReq: ActionRequest): Response = {

    aReq.method match {
      case "GET" => {
        if (aReq.args.length == 1) {
          new ViewResponse("status", setParameters(aReq))
        }
        else {
          aReq.args(1) match {
            case "ajaxStatusTable" => {

              val dbStatus: FlatModel = dbHandler.sensorAliveStatus()

              new ViewResponse("ajaxStatusTable", setParameters(aReq) ++ Map(
                "StatusModelHeaders" -> dbStatus.headers,
                "StatusModelCols" -> dbStatus.cols,
                "StatusModelRows" -> listMapToJava(dbStatus.rows)
              ))
            }
          }
        }
      }
    }
  }

}