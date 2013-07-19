package org.bigsense.util


object Numbers {

  def toLong(a: Any) : Long = {
    a match {
      case v:java.lang.Integer => v.intValue()
      case v:java.lang.Long => v.longValue()
    }
  }

}