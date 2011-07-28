package org.penguindreams.greenstation.util
import org.apache.log4j.Logger


/**
 * A very simple Scala wrapper of Properties.  Loads and/or stores a
 * map of name->value.  Note that there is a default value of "", so
 * using a nonexistant name will not cause an exception. 
 * 
 * Taken from http://blog.ishmal.org/news/2010/06/19/scala-quick-tip-using-a-java-properties-file/
 */ 
object Properties
{
	def log : Logger = Logger.getLogger(Properties.getClass())
  
    def loadFile(fname: String) : Map[String,String] =
        {
        try
            {
            val file = new java.io.FileInputStream(fname)
            val props = new java.util.Properties
            props.load(file)
            file.close
            val iter = props.entrySet.iterator
            val vals = scala.collection.mutable.Map[String,String]()
            while (iter.hasNext)
                {
                val item = iter.next
                vals += (item.getKey.toString ->item.getValue.toString)
                }
            vals.toMap.withDefaultValue("")
            }
        catch
            {
            case e:Exception => log.error("Properties.loadFile: " + fname,e)
            null
            }
        }    

    def saveFile(sprops: Map[String,String], fname: String) : Boolean =
        {
        try
            {
            val jprops = new java.util.Properties
            sprops.foreach(a=> jprops.put(a._1, a._2))
            val file = new java.io.FileOutputStream(fname)
            jprops.store(file, "Scala Properties: " + fname)
            file.close
            true
            }
        catch
            {
            case e:Exception => log.error("Properties.saveFile: " + fname,e)
            false
            }
        }    


}