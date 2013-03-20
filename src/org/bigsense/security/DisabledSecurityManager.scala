package org.bigsense.security

import org.bigsense.action.ActionRequest

class DisabledSecurityManager extends SecurityManagerTrait {

  def securityFilter(request: ActionRequest): Boolean = { true }

}