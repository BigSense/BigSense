/**
 *
 *
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.action {

import javax.servlet.http.HttpServletResponse
import io.bigsense.spring.MySpring
import io.bigsense.processor.ProcessorTrait
import io.bigsense.model.SensorModel
import io.bigsense.processor.ProcessorException

class SensorAction extends ActionTrait {

  def runAction(aReq: ActionRequest): Response = {

    aReq.method match {
      case "POST" => {

        /*
         * This entire section is for N*Us or "Non-Processed <Type> Units"
         */
        aReq.models.foreach(packs => {
          packs.sensors.foreach(sens => {
            val nPat = "N(.*)U".r //Regex for NImageU, NCounterU, etc.

            sens.units match {
              case nPat(processor) => {
                try {
                  packs.processed.append(MySpring.getObject(processor + "Processor").asInstanceOf[ProcessorTrait].processModels(sens))
                }
                catch {
                  case e: ProcessorException => {
                    packs.processed.append(sens.clone().asInstanceOf[SensorModel])
                    sens.data = ""
                    packs.errors.append(e.getMessage())
                  }
                }
              }
              case _ => {}
            }
          })
        })

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