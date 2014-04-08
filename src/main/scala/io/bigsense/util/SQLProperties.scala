package io.bigsense.util
import org.apache.log4j.Logger
import net.jmatrix.eproperties.EProperties
import java.net.URL
import scala.reflect.BeanProperty



class SQLProperties(val sqlCommandFile : String) extends EProperties {  
	load(new java.io.FileInputStream(getClass().getResource(sqlCommandFile).getFile()))  
}

