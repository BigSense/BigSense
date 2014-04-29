package io.bigsense.db

import java.io.File
import scala.collection.mutable.ArrayBuffer
import scala.reflect.BeanProperty
import org.apache.commons.io.IOUtils
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.Resource


class DBOHandler extends DBOHandlerTrait {

  /**
   * environment.
   * Examples: development, staging, production. 
   */
  @BeanProperty
  var env : String = "unknown"
  
  /**
   * class resource path to DDL files.
   */
  @BeanProperty
  var ddlResource : String = ""
    
  
  def updateSchema() : Unit = {
    val ddlFiles = getDDLList()   
    using(ds.getConnection()) { conn =>
        val currentVersion = getCurrentVersion()
        log.info("Current Version %d".format(currentVersion))
	    for(i <- currentVersion+1 to ddlFiles.size) {
        //log.debug("List:" + ddlFiles.toString)
	      log.info("Processing Scheme File %s".format(ddlFiles(i).getFilename))
	      	for(stmts <- getStatements(ddlFiles(i).getFile)) {
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
          return results(0)("version").asInstanceOf[Int]
        }
        case 0 => { return 0 }
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
   * ###-[environment,environment]-Name.sql
   * 
   * The environment is optional and, if left out, means the SQL file applies to all environments. 
   * Multiple environments can be specified using a comma. File 000 is ignored as it's bootstraping file
   * that contains passwords and must be setup manually
   * 
   * Example:
   * 
   * 002-[Production]-SomeFeature.sql
   * 
   * @param environemnt an environment string 
   * @return Array of File objects representing SQL statements to be run in order
   */
  private def getDDLList() : Map[Int,Resource] = {
    val retval =  scala.collection.mutable.Map[Int,Resource]()

    for(resource <- new PathMatchingResourcePatternResolver().getResources(ddlResource)) {

      val parts = resource.getFilename().split("-")
      log.trace("File " + resource.getFilename)

      if(parts(0).toInt != 0) { //skip 000, bootstrap file me be installed manually
        //environment specific files
        if(parts(1).startsWith("[")) {
          if(parts(1).contains(env)) {
            retval.put(parts(0).toInt,resource)
          }
        }
        //files without environment specified
        else {
          retval.put(parts(0).toInt,resource)
        }
      }

    }
    retval.toMap[Int,Resource]
  }
  
  /**
   * returns all the SQL statements from a file, separated by semicolons (;)
   * @param sql SQL File to read 
   * @return Array of SQL statements as strings
   */
  private def getStatements(sql : File) : Array[String] = {
    val source = scala.io.Source.fromFile(sql)
    val lines = source.mkString.split(";")
    source.close ()
    lines
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