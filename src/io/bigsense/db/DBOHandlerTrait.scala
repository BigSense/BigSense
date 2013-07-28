package io.bigsense.db

import java.io.File

trait DBOHandlerTrait extends DataHandlerTrait {
  
  
  /**
   * updates the database schema to the latest version available.
   *  
   * Should be called only once on startup to update schema. Depends on
   * the env and ddlResource properties being correctly set.
   */
  def updateSchema() : Unit
    
  /**
   * get current database schema version
   */
  def getCurrentVersion() : Int

}