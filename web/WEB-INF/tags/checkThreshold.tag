<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@tag import="org.bigsense.util.JSPFunctions"%>
<%@ attribute name="duration" required="true" rtexprvalue="true" %>
<%@ attribute name="threshold" required="true" rtexprvalue="true" %>
<%@ attribute name="trueString" required="true" rtexprvalue="true" %>
<c:if test="${duration gt threshold}">
    ${trueString}
</c:if>
