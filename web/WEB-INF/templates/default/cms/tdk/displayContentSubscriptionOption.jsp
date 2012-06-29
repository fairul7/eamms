<%@ include file="/common/header.jsp" %>

<c:set var="subscription" value="${widget.subscription}"/>

<c:if test="${!widget.hidden}">

<html>
<head>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/cmsadmin/frontedit/style.css">
</head>
<body>

    <c:choose>
    <c:when test="${!empty subscription}">
        <a class="frontedit" href="?action=unsubscribe&id=<c:out value='${widget.id}'/>"><fmt:message key="security.label.unsubscribe"/></a>
    </c:when>
    <c:otherwise>
        <a class="frontedit" href="?action=subscribe&id=<c:out value='${widget.id}'/>"><fmt:message key="security.label.subscribe"/></a>
    </c:otherwise>
    </c:choose>

</body>
</html>

</c:if>
