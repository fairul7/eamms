<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="checkbox" value="${widget}"/>

<c:choose>
    <c:when test="${!checkbox.invalid}">
    <input
       type="checkbox" name="<c:out value="${checkbox.fieldName}"/>"
       value="<c:out value="${checkbox.absoluteName}"/>"
       <c:if test="${checkbox.checked}"> checked</c:if>
       onBlur="<c:out value="${checkbox.onBlur}"/>"
       onClick="<c:out value="${checkbox.onClick}"/>"
       onFocus="<c:out value="${checkbox.onFocus}"/>"
    />
    </c:when>
    <c:otherwise>
    !<input
       style="border:1px solid #de123e"
       type="checkbox" name="<c:out value="${checkbox.fieldName}"/>"
       value="<c:out value="${checkbox.absoluteName}"/>"
       <c:if test="${checkbox.checked}"> checked</c:if>
       onBlur="<c:out value="${checkbox.onBlur}"/>"
       onClick="<c:out value="${checkbox.onClick}"/>"
       onFocus="<c:out value="${checkbox.onFocus}"/>"
    />
    </c:otherwise>
</c:choose>
<c:out value="${checkbox.text}"/>

