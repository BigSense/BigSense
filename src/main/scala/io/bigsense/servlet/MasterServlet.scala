/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.servlet

import javax.servlet.http.{HttpServletRequest => HSReq, HttpServletResponse => HSResp, HttpServlet}
import io.bigsense.server.BigSenseServer
import io.bigsense.spring.MySpring
import io.bigsense.action._
import scala.collection.JavaConversions.mapAsScalaMap
import io.bigsense.format.FormatTrait
import io.bigsense.util.HttpUtil
import org.apache.log4j.Logger
import io.bigsense.model.DataModel
import io.bigsense.db.DatabaseException
import io.bigsense.validation.ValidationError
import io.bigsense.format.UnsupportedFormatException
import io.bigsense.format.NoFormat
import io.bigsense.util.WebAppInfo


class MasterServlet extends HttpServlet {

  override def service(req : HSReq, resp: HSResp) = {    
    
    val log : Logger = Logger.getLogger(this.getClass())

    def err(msg : String) = resp.getOutputStream.print(html.error.render(msg).toString)

    try {      
        //main entry point - bootstrapping

        //Setup basic hostname info for use downsteram
        WebAppInfo.contextPath = if(req.isSecure()) "https" else "http" + "://" + 
	    		    req.getServerName + 
	    		    (if (req.getServerPort != 80) ":%s".format(req.getServerPort()) else "")  +
	    		    req.getContextPath() 
	      WebAppInfo.servletPath = WebAppInfo.contextPath + req.getServletPath()
        
        //construct request object
        val aReq = new ActionRequest()
        aReq.method = req.getMethod()
        aReq.args = getPath(req)
        
        //pull body and split into body and signature (if it exists)
        val dataParts = HttpUtil.pullBody(req).split("\n\n");
        if(dataParts.length >= 2) {
          aReq.data = dataParts(0)
          aReq.signature = Some(dataParts(1))
        }
        else {
          aReq.data = dataParts(0)
        }

        //parameters
        aReq.parameters = mapAsScalaMap(req.getParameterMap().asInstanceOf[java.util.Map[String,Array[Any]]]).toMap
        
        
        //Action
        MySpring.getAction(aReq.args(0)) match {
          case Some(action : ActionTrait) => {

            //Format isn't required, for everything. NoFormat indicates none was specified.
            //TODO: Add "unknown format" and differentitate between the two
            aReq.format = MySpring.getFormat(getExtension(req)) match {
              case Some(format : FormatTrait) => format
              case None => new NoFormat
            }

            //load models
            aReq.models = {
              aReq.data match {
                case "" => { List() }
                case _ => {
                  aReq.format.loadModels(aReq.data).asInstanceOf[List[DataModel]]
                }
              }
            }

            //security
            if(MySpring.securityManager.securityFilter((aReq))) {

              //read only mode check
              if( aReq.method == "GET" || !BigSenseServer.config.options("readOnly").toBoolean) {

                // validation
                action.validator.validateRequest(aReq) match {
                  case None => {
                    //run action
                    val aResp: Response = action.runAction(aReq)

                    //Headers
                    resp.setStatus(aResp.status)
                    for (loc <- aResp.newLocations) yield {
                      resp.addHeader("Location",
                        WebAppInfo.servletPath + '/' + aReq.args(0) + '/' + loc + '.' + getExtension(req))
                    }
                    aResp.contentType match {
                      case Some(contentType: String) => {resp.setContentType(contentType)}
                      case None => {}
                    }

                    //Body
                    aResp match {
                      case bin: BinaryResponse => resp.getOutputStream.write(bin.output)
                      case str: StringResponse => resp.getOutputStream.print(str.output)
                      case vue: ViewResponse => resp.getOutputStream.print(vue.view.body)
                    }
                  }
                  // Errors
                  case Some(error: ValidationError) => {
                    resp.setStatus(error.statusCode)
                    err(error.errorString)
                  }
                }
              }
              else {
                resp.setStatus(HSResp.SC_FORBIDDEN)
                log.info("Blocked Non GET Request (read-only mode)")
                err("Server in read-only mode")
              }
            }
            else {
              log.warn("Security Manager Failed")
              resp.setStatus(HSResp.SC_UNAUTHORIZED)
              err("Could not verify Signature")
            }
          }
          case None => {
            log.warn(s"Invalid Incoming Request ${aReq.args(0)}")
            resp.setStatus(HSResp.SC_BAD_REQUEST)
            err("Bad Request")
          }
        }
    }
    catch {
       case e:DatabaseException => {
         log.error("Database Error",e)
         resp.setStatus(HSResp.SC_INTERNAL_SERVER_ERROR)
         err(e.getMessage)
       }
       case e:UnsupportedFormatException => {
         log.warn("Request Made for Unsupported Format",e)
         resp.setStatus(HSResp.SC_BAD_REQUEST)
         err(e.getMessage)
       }
       case e:Throwable => {
         log.error("Internal Failure",e)
         resp.setStatus(HSResp.SC_INTERNAL_SERVER_ERROR)
         err("Internal Server Error")
       }
    }
  }

  def getExtension(req: HSReq) = req.getRequestURI().split("\\.").drop(1).mkString(".")

  def getPath(req: HSReq) =
    req.getRequestURI()
     .substring( req.getContextPath().length() + req.getServletPath().length() )
     .split("\\.")(0)
     .stripPrefix("/").stripSuffix("/").split("/")
}
