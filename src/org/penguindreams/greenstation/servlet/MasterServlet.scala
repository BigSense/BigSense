package org.penguindreams.greenstation.servlet

import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}
import org.penguindreams.greenstation.spring.MySpring
import org.penguindreams.greenstation.action.ActionTrait
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.penguindreams.greenstation.action.ActionResponse
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.Map

class MasterServlet extends HttpServlet {
  
  def view(view : String, req : HSReq, resp: HSResp) = {
    getServletContext().getRequestDispatcher("/%s.jsp".format(view)).forward(req,resp)
  }
    
  override def service(req : HSReq, resp: HSResp) = {    
    
    try {
        var ops = getPath(req)
    	var action : ActionTrait = MySpring.getObject("Action"+ops(0)).asInstanceOf[ActionTrait]    
    	var aResp : ActionResponse = action.runAction(req.getMethod,ops,mapAsScalaMap(req.getParameterMap()))    	
    }
    catch {
       case e:NoSuchBeanDefinitionException => { 
         req.setAttribute("message","Bad Request")
         resp.setStatus(HSResp.SC_BAD_REQUEST)
         view("error",req,resp)
       }      
    }

  }
    
  def getPath(req: HSReq) =
    req.getRequestURI()
     .substring( req.getContextPath().length() + req.getServletPath().length() )
     .stripPrefix("/").stripSuffix("/").split("/")
}