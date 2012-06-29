<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="table" scope="request" value="${widget}"/>

<table border="0" cellpadding="2" cellspacing="0" width="<c:out value="${table.width}"/>">
<tr>
<td>
    <jsp:include page="../../table.jsp" flush="true" />
</td>
</tr>
<tr>
<td>
    <x:display name="${table.previewPortlet.absoluteName}" />
</td>
</tr>
</table>
