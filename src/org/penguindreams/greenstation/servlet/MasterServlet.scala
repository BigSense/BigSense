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
import org.penguindreams.greenstation.format.{FormatTrait => Format}
import org.penguindreams.greenstation.util.HttpUtil
import org.apache.log4j.Logger
import org.penguindreams.greenstation.model.DataModel




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
        var format = MySpring.getObject("Format"+getExtension(req).toUpperCase).asInstanceOf[Format]   

        var data = HttpUtil.pullBody(req)
        var models : List[DataModel] = null;
        if(data != null) {
          models = format.loadModels(data);
        }
        
    	var aResp : ActionResponse = action.runAction(req.getMethod,ops,mapAsScalaMap(req.getParameterMap()),models,format)    	
    }
    catch {
       case e:NoSuchBeanDefinitionException => {         
         log.warn("Invalid incoming request",e)
         req.setAttribute("message","Bad Request")
         resp.setStatus(HSResp.SC_BAD_REQUEST)
         view("error",req,resp)
       }
       case e:Throwable => {
         log.error("Internal failure",e)
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
     .stripPrefix("/").stripSuffix("/").split("/")
}