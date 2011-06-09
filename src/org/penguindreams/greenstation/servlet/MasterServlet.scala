package org.penguindreams.greenstation.servlet

import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}

class MasterServlet extends HttpServlet {

  override def service(req : HSReq, resp: HSResp) =
    resp.getWriter().write("<html><body>%s</body></html>".format(getPath(req)))
    
  def getPath(req: HSReq) =
    req.getRequestURI().substring( req.getContextPath().length() + req.getServletPath().length() );
}