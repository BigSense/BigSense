<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="ua" %>

	<table id="SensorStatus" class="tablesorter">
		<thead>
			<tr>
			  <c:forEach var="h" items="${StatusModelHeaders}">
			  	<th>${h}</th>
			  </c:forEach>			  
			</tr>
		</thead>
		<tbody>
			  <c:forEach var="r" items="${StatusModelRows}">
			    <tr>
				    <c:forEach var="c" items="${StatusModelCols}">
				    
				      <c:choose>
				        <c:when test="${c eq 'time_since_last_report'}">
				          <c:set var="rClass">
				           <c:choose>
				            <c:when test="${r[c] gt threshold}">
							    rClass
							</c:when>
							<c:otherwise>
							    gClass
							</c:otherwise>
						   </c:choose>
				          </c:set>
 				        </c:when>
				        <c:otherwise>
				          <c:set var="rClass" value="" />
				        </c:otherwise>
				      </c:choose>
				        
				  		<td class="${rClass}">${r[c]}</td>
				  	</c:forEach>
			  	</tr>
			  </c:forEach>			
		</tbody>
	</table>
	
	
	<script type="text/javascript">
	  $(document).ready(function () {
		  $("#SensorStatus").tablesorter();
	  });	 
	</script>