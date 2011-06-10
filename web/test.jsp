<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>

12<c:out value="${hi}" />34

00${hi}00

<ol>
<c:forEach var="op" items="${ops}">
  <li>${op}</li>
</c:forEach> 
</ol>

<p>Test</p>

</body>
</html>