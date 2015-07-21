/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.db

import org.slf4j.LoggerFactory
import net.jmatrix.eproperties.EProperties
import javax.sql.DataSource
import io.bigsense.conversion.ConverterTrait
import scala.collection.mutable.ListBuffer
import java.sql.Statement
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.io.ByteArrayInputStream
import java.util.{TimeZone, Calendar}
import java.text.SimpleDateFormat


trait DataHandlerTrait {

  var ds : DataSource = _

  var converters : Map[String,ConverterTrait] = _

  var sqlCommands : EProperties = _

  var dbDialect : String = _

  val DB_MSSQL = "mssql"
  val DB_MYSQL = "mysql"
  val DB_PGSQL = "pgsql"

  protected var log = LoggerFactory.getLogger(getClass())

  //Taken From: http://zcox.wordpress.com/2009/08/17/simple-jdbc-queries-in-scala/
  protected def using[Closeable <: {def close(): Unit}, B](closeable: Closeable)(getB: Closeable => B): B =
    try {
      getB(closeable)
    } finally {
      try { closeable.close() } catch { case e:Exception => {  } }
    }


  protected def runQuery(req: DBRequest): DBResult = {

    val retval = new DBResult()

    val consBuilder = new StringBuilder(sqlCommands.getProperty(req.queryName))

    val paramList: ListBuffer[Any] = new ListBuffer()
    paramList.appendAll(req.args)

    //constraints
    // (we can't use mkstring because we need to deal with the
    //  complex case of if something is an actual constraint (happens in query)
    //  or a conversation (happens row by row)
    var whereAnd = " WHERE "
    for ((para, list) <- req.constraints) {
      val con = sqlCommands.getProperty("constraint" + para)
      if ((!converters.contains(para)) && (con == null || con == "")) {
        throw new DatabaseException("Unknown Constraint: %s".format(para))
      }
      else if (!converters.contains(para)) {
        for (l <- list) {
          consBuilder.append(whereAnd)
          consBuilder.append(con)
          para match {
            case "WithinMetersFrom" => {

              // format: WithinMetersFrom=x10y10r2 - x, y, radius

              val regex = """([a-zA-Z]+)([\d]+)""".r
              val loc = regex.findAllIn(l.toString).map( s => {
                s match {
                  case regex(as, ns) => Some(as, ns.toInt)
                  case _             => None
                }
              }).filter(_.isDefined).map(_.get).toMap
              paramList.append(loc("long"))
              paramList.append(loc("lat"))
              paramList.append(loc("r"))
            }
            case _ => {
              paramList.append(l)
            }
          }
          whereAnd = " AND "
        }
      }
    }

    //group by and order by
    for (i <- List(req.group, req.order)) yield {
      i match {
        case Some(i: String) => consBuilder.append(sqlCommands.getProperty(i))
        case None => {}
      }
    }

    //prepare statement
    log.debug("SQL Statement: %s".format(consBuilder.toString()))

    /* PostgreSQL drivers quirk. If you use RETURN_GENERATED_KEYS, it adds RETURING
       to the end of every statement! Meanwhile, certain MySQL SELECT statements need RETURN_GENERATED_KEYS.
     */
    var keys = Statement.RETURN_GENERATED_KEYS
    if (dbDialect == DB_PGSQL) {
      keys = if (consBuilder.toString().toUpperCase().startsWith("INSERT")) Statement.RETURN_GENERATED_KEYS else Statement.NO_GENERATED_KEYS
    }

    using(req.conn.prepareStatement(consBuilder.toString(), keys)) {
      stmt =>

      //row limit
        if (req.maxRows > 0) {
          stmt.setMaxRows(req.maxRows)
        }

        var x = 1
        paramList.foreach(a => {
          log.debug("Parameter %s: %s".format(x, a))
          a.asInstanceOf[AnyRef] match {
            case s: java.lang.Integer => {
              stmt.setInt(x, s)
            }
            case s: String => {
              stmt.setString(x, s)
            }
            case s: Date => {
              stmt.setDate(x, s, Calendar.getInstance(TimeZone.getTimeZone("UTC")))
            }
            case s: Time => {
              stmt.setTime(x, s)
            }
            case s: Timestamp => {
              stmt.setTimestamp(x, s, Calendar.getInstance(TimeZone.getTimeZone("UTC")))
            }
            case s: ByteArrayInputStream => {
              stmt.setBinaryStream(x, s, s.available())
            }
            case s => {
              stmt.setObject(x, s)
            }
          }
          x += 1
        })


        //run statement
        stmt.execute()
        log.debug("Statement Executed")

        //get auto-insert keys
        using(stmt.getGeneratedKeys()) { keys =>
          if (keys != null) {
            var keybuf = new ListBuffer[Any]();
            while (keys.next()) {
              keybuf += keys.getInt(1)
            }
            retval.generatedKeys = keybuf.toList
          }
        }

        //pull results
        log.debug("Pulling Results")
        using(stmt.getResultSet()) {
          ret =>
            if (ret != null) {

              val meta = ret.getMetaData()

              var retbuf = new ListBuffer[Map[String, Any]]()
              while (ret.next) {
                val rMap = scala.collection.mutable.Map[String, Any]()

                for (i <- 1 to meta.getColumnCount()) {
                  rMap += (meta.getColumnLabel(i) -> (ret.getObject(i) match {
                    case null => null
                    case ts : Timestamp  => {
                      if(dbDialect == DB_MYSQL) {
                        //Ensure UTC (MySQL is the only driver that has trouble with this)
                        val dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"))
                        dateFormatGmt.format(ts)
                      }
                      else ts
                    }
                    case x:Any => x
                  }))
                }

                //conversion
                for ((para, arg) <- req.constraints) {
                  if (converters.contains(para)) {
                    for (a <- arg) {
                      log.debug("Running Converstion %s=%s".format(para, a))
                      converters(para).convertRow(rMap, a.toString)
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
