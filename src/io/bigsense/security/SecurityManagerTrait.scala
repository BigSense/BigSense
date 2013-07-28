package io.bigsense.security
import io.bigsense.action.ActionRequest
import scala.reflect.BeanProperty
import io.bigsense.db.ServiceDataHandlerTrait

trait SecurityManagerTrait {

  def securityFilter(request : ActionRequest) : Boolean
  @BeanProperty var dbHandler : ServiceDataHandlerTrait = _
}