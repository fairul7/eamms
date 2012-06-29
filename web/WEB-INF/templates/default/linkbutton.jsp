<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<c:set var="link" value="${widget}"/>
<c:url value="${link.url}" var="url">
    <c:param name="cn" value="${link.absoluteName}"/>
    <c:forEach items="${link.parameterMap}" var="p">
        <c:param name="${p.key}" value="${p.value}"/>
    </c:forEach>
</c:url>

<input 
    type="button" class="button"
    onclick="location.href='<c:out value='${url}'/>'" 
    value="<c:out value='${link.text}'/>">
