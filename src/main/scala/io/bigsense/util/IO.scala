package io.bigsense.util

/**
 * Created by cassius on 19/08/15.
 */
object IO {

  //Taken From: http://zcox.wordpress.com/2009/08/17/simple-jdbc-queries-in-scala/
  def using[Closeable <: {def close(): Unit}, B](closeable: Closeable)(getB: Closeable => B): B =
    try {
      getB(closeable)
    } finally {
      try { closeable.close() } catch { case e:Exception => {  } }
    }

}
