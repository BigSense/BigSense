package org.penguindreams.greenstation.servlet

import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}

import org.penguindreams.greenstation.spring.MySpring
import org.penguindreams.greenstation.action.ActionTrait

class MasterServlet extends HttpServlet {

  val ACTION_PREFIX = "Action"
  
  override def service(req : HSReq, resp: HSResp) = {    
    var ops = getPath(req)
    
    var action : ActionTrait = MySpring.getObject(ACTION_PREFIX+ops(0)).asInstanceOf[ActionTrait]
    
    action.runAction(req.getMethod,ops,null)
    /*
    req.setAttribute("hi","someValue")
    req.setAttribute("ops",ops);   

    
    
    ops(0) match {
      case "model" =>
        req.setAttribute("hi","MODEL")
      case "game" =>
        req.setAttribute("hi","GAME")
      case x =>
        req.setAttribute("hi","UNKNOWN")
    }

    getServletContext().getRequestDispatcher("/test.jsp").forward(req,resp)
    //resp.getWriter().write("<html><body>123%s</body></html>".format(ops(0)))*/
  }
    
  def getPath(req: HSReq) =
    req.getRequestURI()
     .substring( req.getContextPath().length() + req.getServletPath().length() )
     .stripPrefix("/").stripSuffix("/").split("/")
}