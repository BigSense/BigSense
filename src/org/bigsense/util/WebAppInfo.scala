package org.bigsense.util

object WebAppInfo {

  /**
   * the full path to the application context including hostname.
   * Set during the bootstrap phase in the MasterServlet.
   */
  var contextPath : String = _
  
  var servletPath : String = _
}