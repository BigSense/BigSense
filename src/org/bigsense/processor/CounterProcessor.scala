package org.bigsense.processor

import scala.collection.immutable.List
import org.bigsense.model.SensorModel

class CounterProcessor extends ProcessorTrait {

  def processModels(sensor : SensorModel) : SensorModel  = {
    val orig = sensor.clone().asInstanceOf[SensorModel]

    
    
    orig
  }

}