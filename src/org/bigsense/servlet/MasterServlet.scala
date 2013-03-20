/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package org.bigsense.servlet

import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}
import org.bigsense.spring.MySpring
import org.bigsense.action.ActionTrait
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.bigsense.action.ActionResponse
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.Map
import java.util.Properties
import java.io.BufferedReader
import org.apache.log4j.PropertyConfigurator
import org.bigsense.format.FormatTrait
import org.bigsense.util.HttpUtil
import org.apache.log4j.Logger
import org.bigsense.model.DataModel
import org.bigsense.db.DatabaseException
import org.bigsense.action.ActionRequest
import org.bigsense.model.ModelTrait
import org.bigsense.validation.ValidationError
import org.bigsense.format.UnsupportedFormatException
import org.bigsense.format.NoFormat
import org.bigsense.util.WebAppInfo
import org.bigsense.security.SecurityManagerTrait
import org.bigsense.security.SecurityManagerException


class MasterServlet extends HttpServlet {
  
  def view(view : String, req : HSReq, resp: HSResp) = {
    getServletContext().getRequestDispatcher("/%s.jsp".format(view)).forward(req,resp)
  }

  
  override def service(req : HSReq, resp: HSResp) = {    
    
    var log : Logger = Logger.getLogger(this.getClass());
    try {      
        //main entry point - bootstrapping
        
        //Setup basic hostname info for use downsteram
        WebAppInfo.contextPath = if(req.isSecure()) "https" else "http" + "://" + 
	    		    req.getServerName + 
	    		    (if (req.getServerPort != 80) ":%s".format(req.getServerPort()) else "")  +
	    		    req.getContextPath() 
	    WebAppInfo.servletPath = WebAppInfo.contextPath + req.getServletPath()		  
        
        //construct request object
        var aReq = new ActionRequest()        
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
        aReq.parameters = mapAsScalaMap(req.getParameterMap().asInstanceOf[java.util.Map[String,Array[String]]]).toMap
        
        
        //Action
    	var action : ActionTrait = MySpring.getObject("Action"+aReq.args(0)).asInstanceOf[ActionTrait]
        
        //Format isn't required, for everything. NoFormat indicates none was specified.         
        //TODO: Add "unknown format" and differentitate between the two
        aReq.format = {
    	  try {
            MySpring.getObject("Format"+getExtension(req).toUpperCase).asInstanceOf[FormatTrait]
    	  }
    	  catch {
    		case e:NoSuchBeanDefinitionException => { new NoFormat() }
    	  }
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
        
        
        //Security manager
        var security : SecurityManagerTrait = MySpring.getObject("SecurityManager").asInstanceOf[SecurityManagerTrait]
        if(!security.securityFilter(aReq)) {
          throw new SecurityManagerException("Could not verify Signature")
        }

       
        
        //validation
        if( action.validator.validateRequest(aReq) match {
            case None => { false }
            case Some(error: ValidationError) => {
	            resp.setStatus(error.statusCode)
	            req.setAttribute("message",error.errorString)
	            view("error",req,resp)
	            true
            }
          }
        ) {}
        else {          
        	//run action
	    	var aResp : ActionResponse = action.runAction(aReq)
	    	
	    	//Headers
	    	resp.setStatus(aResp.status)
	    	for( loc <- aResp.newLocations ) yield {
	    		resp.addHeader("Location",
	    		    WebAppInfo.servletPath + '/'+ aReq.args(0) + '/' + loc + '.' + getExtension(req))
	    	}
	    	aResp.contentType match {
	    	  case Some(contentType : String) => { resp.setContentType(contentType) }
	    	  case None => {}
	    	}
	    	
	    	//Body
	    	aResp.view match {
	    	  case None => {
	    	    aResp.binaryOutput match {
	    	      case true =>  {resp.getOutputStream().write(aResp.binary)}
	    	      case false => {resp.getOutputStream().print(aResp.output)}
	    	    }
	    		    
	    	  }
	    	  case Some(viewStr: String) => {
	    		  for( (key,data) <- aResp.viewData) yield {
	    			  req.setAttribute(key,data)
	    		  }
	    		  view(viewStr,req,resp)
	    	  }
	    	}
        }
    }
    catch {
       case e:DatabaseException => {
         log.error("Database Error",e)
         req.setAttribute("message",e.getMessage())
         resp.setStatus(HSResp.SC_INTERNAL_SERVER_ERROR)
         view("error",req,resp)
       }
       case e:NoSuchBeanDefinitionException => {         
         log.warn("Invalid Incoming Request",e)
         req.setAttribute("message","Bad Request")
         resp.setStatus(HSResp.SC_BAD_REQUEST)
         view("error",req,resp)
       }
       case e:UnsupportedFormatException => {
         log.warn("Request Made for Unsupported Format",e)
         req.setAttribute("message",e.getMessage())
         resp.setStatus(HSResp.SC_BAD_REQUEST)
         view("error",req,resp)
       }
       case e:SecurityManagerException => {
         log.warn("Security Manager Exception",e)
         req.setAttribute("message",e.getMessage())
         resp.setStatus(HSResp.SC_UNAUTHORIZED)
         view("error",req,resp)
       }
       case e:Throwable => {
         log.error("Internal Failure",e)
         req.setAttribute("message","Internal Server Error")
         resp.setStatus(HSResp.SC_INTERNAL_SERVER_ERROR)
         view("error",req,resp)
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
