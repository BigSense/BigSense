/**
 *
 *
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.action {

import javax.servlet.http.HttpServletResponse

class SensorAction extends ActionTrait {

  def runAction(aReq: ActionRequest): Response = {

    aReq.method match {
      case "POST" => {
        new StringResponse("", HttpServletResponse.SC_CREATED, newLocations = this.dbHandler.loadData(aReq.models))
      }
      case "GET" => {
        val models = dbHandler.retrieveData(List(aReq.args(1).toInt))
        if (models.length == 0) {
          new StringResponse("Record could not be found", HttpServletResponse.SC_NOT_FOUND)
        }
        else {
          new StringResponse(aReq.format.renderModels(models))
        }
      }
    }
  }
}

}