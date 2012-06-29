<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<table>
<tr><Td ALIGN="CENTER"><B>Conflict Exception!!</B>
</td></tr>
<tr><td>
The appointment you are going to make has time/resource conflict with the following appointment(s):
</td></tr>
<tr><td><ul>
<c:forEach items="${conflict.conflictList}" var="conflict" >
    <li><a href="" onClick="javascript:window.open('appointmentview.jsp?id=<c:out value="${conflict.eventId}" />&instanceId=<c:out value="${conflict.instanceId}" />','aview','scrollbars=yes,resizable=yes,width=450,height=380'); return false;"><c:out value="${conflict.description}"/> </a>
 (<fmt:formatDate value="${conflict.startDate}" pattern="${globalDatetimeLong}"/> - <fmt:formatDate value="${conflict.endDate}" pattern="${globalDatetimeLong}"/>)</li><br>

 </c:forEach></ul></td></tr></table>
<hr>