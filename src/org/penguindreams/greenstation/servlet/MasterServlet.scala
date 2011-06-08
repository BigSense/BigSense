package org.penguindreams.greenstation.servlet

import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}

class MasterServlet extends HttpServlet {

  override def service(req : HSReq, resp: HSResp) =
    resp.getWriter().write("<html><body>Hello World</body></html>")
}