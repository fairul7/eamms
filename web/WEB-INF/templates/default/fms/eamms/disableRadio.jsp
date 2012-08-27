<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="radio" value="${widget}"/>

<input
   id="r1"
   type="radio"
   name="<c:out value="${radio.fieldName}"/>"
   value="<c:out value="${radio.absoluteName}"/>"
   onclick="javascript: return false;"
   <c:if test="${radio.checked}"> checked</c:if>
/>
<c:out value="${radio.text}"/>
