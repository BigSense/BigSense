/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package org.bigsense.format

import scala.collection.immutable.List
import org.bigsense.model._

class CSVFormat extends FlatFormatTrait {

   protected override def renderRow(row :List[String]) = row.reduceLeft[String] { (a,b) => a + "," + b } + "\n"

}