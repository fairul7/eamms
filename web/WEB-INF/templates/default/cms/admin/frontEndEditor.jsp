<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="fronteditor" value="${widget}"/>

<c:if test="${fronteditor.editMode}">

    <c:set var="id" value="${fronteditor.id}"/>

    <html>
    <head>
        <script language="JavaScript" src="<%= request.getContextPath() %>/cmsadmin/frontedit/frontedit.js">
        </script>
        <link rel="stylesheet" href="<%= request.getContextPath() %>/cmsadmin/frontedit/style.css">
    </head>

    <body>
    <div>
        <c:if test="${fronteditor.permissionMap['CheckOut']}">
            <a class="frontedit" href="#" onclick="return checkOut('<c:out value="${id}"/>')"><fmt:message key='cms.label.edit'/></a>
        </c:if>
        <c:if test="${!empty fronteditor.id}">
            <a class="frontedit" href="#" onclick="return admin('<c:out value="${id}"/>')"><fmt:message key='cms.label.admin'/></a>
        </c:if>
    </div>
    </body>
    </html>

    <x:flush />

</c:if>