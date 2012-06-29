<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="textfield" value="${widget}"/>

<c:set var="size" value="${textfield.size}"/>
<c:if test="${empty size}">
    <c:set var="size" value="50"/>
</c:if>

<c:choose>
    <c:when test="${!textfield.invalid}">
    <input
       type="text" name="<c:out value="${textfield.absoluteName}"/>"
       value="<c:out value="${textfield.value}"/>"
       onBlur="<c:out value="${textfield.onBlur}"/>"
       onChange="<c:out value="${textfield.onChange}"/>"
       onFocus="<c:out value="${textfield.onFocus}"/>"
       onSelect="<c:out value="${textfield.onSelect}"/>"
       maxlength="<c:out value="${textfield.maxlength}"/>"
       size="<c:out value="${size}"/>"
    >
    </c:when>
    <c:otherwise>
    !<input
       style="border:1px solid #de123e"
       type="text" name="<c:out value="${textfield.absoluteName}"/>"
       value="<c:out value="${textfield.value}"/>"
       onBlur="<c:out value="${textfield.onBlur}"/>"
       onChange="<c:out value="${textfield.onChange}"/>"
       onFocus="<c:out value="${textfield.onFocus}"/>"
       onSelect="<c:out value="${textfield.onSelect}"/>"
       maxlength="<c:out value="${textfield.maxlength}"/>"
       size="<c:out value="${size}"/>"
    >
    </c:otherwise>
</c:choose>
<c:forEach var="child" items="${textfield.children}">
    <x:display name="${child.absoluteName}"/>
</c:forEach>


