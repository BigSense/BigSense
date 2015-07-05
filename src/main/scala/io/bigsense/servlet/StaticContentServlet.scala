package io.bigsense.servlet

import javax.servlet.http.{HttpServletRequest, HttpServletResponse, HttpServlet}
import javax.activation.FileTypeMap
import com.google.common.io.ByteStreams
import java.net.URLConnection

import org.slf4j.LoggerFactory

/**
 * needed to serve static resources for Tomcat. Not needed for Jetty
 *
 * Created by cassius on 29/04/14.
 */
class StaticContentServlet extends HttpServlet {

  val log = LoggerFactory.getLogger(this.getClass())

  override def doGet(req : HttpServletRequest, resp : HttpServletResponse) {

    val resourcePath = "/io/bigsense/web/%s".format(req.getPathInfo.stripPrefix("/"))

    val resource = getClass.getResource(resourcePath)

    log.debug("Requesting static resource %s".format(resourcePath))

    if(resource == null) {
      resp.setContentType("text/plain")
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND)
      resp.getWriter.write("Not Found")
      resp.getWriter.close
    }
    else {
      resp.setContentType( getContentType(req.getPathInfo) )
      ByteStreams.copy(getClass.getResourceAsStream(resourcePath), resp.getOutputStream)
      resp.getOutputStream.close
    }
  }

  def getContentType(fileName : String) = {
    fileName match {
      case x if x endsWith "js" => "application/javascript"
      case x if x endsWith "css" => "text/css"
      case _ => Option[String](URLConnection.guessContentTypeFromName(fileName)) match {
        case Some(s) => s
        case None => FileTypeMap.getDefaultFileTypeMap().getContentType(fileName)
      }
    }

  }


}
