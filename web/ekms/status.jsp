<%@ page import="kacang.services.presence.PresenceService,
				 kacang.Application,   
				 kacang.services.security.User,
				 kacang.services.security.SecurityService,  
                 java.util.*"%>
<%@ include file="/common/header.jsp" %>
<%--
	This file is written to refresh every 5 minutes and write the corresponding data
	to the top level document. Layer IDs expected with the given information are:
	<div id>: <information>
	HSTUnreadMsg: Unread Memos
	HSTPOP: POP Status
	HSTQMsg: Quick Messages
	HSTOnline: Online Users
--%>
<%
 
	int onlineUsers = 0;

	SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
    User user = security.getCurrentUser(request);

    //Presence Service
    PresenceService presence = (PresenceService) Application.getInstance().getService(PresenceService.class);
	onlineUsers = presence.getOnlineUsers().size();
    StringBuffer strUsers = new StringBuffer("");
    strUsers.append("<a style=\\\"text-decoration: none\\\" href=\\\"#\\\" class=\\\"textsmallRed\\\" onClick=\\\"javascript:window.open('/ekms/presence.jsp', 'profileWindow', 'height=350,width=250,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');return false;\\\">");
	strUsers.append(String.valueOf(onlineUsers));
	strUsers.append("</a>"); 
%> 
<html>
<head>   
<script> 
	top.document.getElementById("HSTOnline").innerHTML = "<%= strUsers %>";	
</script>
<meta http-equiv="Refresh" content="300">
</head> 
</html>
 
 