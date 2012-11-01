<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="password" value="${widget}"/>

<c:choose>
    <c:when test="${!password.invalid}">
    <input
       type="password"
       name="<c:out value="${password.absoluteName}"/>"
       onBlur="<c:out value="${password.onBlur}"/>"
       onFocus="<c:out value="${password.onFocus}"/>"
       maxlength="<c:out value="${password.maxlength}"/>"
       size="<c:out value="${password.size}"/>"
    >
    </c:when>
    <c:otherwise>
    !<input
       style="border:1px solid #de123e"
       type="password"
       name="<c:out value="${password.absoluteName}"/>"
       onBlur="<c:out value="${password.onBlur}"/>"
       onFocus="<c:out value="${password.onFocus}"/>"
       maxlength="<c:out value="${password.maxlength}"/>"
       size="<c:out value="${password.size}"/>"
    >
    </c:otherwise>
</c:choose>
<c:forEach var="child" items="${password.children}">
    <x:display name="${child.absoluteName}"/>
</c:forEach>


