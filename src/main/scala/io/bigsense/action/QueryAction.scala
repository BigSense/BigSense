package io.bigsense.action

import io.bigsense.util.TimeHelper


class QueryAction extends ActionTrait {

  def runAction(aReq: ActionRequest): Response = {

    aReq.method match {
      case "GET" => {
        aReq.args(1) match {
          case "Latest" => {
            val limit: Int = aReq.args(2).toInt
            new StringResponse(aReq.format.renderModels(dbHandler.retrieveLatestSensorData(limit, aReq.parameters)))
          }
          case "TimestampRange" => {
            val start = TimeHelper.timestampToDate(aReq.args(2))
            val end = TimeHelper.timestampToDate(aReq.args(3))
            new StringResponse(aReq.format.renderModels(dbHandler.retrieveDateRange(start, end, aReq.parameters)))
          }
          case "DateRange" => {
            //Convert Timezone if nescessary
            val start = TimeHelper.convertDateArgument(aReq.args(2), aReq.parameters)
            val end = TimeHelper.convertDateArgument(aReq.args(3), aReq.parameters)
            new StringResponse(aReq.format.renderModels(dbHandler.retrieveDateRange(start, end, aReq.parameters)))
          }
          case "Relays" => {
            new StringResponse(aReq.format.renderModels(dbHandler.listRelays()))
          }
          case "Sensors" => {
            new StringResponse(aReq.format.renderModels(dbHandler.listSensors()))
          }
        }
      }
    }
  }
}
