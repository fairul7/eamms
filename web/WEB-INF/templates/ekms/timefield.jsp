<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="timefield" value="${widget}"/>

<c:if test="${timefield.invalid}">
  !<span style="border:1 solid #de123e">
</c:if>

<select name="<c:out value="${timefield.hourName}"/>" class="WCHhider" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
<c:forEach begin="0" end="23" var="h">
    <option value="<c:out value="${h}"/>" <c:if test="${h eq timefield.hour}">selected</c:if>>
        <fmt:formatNumber pattern="00" value="${h}"/></option>
</c:forEach>
</select>

<select name="<c:out value="${timefield.minuteName}"/>" class="WCHhider" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
<c:forEach begin="0" end="45" step="15" var="m">
    <option value="<c:out value="${m}"/>" <c:if test="${m eq timefield.minute}">selected</c:if>>
        <fmt:formatNumber pattern="00" value="${m}"/></option>
</c:forEach>
</select>

<c:if test="${timefield.invalid}">
  </span>
</c:if>

