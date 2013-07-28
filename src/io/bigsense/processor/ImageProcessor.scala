package io.bigsense.processor

import scala.collection.immutable.List
import io.bigsense.model.SensorModel
import org.apache.commons.codec.binary.Base64
import javax.imageio.ImageIO
import java.io.ByteArrayInputStream
import io.bigsense.smartvision.AnalysisException
import io.bigsense.smartvision.PhotoAnalysisResult
import io.bigsense.smartvision.PhotoAnalyzerTrait
import io.bigsense.spring.MySpring


class ImageProcessor extends ProcessorTrait {

  def processModels(sensor : SensorModel) : SensorModel  = {
    //return a copy
    val orig = sensor.clone().asInstanceOf[SensorModel]

    var analysis : PhotoAnalysisResult = null
    try {
      val image = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(sensor.data)))
      //TODO: Dynmically select which Analyzer to use based on some identifier
      analysis = MySpring.getObject("AnalyzerWaterLevel").asInstanceOf[PhotoAnalyzerTrait].processImage(image)
    }
    catch {
      case e:AnalysisException => { throw new ProcessorException(e.getMessage()) }
    }
    
    //modify the original
    sensor.data = analysis.data
    //leave this out for now until ... need to adjust this in SmartVisit lib for Victor
    //sensor.uniqueId = analysis.barcode
    sensor.units = analysis.units
    sensor.stype = analysis.stype
    
    orig
  }
}