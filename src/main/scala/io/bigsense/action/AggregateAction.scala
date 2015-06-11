package io.bigsense.action
import io.bigsense.util.TimeHelper
import io.bigsense.db._

class AggregateAction extends ActionTrait {

  def runAction(aReq: ActionRequest): Response = {

    aReq.method match {
      case "GET" => {

        var start = new java.sql.Timestamp(1)
        var end = new java.sql.Timestamp(1)

        aReq.args(2) match {
          case "TimestampRange" => {
            start = TimeHelper.timestampToDate(aReq.args(3))
            end = TimeHelper.timestampToDate(aReq.args(4))
          }
          case "DateRange" => {
            //Convert Timezone if nescessary
            start = TimeHelper.convertDateArgument(aReq.args(3), aReq.parameters)
            end = TimeHelper.convertDateArgument(aReq.args(4), aReq.parameters)
          }
        }

        new StringResponse(
          aReq.format.renderModels(dbHandler.aggregate(start, end, Integer.parseInt(aReq.args(5)), {
            aReq.args(1) match {
              case "Sum" => AggregateSum
              case "Average" => AggregateAverage
            }
          }, aReq.parameters))
        )
      }
    }
  }

}