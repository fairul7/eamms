<%@ page import="com.tms.collab.messaging.model.Util"%>
<%--
<%@include file="/common/header.jsp" %>
--%>
<%
    // redirect to index page if user's intranet account EXISTS
    if(Util.hasIntranetAccount(request)) {
        response.sendRedirect("index.jsp");
    }
%>
<%@include file="/ekms/includes/header.jsp" %>
<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10">