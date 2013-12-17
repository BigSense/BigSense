
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


object BulkBZip2DataLoader {


  val BUFFER_SIZE = 4096
  val PACKAGE_CHUNK_SIZE = 10000

  def main(args : Array[String])  {

    if(args.length != 1) {
      System.err.println("Usage: %s [bzip2 file]".format(this.getClass))
      System.exit(1)
    }

    val loader = MySpring.getObject("FormatAGRA.XML").asInstanceOf[FormatTrait]
    val db     = MySpring.getObject("serviceDataHandler").asInstanceOf[ServiceDataHandlerTrait]

    val bzin = new TarArchiveInputStream(new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(args(0)))))
    var entry : TarArchiveEntry = null;
    val models = new ListBuffer[ModelTrait]()
    var chunks = 0

    Stream.continually(bzin.getNextEntry().asInstanceOf[TarArchiveEntry]).takeWhile(_ != null) foreach {
      entry => {
        if(entry.isFile()) {

          val xmlfile = new ByteArrayOutputStream()
          IOUtils.copy(bzin,xmlfile)
          models.appendAll( loader.loadModels(new String(xmlfile.toByteArray())) )

          chunks = chunks + 1
          if( chunks % PACKAGE_CHUNK_SIZE == 0) {
            System.out.println("Sending batch of %d to database".format(PACKAGE_CHUNK_SIZE))
            db.loadData(models.toList.asInstanceOf[List[DataModel]])
            models.clear()
          }
        }
      }
    }
    System.out.println("Finished Processing File");

  }
}