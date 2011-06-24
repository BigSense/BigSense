package org.penguindreams.greenstation.spring;

/**
*
* Basic Logging Interceptor to be used with Spring or Aspect J style point-cuts.
*
*
*
* @author Sumit Khanna - PenguinDreams.org
*/


import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

/**
* This class is designed to be used with Spring's AOP model to automatically log information.
* All beans that are defined with proxies in the application context and that match the
* appropriate point-cuts, or all beans that are auto-proxied, will have the following
* functions called around (before and after) method calls to those beans.
*/
public class LoggingInterceptor implements AfterReturningAdvice, ThrowsAdvice,MethodBeforeAdvice {

    private Logger getLogger (Class subclass) {
        return Logger.getLogger(subclass);
    }
    
    //-- AOP Observer Functions
    
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        Logger log = getLogger(method.getDeclaringClass());
        
        if(returnValue == null) { returnValue = "void"; } //makes more sense in the logs
        
        //array formatting
        if(returnValue.getClass().isArray()) {
            returnValue = formatArray(returnValue);
        }
        
        log.trace("exit " + "(" + returnValue + ") " + method.getName() + formatArgs(args)+ " ["+target+"] " );
    }

    public void before(Method method, Object[] args, Object target) throws Throwable {
        Logger log = getLogger(method.getDeclaringClass());
        log.trace("start " + method.getName() + formatArgs(args) + " ["+target+"]");
    }
    
    public void afterThrowing(Throwable throwable) {
        Logger log = getLogger(throwable.getClass());
        log.warn("throw " + throwable.getMessage(),throwable);
        
        /*if(throwable instanceof SimpleException) {
            innerRenderingExceptionLogger(log, ((SimpleException)throwable).getNestedException(),1);
        }*/
    }
    
    //Helper Functions
    
    /**
* recursive function for logging all nested inner exceptions
* @param r inner exception
* @param level should be set to 1 on initial call. Incremented each iteration
*/
    private void innerRenderingExceptionLogger(Logger log, Throwable r, int level) {
        log.warn("\tinner("+level+") " + r.getMessage(),r);
        /*if(r instanceof SimpleException) {
            innerRenderingExceptionLogger(log, ((SimpleException)r).getNestedException(),++level);
        }*/
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
    private Object formatArray(Object returnValue) {
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