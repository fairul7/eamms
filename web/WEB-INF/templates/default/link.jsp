<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="link" value="${widget}"/>
<c:url value="${link.url}" var="url">
    <c:param name="cn" value="${link.absoluteName}"/>
    <c:forEach items="${link.parameterMap}" var="p">
        <c:param name="${p.key}" value="${p.value}"/>
    </c:forEach>
</c:url>

<a href="<c:out value='${url}'/>"
   target="_blank"
   onClick="<c:out value='${link.onClick}'/>"
   onMouseOut="<c:out value='${link.onMouseOut}'/>"
   onMouseOver="<c:out value='${link.onMouseOver}'/>"
>
    <c:out value='${link.text}'/>
</a>
