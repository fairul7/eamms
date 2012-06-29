<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<c:set var="label" value="${widget}"/>
<table cellpadding="2" cellspacing="0" width="100%" style="border:1px solid gray"">
	<tr><td bgcolor="E5E5E5"><c:out value="${label.text}" escapeXml="${label.escapeXml}"/></td></tr>
</table>
