package io.bigsense;


import org.aspectj.lang.ProceedingJoinPoint;
import java.beans.MethodDescriptor;
import java.io.FileWriter;
import java.lang.reflect.Array;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public aspect AspectLogger {

  private long start;

  pointcut conn() : call(public * io.bigsense..*(..)) && !within(AspectLogger) && !within(io.bigsense.server.*);

  before() : conn() {
    Logger log = LoggerFactory.getLogger(thisJoinPoint.getSignature().getDeclaringType());
    log.trace("start " + thisJoinPoint.getSignature().getName() +
        formatArgs(thisJoinPoint.getArgs()));
  }

  after() returning(Object r) : conn() {
    Logger log = LoggerFactory.getLogger(thisJoinPoint.getSignature().getDeclaringType());

    String returnFmt = "";

    if(r == null) {
      returnFmt = "void";
    }
    else if(r.getClass().isArray()) {
      returnFmt = formatArray(r);
    }
    else {
      returnFmt = r.toString();
    }

    log.trace("exit (" + returnFmt + ") " + thisJoinPoint.getSignature().getName());
  }

  after() throwing(Throwable t) : conn() {
    Logger log = LoggerFactory.getLogger(t.getClass());
    log.warn("throw " + t.getMessage(), t);
  }

  /**
   * formats objects into a comma separated list enclosed in parentheses.
   * To be used by before and after returning functions to format arguments for logs.
   * @param args method arguments
   * @return formatted string
   */
  private String formatArgs(Object[] args) {
    String retval = "(";
    for(Object l : args) {

      if(l!= null && l.getClass().isArray()) {
        l = formatArray(l);
      }

      retval += l + ",";
    }
    if(retval.endsWith(",")) {
      retval = retval.substring(0,retval.length()-1); //remove last comma
    }
    retval += ")";
    return retval;
  }

  /**
   * formats an array into individual comma separated elements enclosed in braces and prefixed with type and size.
   * @param returnValue array to format into a human readable string for logging.
   * @return String in the format <class>[size]{element1,element2,...}
   */
  private String formatArray(Object returnValue) {
    String fmtReturn = "";
    fmtReturn = returnValue.getClass() + "[" + Array.getLength(returnValue) + "] {";

    //This is kinda horrible, but I need to deal with primitive types.
    //If you can think of a better way, please fix this.
    if(returnValue.getClass().getComponentType().isPrimitive()) {
      if(returnValue instanceof int[]) {
        for(int o : (int[]) returnValue) {
          fmtReturn += o + ",";
        }
      }
      if(returnValue instanceof byte[]) {
        for(byte o : (byte[]) returnValue) {
          fmtReturn += o + ",";
        }
      }
      if(returnValue instanceof short[]) {
        for(short o : (short[]) returnValue) {
          fmtReturn += o + ",";
        }
      }
      if(returnValue instanceof long[]) {
        for(long o : (long[]) returnValue) {
          fmtReturn += o + ",";
        }
      }
      if(returnValue instanceof float[]) {
        for(float o : (float[]) returnValue) {
          fmtReturn += o + ",";
        }
      }
      if(returnValue instanceof double[]) {
        for(double o : (double[]) returnValue) {
          fmtReturn += o + ",";
        }
      }
      if(returnValue instanceof boolean[]) {
        for(boolean o : (boolean[]) returnValue) {
          fmtReturn += o + ",";
        }
      }
      if(returnValue instanceof char[]) {
        for(char o : (char[]) returnValue) {
          fmtReturn += o + ",";
        }
      }
    }
    else {
      for(Object o : (Object[]) returnValue) {
        fmtReturn += o + ",";
      }
    }

    if(fmtReturn.endsWith(",")) {
      fmtReturn = fmtReturn.substring(0,fmtReturn.length()-1); //remove last comma
    }
    fmtReturn += "}";
    return fmtReturn;
  }
}