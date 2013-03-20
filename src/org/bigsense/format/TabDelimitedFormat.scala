package org.bigsense.format

import scala.collection.immutable.List
import org.bigsense.model.ModelTrait
import org.bigsense.model.DataModel

class TabDelimitedFormat extends FlatFormatTrait {

  protected override def renderRow(row :List[String]) = row.reduceLeft[String] { (a,b) => a + "\t" + b } + "\n"
  
}