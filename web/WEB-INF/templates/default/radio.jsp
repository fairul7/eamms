<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="radio" value="${widget}"/>

<c:choose>
    <c:when test="${!radio.invalid}">
    <input
       type="radio"
       name="<c:out value="${radio.fieldName}"/>"
       value="<c:out value="${radio.absoluteName}"/>"
       onBlur="<c:out value="${radio.onBlur}"/>"
       onClick="<c:out value="${radio.onClick}"/>"
       onFocus="<c:out value="${radio.onFocus}"/>"
       <c:if test="${radio.checked}"> checked</c:if>
    />
    </c:when>
    <c:otherwise>
    !<input
       style="border:1px solid #de123e"
       type="radio"
       name="<c:out value="${radio.fieldName}"/>"
       value="<c:out value="${radio.absoluteName}"/>"
       onBlur="<c:out value="${radio.onBlur}"/>"
       onClick="<c:out value="${radio.onClick}"/>"
       onFocus="<c:out value="${radio.onFocus}"/>"
       <c:if test="${radio.checked}"> checked</c:if>
    />
    </c:otherwise>
</c:choose>
<c:out value="${radio.text}"/>


