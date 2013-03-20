package org.bigsense.processor
import scala.reflect.BeanProperty
import org.bigsense.db.DataHandlerTrait
import org.bigsense.model.SensorModel

trait ProcessorTrait {
  
  	  @BeanProperty var dbHandler : DataHandlerTrait = _
  	  
  	  /**
  	   * Analyzes the current model and replaces values with processed information while returning a copy of the original.
  	   * Since the incoming sensor comes from an immutable list, which will be inserted into the database,
  	   * the returned SensorModel should contain the original input data while the input should be modified with 
  	   * data to be inserted into the sensor_data table. 
  	   * 
  	   */
  	  @throws(classOf[ProcessorException])
  	  def processModels(sensor : SensorModel) : SensorModel 
}