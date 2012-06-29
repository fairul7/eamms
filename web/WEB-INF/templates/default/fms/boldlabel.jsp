<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="label" value="${widget}"/>
<b><c:out value="${label.text}" escapeXml="${label.escapeXml}"/></b>
