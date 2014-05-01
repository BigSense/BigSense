
package io.bigsense.util

import java.io.{ByteArrayOutputStream, BufferedInputStream, FileInputStream, File}
import org.apache.commons.compress.archivers.tar.{TarArchiveEntry, TarArchiveInputStream}
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import io.bigsense.spring.MySpring
import org.apache.commons.compress.utils.IOUtils
import io.bigsense.format.{FormatTrait, AgraDataXMLFormat}
import scala.collection.mutable.ListBuffer
import io.bigsense.model.{DataModel, ModelTrait}
import io.bigsense.db.ServiceDataHandlerTrait
import java.util.{Calendar, TimeZone}


object BulkBZip2DataLoader {


  val BUFFER_SIZE = 4096

  def load(file : String, chunkSize : Long, minYear : Option[Int] = None)  {

    val loader = MySpring.getObject("FormatAGRA.XML").asInstanceOf[FormatTrait]
    val db     = MySpring.getObject("serviceDataHandler").asInstanceOf[ServiceDataHandlerTrait]

    val bzin = new TarArchiveInputStream(new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(file))))
    val models = new ListBuffer[ModelTrait]()
    var chunks = 0

    Stream.continually(bzin.getNextEntry()).takeWhile(_ != null) foreach {
      entry => {
        if(entry.asInstanceOf[TarArchiveEntry].isFile()) {

          val xmlfile = new ByteArrayOutputStream()
          IOUtils.copy(bzin,xmlfile)
          println(String.format("Processing Entry %s",entry.getName));

          models.appendAll(
            loader.loadModels(new String(xmlfile.toByteArray()))
              .filter( p => p.isInstanceOf[DataModel])
              .filter( q => {
                minYear match {
                  case Some(year) => TimeHelper.yearFromTimestamp(q.asInstanceOf[DataModel].timestamp)>= year
                  case None => true
                }
              })
          )

          xmlfile.close()

          chunks = chunks + 1
          if( chunks % chunkSize == 0) {
            println("Sending batch of %d to database".format(chunkSize))
            db.loadData(models.toList.asInstanceOf[List[DataModel]])
            models.clear()
          }
        }
      }
    }
  }

}