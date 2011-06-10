package org.penguindreams.greenstation.servlet

import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}

class MasterServlet extends HttpServlet {

  override def service(req : HSReq, resp: HSResp) = {
    var ops = getPath(req);
    req.setAttribute("hi","someValue");
    req.getRequestDispatcher("./test.jsp").forward(req,resp)
    //resp.getWriter().write("<html><body>%s</body></html>".format(ops(0)))
  }
    
  def getPath(req: HSReq) =
    req.getRequestURI().substring( req.getContextPath().length() + req.getServletPath().length() ).split("/");
}