<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<c:set var="countryselectbox" value="${widget}"/>
<c:if test="${countryselectbox.invalid}">
  !<span style="border:1 solid #de123e">
</c:if>
<select
    name="<c:out value="${countryselectbox.absoluteName}"/>"
    onBlur="<c:out value="${countryselectbox.onBlur}"/>"
    onChange="<c:out value="${countryselectbox.onChange}"/>"
    onFocus="<c:out value="${countryselectbox.onFocus}"/>"
>
    <c:forEach items="${countryselectbox.optionMap}" var="country">
        <option value="<c:out value='${country.key}'/>" <c:if test="${countryselectbox.value==country.key}">SELECTED</c:if>><c:out value='${country.value}'/></option>
    </c:forEach>
</select>
<c:if test="${countryselectbox.invalid}">
  </span>
</c:if>
