/**
 * 
 * 
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.format

import scala.collection.immutable.List
import io.bigsense.model._

class CSVFormat extends FlatFormatTrait {

   protected override def renderRow(row :List[String]) = row.reduceLeft[String] { (a,b) => a + "," + b } + "\n"
   override def mimeType = "text/csv"
}