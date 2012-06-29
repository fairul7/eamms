<%@ page import="com.tms.collab.messaging.model.Util,kacang.Application"%>

<%
    // redirect to activate page if user's intranet account DOES NOT EXIST
    if(!Util.hasIntranetAccount(request)) {
        response.sendRedirect("activate.jsp");
    }
%>

<c:if test="${param.cn=='mFolderTree.tree'}">
    <c:choose>
        <c:when test="${!empty param.folder}">
            <c:redirect url="messageList.jsp?folder=${param.folder}" />
        </c:when>
        <c:otherwise>
            <c:redirect url="messageList.jsp?folderId=${param.folderId}" />
        </c:otherwise>
    </c:choose>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>

