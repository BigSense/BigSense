package io.bigsense.smartvision

import ij.ImagePlus
import ij.io.Opener
import javax.imageio.ImageIO
import java.io.ByteArrayOutputStream
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import org.apache.log4j.Logger
import javax.imageio.ImageWriter
import javax.imageio.IIOImage

trait PhotoAnalyzerTrait {

  
  def processImage(image : BufferedImage) : (PhotoAnalysisResult)
  
}