/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package org.bigsense.db
import org.bigsense.model.DataModel
import org.bigsense.model.RelayModel
import java.sql.Connection
import org.bigsense.model.FlatModel
import java.awt.Image
import scala.reflect.BeanProperty
import net.jmatrix.eproperties.EProperties
import org.apache.log4j.Logger
import javax.sql.DataSource
import org.bigsense.conversion.ConverterTrait
import scala.collection.mutable.ListBuffer
import java.sql.Statement
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.io.ByteArrayInputStream


trait DataHandlerTrait {

  @BeanProperty 
  var ds : DataSource = _
    
  @BeanProperty
  var converters : scala.collection.mutable.Map[String,ConverterTrait] = _

  @BeanProperty
  var sqlCommands : EProperties = _
  
  protected var log = Logger.getLogger(getClass())

    //Taken From: http://zcox.wordpress.com/2009/08/17/simple-jdbc-queries-in-scala/
   protected def using[Closeable <: {def close(): Unit}, B](closeable: Closeable)(getB: Closeable => B): B =
    try {
      getB(closeable)
    } finally {
      try { closeable.close() } catch { case e:Exception => {} }
    }
  

  protected def runQuery(req : DBRequest) : DBResult = {    
    
    var retval = new DBResult()
    
    var consBuilder = new StringBuilder(sqlCommands.getProperty(req.queryName))
    
    var paramList : ListBuffer[Any] = new ListBuffer()
    paramList.appendAll(req.args)
                    
    //constraints
    // (we can't use mkstring because we need to deal with the
    //  complex case of if something is an actual constraint (happens in query)
    //  or a conversation (happens row by row)
    var whereAnd = " WHERE "
    for( (para,list) <- req.constraints ) {
      var con = sqlCommands.getProperty("constraint"+para)
      if( (!converters.contains(para)) &&  (con == null || con == "")) {
        throw new DatabaseException("Unknown Constraint: %s".format(para))
      }
      else if( !converters.contains(para) ) {
	      for( l <- list) {          
	        consBuilder.append(whereAnd)
	        consBuilder.append(con)
	        paramList.append(l)
	        whereAnd = " AND "
	      }
      }
    }      
    
    //group by and order by
    for( i <- List(req.group,req.order)) yield {
      i match {
      	case Some(i: String) => consBuilder.append(sqlCommands.getProperty(i)) 
      	case None => {}        
      }
    }
   
    //prepare statement
    log.debug("SQL Statement: %s".format(consBuilder.toString()))
    using( req.conn.prepareStatement(consBuilder.toString(),Statement.RETURN_GENERATED_KEYS) ) { stmt => 
	    
	    //row limit
	    if(req.maxRows > 0) { stmt.setMaxRows(req.maxRows) }
	        
	    var x = 1
	    paramList.foreach( a => { 
	      a.asInstanceOf[AnyRef] match {
	        case s: Integer => { stmt.setInt(x,s) }
	        case s: String => { stmt.setString(x,s) }
	        case s: Date => { stmt.setDate(x,s) }
	        case s: Time => { stmt.setTime(x,s) }
	        case s: Timestamp => { stmt.setTimestamp(x,s) }
	        case s: ByteArrayInputStream => { stmt.setBinaryStream(x,s,s.asInstanceOf[ByteArrayInputStream].available()) }
	        case s => { stmt.setObject(x,s) }
	      }
	      log.debug("Parameter %s: %s".format(x,a))
	      x += 1 
	    })
	    
	    
	    //run statement
	    stmt.execute()
	    log.debug("Statement Executed")
	    
	    //get auto-insert keys
		var keys = stmt.getGeneratedKeys()
		if(keys != null) {
		    var keybuf = new ListBuffer[Any]();
		    while(keys.next()) {
		      keybuf += keys.getInt(1)
		    }
		    retval.generatedKeys = keybuf.toList
		}
	    
	    //pull results
	    log.debug("Pulling Results")
	    using( stmt.getResultSet() ) { ret => 
		  if(ret != null) {
			      
		   var meta = ret.getMetaData()
			        
		   var retbuf = new ListBuffer[Map[String,Any]]()
		   while(ret.next) {
		     val rMap = scala.collection.mutable.Map[String,Any]()
			
		     for(i <- 1 to meta.getColumnCount()) {
		       rMap += ( meta.getColumnLabel(i) -> ret.getObject(i))
		     }

		     //conversion
			 for( (para,arg) <- req.constraints ) {
			   if(converters.contains(para)) {
			     for( a <- arg) {
			      log.debug("Running Converstion %s=%s".format(para,a)) 
			      converters(para).convertRow(rMap,a)
			     }
			   }
			 }

		     		     
		     retbuf += Map(rMap.toSeq: _*)
		   }
			    
		   retval.results = retbuf.toList
		  }
	    }
	    log.debug("Result Pull Complete")
    }    
    retval
  }
  
  
}