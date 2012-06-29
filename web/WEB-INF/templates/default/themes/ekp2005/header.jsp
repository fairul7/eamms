<%@ page import="java.util.Date,
                 kacang.Application,
				 kacang.ui.menu.MenuGenerator,
                 com.tms.portlet.taglibs.PortalServerUtil,
				 java.net.InetAddress,
				 java.util.ArrayList,
				 java.util.Iterator,
				 kacang.ui.menu.MenuItem,
                 com.tms.collab.messenger.MessageModule"%>
<%@ include file="/common/header.jsp"%>
<x:config>
	<page name="header">
		<com.tms.collab.calendar.ui.WeeklyView name="calendar"/>
	</page>
</x:config>
<c:set var="showServerDetail"><%= Application.getInstance().getProperty("cluster.showServerDetail") %></c:set>
<html>
<head>
<title><fmt:message key="general.label.ekp"/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script>
<!--
	var isIE = (navigator.appName == "Microsoft Internet Explorer") ? true : false;
	var portletList = [];
	var placeholderList = [];
	var currentlyDragging = false;
//-->
</script>

    <script language="javascript" src="<c:url value="/ekms/images/ekp2005/ekp2005.js"/>"></script>
	<script language="javascript" src="<c:url value="/common/tree/tree.js"/>"></script>
	<script language="javascript" src="<c:url value="/common/WCH.js"/>"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" class="dashboardBackground">
	<div id="loadingMessage" style="display: block; position: absolute; border: solid 1px silver; background: white; font: 8pt black; font-family: Arial; padding:10px"><img src="<c:url value="/ekms/images/ekp2005/loading.gif" />" width="16" height="16" valign="middle" border="0"> <fmt:message key="general.label.loadingPleaseWait"/></div>
	<script>
	<!--
		function centerMessage() {
			var loadingMessage = document.getElementById("loadingMessage");
			loadingMessage.style.left=(document.body.offsetWidth / 2) - (loadingMessage.offsetWidth / 2);
			loadingMessage.style.top=200;
		}
		centerMessage();
	//-->
	</script>
    <iframe src="<%= request.getContextPath() %>/ekms/poll.jsp" width="0" height="0" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	<!--- Profile and Logout --->
	<table cellpadding=0 cellspacing=0 border=0 width=100%>
		<tr>
			<td valign=top align=left width=50% height=28 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_topgrey.gif">
				<table border=0 cellspacing=0 cellpadding=0 width="100%">
					<tr><td valign=middle align=left height=28 nowrap><span class="ekpHeader"><b>&nbsp;<fmt:message key="general.label.ekp"/></b></span>
						<c:if test="${showServerDetail}">&nbsp;&nbsp;&nbsp;[ <%= InetAddress.getLocalHost().getHostName() %> <%= InetAddress.getLocalHost().getHostAddress() %>, <%= System.getProperty("os.name", "unknown OS") %> ]</c:if>
					</td></tr>
				</table>
			</td>
			<td valign=top align=right width=50% height=28 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_topgrey.gif">
				<table border=0 cellspacing=0 cellpadding=0 width=430>
					<tr>
						<td valign=middle align=right width=280 height=28><span class="loginFont"><fmt:message key="theme.ekp2005.loginAs"/> : <b><c:out value="${sessionScope.currentUser.propertyMap.firstName}"/> <c:out value="${sessionScope.currentUser.propertyMap.lastName}"/></b></span></td>
						<td valign=middle align=right width=66 height=28>
							<table border=0 cellspacing=0 cellpadding=0>
								<tr><td valign=top align=left width=66 bgcolor="919191" colspan=3><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
								<tr>
									<td valign=top align=left width=1 bgcolor="919191"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td>
									<td valign=middle align=center width=64 height=15 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_tpbutgrey.gif"><a href="/ekms/profile/profile.jsp" class="blackbutlink"><fmt:message key="theme.ekp2005.myProfile"/></a></td>
									<td valign=top align=left width=1 bgcolor="F9F9F9"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td>
								</tr>
								<tr><td valign=top align=left width=66 bgcolor="F9F9F9" colspan=3><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
							</table>
						</td>
						<td valign=middle align=right width=64 height=28>
							<table border=0 cellspacing=0 cellpadding=0>
								<tr><td valign=top align=left width=63 bgcolor="919191" colspan=3><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
								<tr>
									<td valign=top align=left width=1 bgcolor="919191"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td>
									<td valign=middle align=center width=61 height=14 class="dashboardLogout"><a href="/ekms/logout.jsp" class="redbutlink"><fmt:message key="general.label.logout"/></a></td>
									<td valign=top align=left width=1 bgcolor="F9F9F9"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td>
								</tr>
								<tr><td valign=top align=left width=63 bgcolor="F9F9F9" colspan=3><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
							</table>
						</td>
						<td valign=middle align=right width=10><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<!--- App Menu --->
	<table cellpadding=0 cellspacing=0 border=0 width=100%>
		<tr>
			<td valign=top align=left width=2% height=26 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_bluetone.gif"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=5 border=0></td>
			<td valign=middle align=left width=20% height=26 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_bluetone.gif">
				<!--- My Dashboard --->
				<table border=0 cellspacing=0 cellpadding=0 align=left>
					<tr><td valign=top align=left width=98 bgcolor="1C4274" colspan=3><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
					<tr>
						<td valign=top align=left width=1 bgcolor="1C4274"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td>
						<td valign=top align=center width=96 height=16 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_tpbutblue.gif" nowrap>
                            <img src="<%= request.getContextPath() %>/ekms/images/ekp2005/ic_fire.gif" height=12 width=13 border=0><a href="/ekms/" class="whitebutlink"><fmt:message key="general.label.dashboard"/></a>
						</td>
						<td valign=top align=left width=1 bgcolor="91AED5"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td>
					</tr>
					<tr><td valign=top align=left width=95 bgcolor="91AED5" colspan=3><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
				</table>
				<!--- Lock/Unlock My Dashboard --->
                <c:if test="${pageContext.request.requestURI == '/ekms/home.jsp'}">
                <c:set var="ekpDashboardCookieValue" scope="request" value="${cookie['ekpLockDashboard'].value}"/>
                <c:choose>
                    <c:when test="${ekpDashboardCookieValue == false}">
                        <a href="#" onClick="toggleDashboard(); return false;" class="dashboardButtonLink">&nbsp;<img src="<%= request.getContextPath() %>/ekms/images/ekp2005/icv_unlocked.gif" width="19" height="19" border="0" align="middle" title="<fmt:message key="theme.ekp2005.dashboardUnlocked"/>">&nbsp;</a>
                    </c:when>
                    <c:otherwise>
                        <a href="#" onClick="toggleDashboard(); return false;" class="dashboardButtonLink">&nbsp;<img src="<%= request.getContextPath() %>/ekms/images/ekp2005/icv_locked.gif" width="19" height="19" border="0" align="middle" title="<fmt:message key="theme.ekp2005.dashboardLocked"/>">&nbsp;</a>
                    </c:otherwise>
                </c:choose>
                </c:if>
			</td>
			<td valign=top align=right width=80% height=26 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_bluetone.gif">
				<!--- Top menu --->
				<table border=0 cellspacing=0 cellpadding=0>
				<tr>
				<td valign=middle align=left background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_dkbluetone.gif">
					<table border=0 cellspacing=0 cellpadding=0>
						<tr>
							<x:display name="portal.appMenu" body="custom">
								<c:forEach items="${widget.menuItems}" var="menuItem" begin="0" end="4">
                                    <c:choose>
                                        <c:when test="${empty menuItem.icon}"><c:set var="imageIcon" value="/ekms/images/menu/clear.gif"/></c:when>
                                        <c:otherwise><c:set var="imageIcon" value="${menuItem.icon}"/></c:otherwise>
                                    </c:choose>
									<td valign=top align=left width=1><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/dot_vertical.gif" height=26 width=1 border=0></td>
									<td valign=middle align=center width=25><IMG SRC="<c:out value="${imageIcon}"/>" height=21 width=22 border=0></td>
									<td valign=middle align=center><a href="<c:out value="${menuItem.link}"/>" class="topmenu"><c:out value="${menuItem.label}"/></a></td>
									<td valign=top align=left width=4><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=4 border=0></td>
								</c:forEach>
							</x:display>
							<td valign=top align=left width=1><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/dot_vertical.gif" height=26 width=1 border=0></td>
							<td valign=middle align=center width=40><a href="" onClick="toggleHeaderTopMenu(); return false;" class="topmenu2"><fmt:message key="theme.ekp2005.more"/></a></td>
							<td valign=middle align=center width=25><a href="" onClick="toggleHeaderTopMenu(); return false;" ><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/ic_arrowdown.gif" height=21 width=22 border=0></a></td>
							<td valign=top align=left width=4><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=4 border=0></td>
							<td valign=top align=left width=1><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/dot_vertical.gif" height=26 width=1 border=0></td>
						</tr>
					</table>
				</td>
				</tr>
				</table>
				<!--- Close Top menu --->
			</td>
			<td valign=top align=left width=2% height=26 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_bluetone.gif"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=5 border=0></td>
		</tr>
	</table>
	<!--- Status Summary --->
	<table cellpadding=0 cellspacing=0 border=0 width=100% background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_bluestrip.gif">
		<tr><td height="10" colspan="3"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
		<tr>
			<td valign=bottom align=left width=10%>
				<table border=0 cellspacing=0 cellpadding=0>
					<tr>
						<td valign=bottom align=left width=20 height=21><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/date_left.gif" height=21 width=20 border=0></td>
						<td valign=middle align=center height=21 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_date.gif">
							<table border=0 cellspacing=0 cellpadding=0>
								<tr>
									<td valign=top align=left width=90 nowrap><a style="text-decoration: none" href="<%= request.getContextPath() %>/ekms/messaging/messageList.jsp?folder=Inbox"><span class="textsmall"><fmt:message key="theme.ekp2005.unreadMsg"/>&nbsp;:&nbsp;</span></a></td>
									<td valign=top align=left width=13 nowrap><a style="text-decoration: none" href="<%= request.getContextPath() %>/ekms/messaging/messageList.jsp?folder=Inbox"><span class="textsmallRed"><div id="HSTUnreadMsg">-</div></span></a></td>
									<td valign=top align=left width=30 nowrap><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=30 border=0></td>
									<td valign=top align=left width=90 nowrap><a style="text-decoration: none" href="<%= request.getContextPath() %>/ekms/messaging/checkEmail.jsp"><span class="textsmall"><fmt:message key="theme.ekp2005.popMail"/>&nbsp;:&nbsp;</span></a></td>
									<td valign=top align=left width=13 nowrap><a style="text-decoration: none" href="<%= request.getContextPath() %>/ekms/messaging/checkEmail.jsp"><span class="textsmallRed"><div id="HSTPOP">-</div></span></a></td>
									<td valign=top align=left width=30 nowrap><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=30 border=0></td>
									<td valign=top align=left width=90 nowrap><a style="text-decoration: none" href="<%= request.getContextPath() %>/ekms/messaging/messageList.jsp?folder=Quick%20Messages"><span class="textsmall"><fmt:message key="theme.ekp2005.quickMsg"/>&nbsp;:&nbsp;</span></a></td>
									<td valign=top align=left width=13 nowrap><a style="text-decoration: none" href="<%= request.getContextPath() %>/ekms/messaging/messageList.jsp?folder=Quick%20Messages"><span class="textsmallRed"><div id="HSTQMsg">-</div></span></a></td>
									<td valign=top align=left width=30 nowrap><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=30 border=0></td>
									<td valign=top align=left width=90 nowrap><span class="textsmall"><fmt:message key="theme.ekp2005.usersOnline"/>&nbsp;:&nbsp;</span></td>
									<td valign=top align=left width=13 nowrap><span class="textsmallRed"><div id="HSTOnline">-</div></span></td>
								</tr>
							</table>
						</td>
						<td valign=bottom align=left width=28 height=21><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/date_right.gif" height=21 width=28 border=0></td>
					</tr>
				</table>
				<iframe src="<%= request.getContextPath() %>/ekms/status.jsp" width="0" height="0" frameborder="0" marginheight="0" marginwidth="0"></iframe>
			</td>
			<td valign=bottom align=right width=80% height=21 valign="bottom">
				<table border=0 cellspacing=0 cellpadding=0 width="100%">
					<tr><td valign=top align=left width=100% height=1 class="dashboardStatusShadow" colspan=3><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
				</table>
			</td>
			<td valign=bottom align=right width=10% height=21>
				<table border=0 cellspacing=0 cellpadding=0>
					<tr>
						<td valign=bottom align=left width=22 height=21><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/date_left.gif" height=21 width=22 border=0></td>
						<td valign=middle align=center height=21 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_date.gif" nowrap><span class="dateFont"><%  pageContext.setAttribute("date", new Date()); %><fmt:formatDate value="${date}"/></span></td>
						<td valign=bottom align=left width=28 height=21><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/date_right.gif" height=21 width=28 border=0></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td valign=top align=left width=100% height=3 class="dashboardStatusBackground" colspan=3><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=3 width=1 border=0></td></tr>
		<tr><td valign=top align=left width=100% height=1 class="dashboardStatusShadow" colspan=3><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
	</table>
    <div id="ekmsTopMenu" style="position:absolute; width:180px; height:10px; z-index:-100; top: 29px; visibility:hidden">
		<table cellpadding=2 cellspacing=0 border=0 width=180>
			<tr>
				<td valign=top align=left width=119><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=20 width=1 border=0></td>
				<td valign=middle align=center width=61 class="dashboardMenuSelected">
					<table cellpadding=0 cellspacing=0 border=0>
						<tr>
							<td valign=middle align=center width=40><a href="" onClick="toggleHeaderTopMenu(); return false;"><fmt:message key="theme.ekp2005.more"/></td>
							<td valign=middle align=center width=25><a href="" onClick="toggleHeaderTopMenu(); return false;"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/ic_arrowdown.gif" height=21 width=22 border=0></a></td>
							<td valign=top align=left width=4><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=4 border=0></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td valign=top align=left width=180 colspan=2 class="dashboardMenuBorder">
					<table cellpadding=2 cellspacing=0 border=0 class="dashboardMenuBackground" width=100%>
						<x:display name="portal.appMenu" body="custom">
							<c:forEach items="${widget.menuItems}" var="menuItem" begin="5">
								<c:set var="menuLabel" value="${menuItem.label}" scope="request" />
								<%
									String menuLabel = (String) request.getAttribute("menuLabel");
									if(menuLabel.startsWith("---"))
									{
								%>
                                	<tr>
										<td valign=middle align=center width="24" height="1" class="dashboardMenuItem"><img src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td>
										<td valign=middle align=left height="1"><hr size="1" class="topmenuhr" width="85%" align="left"></td>
									</tr>
								<%
									}
									else
									{
								%>
									<tr>
                                        <c:set var="iconUrl"><c:choose><c:when test="${empty menuItem.icon}">/ekms/images/menu/clear.gif</c:when><c:otherwise><c:out value="${menuItem.icon}"/></c:otherwise></c:choose>
                                        </c:set>
										<td valign=middle align=center width=24 class="dashboardMenuItem"><IMG SRC="<c:url value="${iconUrl}"/>" height=21 width=22 border=0></td>
										<td valign=middle align=left><a href="<c:out value="${menuItem.link}"/>" class="topmenu3"><c:out value="${menuItem.label}" /></a></td>
									</tr>
								<%
									}
								%>
							</c:forEach>
						</x:display>
					</table>
				</td>
			</tr>
		</table>
    </div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr valign="top">
            <td id="ekmsLeftColumn" valign="top" width="166" background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_sitebar.gif" height="100%">
				<div id="ekmsLeftMenu" style="display:block">
            		<jsp:include page="left.jsp" flush="false"/>
				</div>
            </td>
			<td valign=top align=left width=34 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_minbar.gif">
				<table border=0 cellspacing=0 cellpadding=0 width=34>
					<tr><td valign=top align=left width=34 colspan=2><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=6 width=1 border=0></td></tr>
					<tr><td valign=top align=center width=34 colspan=2><a href="" onClick="toggleLeftMenu(); return false;"><img src="<%= request.getContextPath() %>/ekms/images/ekp2005/but_arw_right.gif" height=14 width=15 border=0 id="leftMenuArrow"></a></td></tr>
					<tr><td valign=top align=left width=34 colspan=2><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=5 width=1 border=0></td></tr>
					<tr>
						<td valign=top align=left width=5><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=5 border=0></td>
						<td valign=top align=left width=29><a href="" title="<fmt:message key="theme.ekp2005.shortcuts"/>" onClick="toggleLeftTabs('leftMenuShortcuts', 'leftMenuSearch', 'leftMenuHelp', 'leftMenuMessenger'); return false;"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/leftMenuShortcuts_on.gif" id="leftMenuShortcutsImage" height=36 width=23 border=0></a></td>
					</tr>
					<tr><td valign=top align=left width=34 colspan=2><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=2 width=1 border=0></td></tr>
					<tr>
						<td valign=top align=left width=5><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=5 border=0></td>
						<td valign=top align=left width=29><a href="" title="<fmt:message key="theme.ekp2005.search"/>" onClick="toggleLeftTabs('leftMenuSearch', 'leftMenuShortcuts', 'leftMenuHelp', 'leftMenuMessenger'); return false;"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/leftMenuSearch_off.gif" id="leftMenuSearchImage" height=36 width=23 border=0></a></td>
					</tr>
					<tr><td valign=top align=left width=34 colspan=2><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=2 width=1 border=0></td></tr>
					<tr>
						<td valign=top align=left width=5><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=5 border=0></td>
						<td valign=top align=left width=29><a href="" title="<fmt:message key="theme.ekp2005.help"/>" onClick="toggleLeftTabs('leftMenuHelp', 'leftMenuMessenger', 'leftMenuShortcuts', 'leftMenuSearch'); return false;"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/leftMenuHelp_off.gif" id="leftMenuHelpImage" height=36 width=23 border=0></a></td>
					</tr>
					<%
                        if("1".equals(Application.getInstance().getProperty("com.tms.collab.messenger.enabled")))
                        {
                    %>
					<tr><td valign=top align=left width=34 colspan=2><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=2 width=1 border=0></td></tr>
					<tr>
						<td valign=top align=left width=5><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=5 border=0></td>
						<td valign=top align=left width=29><a href="" title="<fmt:message key="theme.ekp2005.messenger"/>" onClick="toggleLeftTabs('leftMenuMessenger', 'leftMenuShortcuts', 'leftMenuSearch', 'leftMenuHelp'); return false;"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/tab_mesg_off.gif" id="leftMenuMessengerImage" height=36 width=23 border=0></a></td>
						
					</tr>
					<%
                       	}
                        else
                        {
					%>
					<IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" id="leftMenuMessengerImage" height=0 width=0 border=0>
					<%
                        }
                    %>
				</table>
				<script>
				<!--
					initLeftMenu();
				//-->
				</script>
			</td>
            <td width="1"><img src="<c:url value="/ekms/images/blank.gif" />" width="1"></td>
			<td>
				<table cellpadding="0" cellspacing="0" width="100%" border="0">
					<tr>
						<td colspan="2">
                            <table cellpadding="0" cellspacing="0" width="100%">
                                <tr><td><x:display name="header.calendar"/></td></tr>
                            </table>
							<table cellpadding=0 cellspacing=0 border=0 width=100%>
								<tr><td valign=middle align=center height=1><img src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height="1" width="1"></td></tr>
								<tr><td valign=middle align=center height=5 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_calminbar.gif"><a href="" onClick="toggleCalendarMenu(); return false;"><img id="topCalendarButton" src="<%= request.getContextPath() %>/ekms/images/ekp2005/but_miup.gif" height=5 width=26 border=0></a></td></tr>
							</table>
						</td>
					</tr>
                    <script>
                    <!--
                        function toggleCalendarMenu()
                        {
                            if(document.getElementById("ekmsCalendarWidget").style.display == "none" || document.getElementById("ekmsCalendarWidget").style.display == "" || document.getElementById("ekmsCalendarWidget").style.display == null)
                            {
                                document.getElementById("ekmsCalendarWidget").style.display="block";
                            }
                            else
                            {
                                document.getElementById("ekmsCalendarWidget").style.display="none";
                            }
                            updateCalendarImage();
                            treeSave("ekmsCalendarWidget");
                        }
                        function updateCalendarImage() {
                            if (document.getElementById("ekmsCalendarWidget").style.display == "none" || document.getElementById("ekmsCalendarWidget").style.display == "" || document.getElementById("ekmsCalendarWidget").style.display == null) {
                                document.getElementById("topCalendarButton").src = "<%= request.getContextPath() %>/ekms/images/ekp2005/but_midwn.gif";
                            }
                            else {
                                document.getElementById("topCalendarButton").src = "<%= request.getContextPath() %>/ekms/images/ekp2005/but_miup.gif";
                            }
                        }
                        updateCalendarImage();
                    //-->
                    </script>
					<tr>
						<x:display name="portal.menu" body="custom">
						<%
							ArrayList items = (ArrayList) request.getAttribute(MenuGenerator.MENU_FILE);
							if(items != null)
							{
                                items = new ArrayList(items);
							%>
								<td valign="top" align="left" width="170" class="dashboardModuleMenu">
									<table border=0 cellspacing=0 cellpadding=0 width=170>
									<%
										for (Iterator i = items.iterator(); i.hasNext();)
										{
											MenuItem item =  (MenuItem) i.next();
											if(item.isHeader())
											{
											%>
												<tr><td valign=middle align=left height=25 colspan=2 background="<%= request.getContextPath() %>/ekms/images/ekp2005/bg_letpanel.gif">&nbsp;&nbsp;&nbsp;<span class="folderHeader"><%= item.getLabel() %></span></td></tr>
											<%
											}
											else
											{
												if(item.isLinked())
												{
												%>
													<tr class="dashboardModuleMenu">
														<td valign=middle width=15% align=right height=28><IMG src="<%= request.getContextPath() %>/ekms/images/ic_general.gif" height=14 width=15 border=0></td>
														<td valign=middle width=85% align=left height=28>&nbsp;<a href="<%= item.getLink() %>" class="folderlink" <% if(!(item.getTarget()==null || "".equals(item.getTarget()))) {%>target="<%= item.getTarget() %>"<% } %>><%= item.getLabel() %></a></td>
													</tr>
												<%
												}
												if (item.getInclude() != null)
												{
												%>
                                                    <c:set var="includedContent"><jsp:include page="<%= item.getInclude() %>" /></c:set>
                                                    <c:if test="${!empty includedContent}">
													<tr class="dashboardModuleMenu">
														<td valign=middle align=left height=28 colspan="2"><c:out value="${includedContent}" escapeXml="false" /></td>
													</tr>
                                                    </c:if>
												<%
												}
												%>
                                                <tr class="dashboardModuleShadow"><td valign=middle width=100% colspan=2 align=right height=1><IMG src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
                                                <tr class="dashboardModuleLine"><td valign=middle width=100% colspan=2 align=right height=1><IMG src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
											<%
											}
										}
										%>

                                        </table>
									</td>
								<%
							}
						%>
						</x:display>
                        <%
                            if("1".equals(Application.getInstance().getProperty("com.tms.collab.messenger.enabled")))
                            {
                        %>
                        <x:template type="com.tms.collab.messenger.ui.MessengerPanel" />
                        <%
                            }
                        %>
						<% ArrayList items = (ArrayList) request.getAttribute(MenuGenerator.MENU_FILE); %>
						<td align="center" valign="top" width="90%" <% if(items == null) { %>colspan="2"<% } %>>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td valign="top">
										<table width="100%" border="0" cellpadding="5" cellspacing="0">
											<tr>
												<td valign="top">

