<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="relatedContent" value="${widget}"/>
<c:set var="children" value="${relatedContent.related}"/>

<c:if test="${!empty children && !empty children[0]}">

    <br>
    <div class="contentHeader">
        <b><fmt:message key='cms.label.relatedContent'/></b>
    </div>

    <p>
    <ul>
    <c:forEach var="co" items="${children}" varStatus="status">

        <li><a href="content.jsp?id=<c:out value="${co.id}"/>" class="contentChildName"><c:out value="${co.name}"/></a></li>

    </c:forEach>
    </ul>
    </p>

</c:if>
