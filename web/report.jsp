<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en-US" >
<head>
  <title>Reports</title>
  <c:set var="home" value="${pageContext.request.contextPath}" />
  <link rel="StyleSheet" type="text/css" href="${home}/anytime/anytimc.css" />
  <script type="text/javascript" src="${home}/js/jquery-1.7.1.min.js"></script>
  <script type="text/javascript" src="${home}/anytime/anytimec.js"></script>
  <script type="text/javascript" src="${home}/anytime/anytimetz.js"></script>

</head>
<body>

	<div id="content">
	
	<h1>Reports</h1>
	
		<form action="">
			<h2>Action</h2>
			  <input type="radio" id="aggregate" name="action" /><label for="aggregate">Aggregate</label>
			  <input type="radio" id="query" name="action" /><label for="query">Query</label>
			  
			<div id="DateArguments">
			  
			</div>
			
			<h2>Constraints</h2>
			  
			
			<h2>Conversions</h2>
			
			<h2>Format</h2>
			  <input type="radio" id="table.html" name="format" /><label for="table.html">Table/HTML</label><br/>
			  <input type="radio" id="csv" name="format" /><label for="csv">Comma Separated</label><br />
			  <input type="radio" id="txt" name="format" /><label for="txt">Tab Delimited</label><br />
			  <input type="radio" id="flat.xml" name="format" /><label for="flat.xml">Flat/XML</label><br />
		</form>
	</div>

</body>
</html>