<%@ page import="kacang.runtime.taglib.DisplayTag"%>
<%@ include file="/common/header.jsp" %>

<c:choose>
    <c:when test="${param.debug == 'on'}">
        <c:set scope="session" var="kacang.widget.debug" value="true"/>
        <c:redirect url="/common/debug/debug.jsp"/>
    </c:when>
    <c:when test="${param.debug == 'off'}">
        <c:set scope="session" var="kacang.widget.debug" value="false"/>
        <c:redirect url="/common/debug/debug.jsp"/>
    </c:when>
</c:choose>

WIDGET DEBUG
<c:choose>
    <c:when test="${sessionScope['kacang.widget.debug'] == 'true'}">ON</c:when>
    <c:otherwise>OFF</c:otherwise>
</c:choose>

<c:set var="toggleParam">
<c:choose>
    <c:when test="${sessionScope['kacang.widget.debug'] == 'true'}">off</c:when>
    <c:otherwise>on</c:otherwise>
</c:choose>
</c:set>

[<a href="<c:url value="/common/debug/debug.jsp?debug=${toggleParam}"/>">toggle</a>]
