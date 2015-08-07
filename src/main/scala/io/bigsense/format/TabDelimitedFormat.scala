package io.bigsense.format

import scala.collection.immutable.List
import io.bigsense.model.ModelTrait
import io.bigsense.model.DataModel

class TabDelimitedFormat extends FlatFormatTrait {

  protected override def renderRow(row :List[String]) = row.reduceLeft[String] { (a,b) => a + "\t" + b } + "\n"
  override def mimeType = "text/tab-separated-values"
}