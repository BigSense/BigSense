package io.bigsense.smartvision

import java.awt.image.BufferedImage
import ij.ImagePlus
import org.apache.log4j.Logger

class WaterLevelAnalyzer extends PhotoAnalyzerTrait {
  
  private var log = Logger.getLogger(getClass())

  def processImage(image: BufferedImage): PhotoAnalysisResult = {
    
    //Transform standard Java BufferedImage into ImageJ ImagePlus object
    val ijImage : ImagePlus = new ImagePlus("ImportedImage",image)
    
    var ret = new PhotoAnalysisResult() //Prepare Return value
    
    //Do Analysis Here, Store results

    ret.width = ijImage.getDimensions()(0)
    ret.height = ijImage.getDimensions()(1)
    ret.barcode = "PHOTO-XX1232SomeTranslatedBarcode"
    ret.units = "mm"
    ret.data = "2.52"
    ret.stype = "WaterLevel"
      
    //If an error occurs, throw it like so. The SensorAction will catch it
    // and store it in the Errors table, discarding this entry
    
    //throw new SmartVisionException("Error Message Goes Here")
      
    
    //Return Results
    ret
  }

}