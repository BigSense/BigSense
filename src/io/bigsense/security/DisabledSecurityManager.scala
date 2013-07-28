package io.bigsense.security

import io.bigsense.action.ActionRequest

class DisabledSecurityManager extends SecurityManagerTrait {

  def securityFilter(request: ActionRequest): Boolean = { true }

}