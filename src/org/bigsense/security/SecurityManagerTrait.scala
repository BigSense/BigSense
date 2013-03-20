package org.bigsense.security
import org.bigsense.action.ActionRequest
import scala.reflect.BeanProperty
import org.bigsense.db.ServiceDataHandlerTrait

trait SecurityManagerTrait {

  def securityFilter(request : ActionRequest) : Boolean
  @BeanProperty var dbHandler : ServiceDataHandlerTrait = _
}