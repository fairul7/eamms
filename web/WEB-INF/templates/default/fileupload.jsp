<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="fileselector" value="${widget}"/>

<fmt:message key="fileUpload.file"/>
<c:choose>
    <c:when test="${!fileselector.invalid}">
    <input
       type="file" name="<c:out value="${fileselector.absoluteName}"/>"
       value="<c:out value="${fileselector.value}"/>"
       onBlur="<c:out value="${fileselector.onBlur}"/>"
       onChange="<c:out value="${fileselector.onChange}"/>"
       onFocus="<c:out value="${fileselector.onFocus}"/>"
    >
    </c:when>
    <c:otherwise>
    !<input
       style="border:1px solid #de123e"
       type="file" name="<c:out value="${fileselector.absoluteName}"/>"
       value="<c:out value="${fileselector.value}"/>"
       onBlur="<c:out value="${fileselector.onBlur}"/>"
       onChange="<c:out value="${fileselector.onChange}"/>"
       onFocus="<c:out value="${fileselector.onFocus}"/>"
    >
    </c:otherwise>
</c:choose>
<c:forEach var="child" items="${fileselector.children}">
    <x:display name="${child.absoluteName}"/>
</c:forEach>