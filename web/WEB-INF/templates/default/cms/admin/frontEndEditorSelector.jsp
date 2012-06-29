<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="fronteditor" value="${widget}"/>
<c-rt:set var="query" value="<%= request.getQueryString() %>"/>

<html>
<head>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/cmsadmin/frontedit/style.css">
</head>
<body>

<c:choose>
<c:when test="${!fronteditor.hasPermission}">
</c:when>
<c:when test="${fronteditor.editMode}">
    <a class="frontedit" href="?editMode=false&<c:out value='${query}'/>"><fmt:message key='cms.label.viewMode'/></a>
</c:when>
<c:otherwise>
    <a class="frontedit" href="?editMode=true&<c:out value='${query}'/>"><fmt:message key='cms.label.editMode'/></a>
</c:otherwise>
</c:choose>

<c:if test="${fronteditor.refreshCache}">
    <x:flush/>
</c:if>

</body>
</html>