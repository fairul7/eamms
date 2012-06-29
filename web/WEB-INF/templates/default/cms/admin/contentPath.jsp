<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="path" scope="request" value="${widget}"/>

<c:forEach items="${path.pathToContentObject}" var="co" varStatus="ps">
    <c:choose>
        <c:when test="${path.selectedId == co.id}">
            &nbsp;<a title="<c:out value="${co[path.displayTitle]}"/>"
               class="path"
               href="<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${path.absoluteName}"/>&id=<c:out value="${co[path.displayId]}"/>">
                    <b><c:out value="${co.name}"/></b></a>
        </c:when>
        <c:otherwise>
            &nbsp;<a title="<c:out value="${co[path.displayTitle]}"/>"
               class="path"
               href="<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${path.absoluteName}"/>&id=<c:out value="${co[path.displayId]}"/>">
                    <c:out value="${co[path.displayProperty]}"/></a>
        </c:otherwise>
    </c:choose>
    <c:if test="${!ps.last}">
        &gt;
    </c:if>
</c:forEach>