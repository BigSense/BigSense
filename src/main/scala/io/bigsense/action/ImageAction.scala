package io.bigsense.action

import javax.servlet.http.HttpServletResponse
import io.bigsense.util.TimeHelper

class ImageAction extends ActionTrait {

  def runAction(aReq: ActionRequest): Response = {

    aReq.method match {
      case "GET" => {
        aReq.args(1) match {
          case "List" => {
            aReq.args(2) match {
              case "Latest" => {
                val limit: Int = aReq.args(3).toInt
                StringResponse(aReq.format.renderModels(dbHandler.retrieveLatestImageInfo(limit, aReq.parameters)))
              }
              case "TimestampRange" => {
                val start = TimeHelper.timestampToDate(aReq.args(3))
                val end = TimeHelper.timestampToDate(aReq.args(4))
                StringResponse(aReq.format.renderModels(dbHandler.retrieveImageInfoRange(start, end, aReq.parameters)))
              }
              case "DateRange" => {
                //Convert Timezone if nescessary
                val start = TimeHelper.convertDateArgument(aReq.args(3), aReq.parameters)
                val end = TimeHelper.convertDateArgument(aReq.args(4), aReq.parameters)
                StringResponse(aReq.format.renderModels(dbHandler.retrieveDateRange(start, end, aReq.parameters)))
              }
            } //End match List types
          }
          case "Pull" => {
            val image = dbHandler.retrieveImage(aReq.args(2).toInt)
            image match {
              case Some(imgBytes: Array[Byte]) => {
                BinaryResponse(imgBytes, HttpServletResponse.SC_OK, Some("image/jpeg"))
              }
              case None => {
                StringResponse("Image Not Found", HttpServletResponse.SC_NOT_FOUND)
              }
            }
          }
        }
      }
    }
  }
}