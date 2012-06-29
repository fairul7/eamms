<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="calendar" value="${widget}"/>
<c:set var="event" value="${calendar.event}"/>

<p>
<table width="90%">
<tr>
    <td><fmt:message key='calendar.label.title'/>:</td>
    <td><c:out value="${event.title}"/></td>
</tr>
<tr>
    <td><fmt:message key='calendar.label.from'/>:</td>
    <td><fmt:formatDate value="${event.startDate}" pattern="${globalDateLong}" /></td>
</tr>
<tr>
    <td><fmt:message key='calendar.label.to'/>:</td>
    <td><fmt:formatDate value="${event.endDate}" pattern="${globalDateLong}" /></td>
</tr>
<tr>
    <td><fmt:message key='general.label.location'/>:</td>
    <td><c:out value="${event.location}"/></td>
</tr>
<tr>
    <td colspan="2">
        <hr size="1">
        <p>
        <c:out value="${event.description}" escapeXml="false" />
    </td>
</tr>
</table>



