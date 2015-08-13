package io.bigsense.security
import io.bigsense.action.ActionRequest
import io.bigsense.db.ServiceDataHandlerTrait

trait SecurityManagerTrait {

  def securityFilter(request : ActionRequest) : Boolean
  var dbHandler : ServiceDataHandlerTrait = _
}