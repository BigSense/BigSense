package io.bigsense.util
import net.jmatrix.eproperties.EProperties



class SQLProperties(val sqlCommandFile : String) extends EProperties {  
	//load(new java.io.FileInputStream(getClass().getResource(sqlCommandFile).getFile()))
  load(getClass()getResourceAsStream(sqlCommandFile))
}

