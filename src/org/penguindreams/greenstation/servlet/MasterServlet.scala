package org.penguindreams.greenstation.servlet

import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}
import org.penguindreams.greenstation.spring.{MySpring => Spring}
import org.penguindreams.greenstation.action.{ActionTrait => Action}
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.penguindreams.greenstation.action.{ActionResponse => Response}
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.Map
import java.util.Properties
import java.io.BufferedReader
import org.apache.log4j.PropertyConfigurator
import org.penguindreams.greenstation.format.{FormatTrait => Format}
import org.penguindreams.greenstation.model.{ModelTrait => Model}
import org.penguindreams.greenstation.util.HttpUtil




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
    
    try {
        //main entry point - bootstrapping
        loadLogger()
        var ops = getPath(req)
    	var action : Action = Spring.getObject("Action"+ops(0)).asInstanceOf[Action]    
        //var format = Spring.getObject("Format"+getExtension(req).toUpperCase).asInstanceOf[Format]
        var format = null 
        var data = req.getParameter("data")
        var data = HttpUtil.pullyBody(req)
        var model : Model = null;
        if(data != null) {
          //model = format.loadModel(req.getParameter("data"));
        }
        
    	var aResp : Response = action.runAction(req.getMethod,ops,mapAsScalaMap(req.getParameterMap()),model,format)    	
    }
    catch {
       case e:NoSuchBeanDefinitionException => { 
         req.setAttribute("message","Bad Request")
         resp.setStatus(HSResp.SC_BAD_REQUEST)
         view("error",req,resp)
       }      
    }

  }
  
  def getExtension(req: HSReq) = req.getRequestURI().split(".").tail(1).mkString(".")
  
  def getPath(req: HSReq) =
    req.getRequestURI()
     .substring( req.getContextPath().length() + req.getServletPath().length() )
     .stripPrefix("/").stripSuffix("/").split("/")
}