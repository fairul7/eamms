<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
				 kacang.services.presence.PresenceService,
				 kacang.Application,
				 java.util.Collection,
				 java.util.Iterator,
				 kacang.services.security.User"%>
<%@ include file="/common/header.jsp" %>
<c-rt:set var="usersOnline" value="<%= Application.getInstance().getMessage("theme.ekp2005.usersOnline") %>"/>
<html>
	<head>
		<title><c:out value="${usersOnline}"/></title>
		<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
		<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
		<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
	</head>
	<body class="portletRow">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr valign="middle">
				<td height="22" bgcolor="#003366" class="contentTitleFont"><b>&nbsp;<c:out value="${usersOnline}"/></b></td>
				<td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
			</tr>
			<tr><td colspan="2" valign="top" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
			<tr>
				<td colspan="2" valign="top">
<%
	PresenceService service = (PresenceService) Application.getInstance().getService(PresenceService.class);
	Collection users = service.getOnlineUsers();
	for (Iterator i = users.iterator(); i.hasNext();)
	{
		User user = (User) i.next();
%><li><%= user.getName() %></li><br><%
	}
%>
				</td>
			</tr>
			<tr><td colspan="2" valign="top" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td></tr>
		</table>
	</body>
</html>