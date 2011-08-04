package org.penguindreams.greenstation.servlet

import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}
import org.penguindreams.greenstation.spring.MySpring
import org.penguindreams.greenstation.action.ActionTrait
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.penguindreams.greenstation.action.ActionResponse
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.Map
import java.util.Properties
import java.io.BufferedReader
import org.apache.log4j.PropertyConfigurator
import org.penguindreams.greenstation.format.FormatTrait
import org.penguindreams.greenstation.util.HttpUtil
import org.apache.log4j.Logger
import org.penguindreams.greenstation.model.DataModel
import org.penguindreams.greenstation.db.DatabaseException
import org.penguindreams.greenstation.action.ActionRequest




class MasterServlet extends HttpServlet {
  
  def view(view : String, req : HSReq, resp: HSResp) = {    
    getServletContext().getRequestDispatcher("/%s.jsp".format(view)).forward(req,resp)
  }

  private def loadLogger() = {
    var props = new Properties();
    props.load(this.getClass().getResourceAsStream("/org/penguindreams/greenstation/spring/log4j.properties"))
    PropertyConfigurator.configure(props)    
  }
  
  override def service(req : HSReq, resp: HSResp) = {    
    
    var log : Logger = Logger.getLogger(this.getClass());
    try {      
        //main entry point - bootstrapping
        loadLogger()
        
        var ops = getPath(req)
    	var action : ActionTrait = MySpring.getObject("Action"+ops(0)).asInstanceOf[ActionTrait]    
        var format = MySpring.getObject("Format"+getExtension(req).toUpperCase).asInstanceOf[FormatTrait]   

        var data = HttpUtil.pullBody(req)
        var models : List[DataModel] = null;
        if(data != null) {
          models = format.loadModels(data);
        }
        
        var aReq = new ActionRequest()
        aReq.method = req.getMethod()
        aReq.args = ops
        aReq.parameters = mapAsScalaMap(req.getParameterMap()).toMap
        aReq.models = models
        aReq.format = format
    	var aResp : ActionResponse = action.runAction(aReq)
    	
    	//Headers
    	resp.setStatus(aResp.status)
    	for( loc <- aResp.newLocations ) yield {
    		resp.addHeader("Location",
    		    if(req.isSecure()) "https" else "http" + "://" + 
    		    req.getServerName + 
    		    (if (req.getServerPort != 80) ":%s".format(req.getServerPort()) else "")  +
    		    req.getContextPath() + 
    		    req.getServletPath() + '/'+ ops(0) + '/' + loc + '.' + getExtension(req))
    	}
    	
    	//Body
    	if(aResp.view != null) {
    	  view(aResp.view,req,resp) 
    	}
    	else {
    	  resp.getOutputStream().print(aResp.output)
    	}
    	
    }
    catch {
       case e:DatabaseException => {
         log.error("Database Error",e)
         req.setAttribute("message",e.getMessage())
         resp.setStatus(HSResp.SC_INTERNAL_SERVER_ERROR)
       }
       case e:NoSuchBeanDefinitionException => {         
         log.warn("Invalid Incoming Request",e)
         req.setAttribute("message","Bad Request")
         resp.setStatus(HSResp.SC_BAD_REQUEST)
       }
       case e:Throwable => {
         log.error("Internal Failure",e)
         req.setAttribute("message","Internal Server Error")
         resp.setStatus(HSResp.SC_INTERNAL_SERVER_ERROR)
       }
       view("error",req,resp)
    }

  }
  
  def getExtension(req: HSReq) = req.getRequestURI().split("\\.").drop(1).mkString(".")
  
  def getPath(req: HSReq) =
    req.getRequestURI()
     .substring( req.getContextPath().length() + req.getServletPath().length() )
     .split("\\.")(0)
     .stripPrefix("/").stripSuffix("/").split("/")
}