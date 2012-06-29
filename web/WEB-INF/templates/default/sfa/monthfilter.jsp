<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="datefield" value="${widget}"/>

<c:if test="${datefield.invalid}">
  !<span style="border:1 solid #de123e">
</c:if>

<select name="<c:out value="${datefield.monthName}"/>" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
<c:forEach begin="0" end="11" step="1" var="month">
    <option value="<c:out value="${month}"/>"<c:if test="${month eq datefield.month}">selected</c:if>>
        <fmt:parseDate value="${month+1}" pattern="MM" var="fm"/>
        <fmt:formatDate pattern="MMM" value="${fm}"/></option>
</c:forEach>
</select>
<input type="text" name="<c:out value="${datefield.yearName}"/>" value="<c:out value="${datefield.year}" /><%--<fmt:formatDate pattern="yyyy" value="${datefield.date}"/>--%>" maxlength="4" size="4" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">

<c:if test="${datefield.invalid}">
  </span>
</c:if>