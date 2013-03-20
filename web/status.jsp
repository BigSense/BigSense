<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="ua" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en-US" >
<head>
  <title>Status</title>
  <c:set var="home" value="${pageContext.request.contextPath}" />
  <script type="text/javascript" src="${home}/js/jquery-1.7.1.min.js"></script>
  <script type="text/javascript" src="${home}/js/jquery.tablesorter.min.js"></script>
  <script type="text/javascript" src="${home}/js/jquery.timer.js"></script>
  <link rel="StyleSheet" type="text/css" href="${home}/js/jquery.tablesorter.theme.blue/style.css" />
  

   <style type="text/css">
     .rClass {
       background-color:red !important;
     }
     .gClass {
       background-color: green !important;
     }
   </style>
   
   	<script type="text/javascript">
   	
   	  function pullStatusTable() {
		  $.ajax({
			  url: "Status/ajaxStatusTable",
			  type: "GET",
			  data: {
				  refresh : $('#refresh').val(),
				  threshold: $('#threshold').val() 
			  },
			  cache: false
		  })
		  .success(function(data) {
			  $('#statusTable').html(data)
		  })
		  .fail(function(data) {
			  $('#statusTable').html('Error Loading Status Data')
		  });   		  
   	  }
   	  
	  var timer = $.timer(pullStatusTable);
   	  
   	  function adjustTimer() {
  		timer.once()
		timer.set({
			time: ($('#refresh').val() * 60000), 
			autostart: true
		});
   	  }
   	
	  $(document).ready(function () {
		adjustTimer()
		$('#updateChecks').click(adjustTimer);		
	  });	 
	</script>
</head>
<body>
	<h1>Active Sensor Status</h1>
	
	  <form id="refreshForm" action="<%= request.getContextPath() %>/api/Status" method="get">
	  <p>
		<label for="threshold">Threshold</label>
		<input id="threshold" name="threshold" type="text" value="${threshold}"/>		
		<label for="refresh">Refresh Interval</label>
		<input id="refresh" name="refresh" type="text" value="${refresh}" />
		<input id="updateChecks" type="button" value="Update" />
	  </p>
	  </form>

	  <div id="statusTable">
	  </div>

	
	
</body>
</html>