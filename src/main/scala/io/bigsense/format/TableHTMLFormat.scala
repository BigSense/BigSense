package io.bigsense.format
import io.bigsense.model.ModelTrait
import io.bigsense.util.WebAppInfo

class TableHTMLFormat extends FlatFormatTrait {

  override protected def renderRow(row :List[String]) : String = row.mkString("<tr><td>","</td><td>","</td></tr>")
  
  override protected def renderHeader(row:List[String]) : String = row.mkString("<thead><tr><th>","</th><th>","</th></tr></thead>")

  override def mimeType = "text/html"

  override def renderModels(model : List[ModelTrait]) : String = 
    """<!DOCTYPE html><html>
       <head>
       <title>BigSense :: Data Table</title>
       <script type="text/javascript" src="%s/js/jquery-1.7.1.min.js"></script>
       <script type="text/javascript" src="%s/js/jquery.tablesorter.min.js"></script>
       <script type="text/javascript" src="%s/js/jquery.timer.js"></script>
       <link rel="StyleSheet" type="text/css" href="%s/js/jquery.tablesorter.theme.blue/style.css" />
	   <script type="text/javascript">
	    $(document).ready(function () {
		  $("#dataTable").tablesorter();
	    });	 
	   </script>
       </head>
       <body><table id="dataTable" class="tablesorter">%s</table></body></html>""".format(WebAppInfo.contextPath,
           WebAppInfo.contextPath,WebAppInfo.contextPath,WebAppInfo.contextPath,super.renderModels(model))  
  
}
