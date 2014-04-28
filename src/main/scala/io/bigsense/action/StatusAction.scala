package io.bigsense.action

import io.bigsense.model.FlatModel
import scalaj.collection.Imports._
import scala.collection.JavaConversions._
import javax.servlet.http.HttpServletResponse
import io.bigsense.server.{BigSenseServer, JettyServer}

class StatusAction extends ActionTrait {

  private def setParam(aReq: ActionRequest, param : String) : Int = {
    //TODO: defaults in web.xml
    if (aReq.parameters.contains(param)) aReq.parameters(param)(0).toString.toInt else 10
  }

  def runAction(aReq: ActionRequest): Response = {

    aReq.method match {
      case "GET" => {
        if (aReq.args.length == 1) {
          new ViewResponse(html.status(BigSenseServer.webRoot,BigSenseServer.contentRoot,setParam(aReq,"threshold"),setParam(aReq,"refresh")))
        }
        else {
          aReq.args(1) match {
            case "ajaxStatusTable" => {
              val dbStatus: FlatModel = dbHandler.sensorAliveStatus()
              new ViewResponse(html.ajaxStatusTable(dbStatus.headers,dbStatus.cols,dbStatus.rows,setParam(aReq,"threshold")))
            }
          }
        }
      }
    }
  }

}