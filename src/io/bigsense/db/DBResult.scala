/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.db

class DBResult {

  var generatedKeys : List[Any] = null 
  var results : List[Map[String,Any]] = null
  
  override def toString() : String = "DBResult: [Keys: %s] - [Reults: %s]".format(generatedKeys,results)  
}