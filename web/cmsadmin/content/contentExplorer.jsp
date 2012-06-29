<%@ page import="com.tms.cms.core.model.ContentUtil"%>

<%
    ContentUtil.setContentTreeMode(request, true);
%>
<jsp:forward page="contentView.jsp"/>
