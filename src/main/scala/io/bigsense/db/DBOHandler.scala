package io.bigsense.db

import java.io.InputStream
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.Resource
import io.bigsense.server.BigSenseServer
import io.bigsense.util.IO.using


class DBOHandler extends DBOHandlerTrait {

  /**
   * class resource path to DDL files.
   */
  var ddlResource : String = ""
    
  
  def updateSchema() : Unit = {
    val ddlFiles = getDDLList()   
    using(ds.getConnection()) { conn =>
      val currentVersion = getCurrentVersion()
      log.info("Current Version %d".format(currentVersion))
	    for(i <- currentVersion+1 to ddlFiles.size) {
	      log.info("Processing Scheme File %s".format(ddlFiles(i).getFilename))
	      for(stmts <- getStatements(ddlFiles(i).getInputStream)) {
          if(!stmts.trim().equals(""))  {  //ignore blank lines
	         log.info("Running %s".format(stmts))
	         conn.prepareStatement(stmts).execute()
          }
	      }
	      updateVersion(i)
	    }
    }
  }
  
  def getCurrentVersion() : Int = {
    using(ds.getConnection()) { conn =>
      val req : DBRequest = new DBRequest(conn,"currentDDLVersion")
      val results = runQuery(req).results
      results.length match {
        case 1 => {
          results(0)("version").asInstanceOf[Int]
        }
        case 0 => { 0 }
        case _ => { throw new DatabaseException("Multiple Results for Single Row Operation") }
      }
    }    
  }
  
  
  /**
   * returns a list of available SQL files, excluding the bootstrap file (000) in 
   * installation order.
   * 
   * All SQL files for database construction are contained within the ddl/<database backend> direcotry.
   * Currently only MSSQL is supported. The format for naimg the files is as such:
   * 
   * ###-Name.sql
   * 
   * Example:
   * 
   * 002-SomeFeature.sql
   *
   * @return Array of File objects representing SQL statements to be run in order
   */
  private def getDDLList() : Map[Int,Resource] = {
    val retval =  scala.collection.mutable.Map[Int,Resource]()

    log.debug(s"DDL Resources: $ddlResource")
    for(resource <- new PathMatchingResourcePatternResolver().getResources(ddlResource)) {

      val parts = resource.getFilename().split("-")
      log.debug(s"DDL File ${resource.getFilename}")

      if(parts(0).toInt != 0) { //skip 000, bootstrap file me be installed manually
          retval.put(parts(0).toInt,resource)
      }
    }
    retval.toMap[Int,Resource]
  }
  
  /**
   * returns all the SQL statements from a file, separated by semicolons (;).
   * Substitution for database user names occurs here. Only needed for
   * postgres since it requires permissions per table.
   * @param sql SQL File to read 
   * @return Array of SQL statements as strings
   */
  private def getStatements(sql : InputStream) : Array[String] =
    using(scala.io.Source.fromInputStream(sql)) {
      _.mkString.replace("${dbUser}", BigSenseServer.config.options("dbUser")).split(";")
    }
  
  /**
   * updates schema information table to indicate the latest schema version installed.
   */
  private def updateVersion(version : Int) = {
    using(ds.getConnection()) { conn =>
      val req : DBRequest = new DBRequest(conn,"updateDDLVersion")
      req.args = List(version)
      runQuery(req)
    }
  }
  
  
}