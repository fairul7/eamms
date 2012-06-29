<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="path" scope="request" value="${widget}"/>
<c:set var="pathToContentObject" value="${path.pathToContentObject}"/>

<c:if test="${!empty pathToContentObject[0]}">
    <a class="contentPathLink" href="index.jsp"><fmt:message key='general.label.home'/></a> &gt;
</c:if>

<c:forEach items="${pathToContentObject}" var="co" varStatus="ps">
    <c:choose>
        <c:when test="${param.id == co.id}">
            <a title="<c:out value="${co.name}"/>"
               class="contentPathLinkSelected"
               href="./content.jsp?id=<c:out value="${co.id}"/>">
                    <c:out value="${co.name}"/></a>
        </c:when>
        <c:otherwise>
            <a title="<c:out value="${co.name}"/>"
               class="contentPathLink"
               href="./content.jsp?id=<c:out value="${co.id}"/>">
                    <c:out value="${co.name}"/></a>
        </c:otherwise>
    </c:choose>
    <c:if test="${!ps.last}">
        &gt;
    </c:if>
</c:forEach>