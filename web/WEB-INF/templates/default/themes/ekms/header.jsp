<%@ page import="java.util.Date,
                 kacang.Application,
                 com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.portlet.PortletHandler,
				 java.util.ArrayList,
				 kacang.ui.menu.MenuGenerator,
				 java.util.Iterator,
				 kacang.ui.menu.MenuItem"%>
<%@ include file="/common/header.jsp"%>
<x:config >
    <page name="headermain">
        <com.tms.collab.calendar.ui.WeeklyView name="weeklyView" view="weeklyview"/>
    </page>
</x:config>

<html>
<head>
<title><fmt:message key="general.label.ekp"/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
    <script>
    <!--
        function portletEdit(url)
        {
            window.open(url, "entityPreferenceWindow", "height=400,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
        }
    //-->
    </script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgcolor="#FFFFFF">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td colspan="2" align="left" Valign="middle" bgcolor="#639ACE" class="titleTop"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="2"></td>
            <td colspan="2" align="left" Valign="middle" bgcolor="#639ACE" class="titleTop"><img src="<c:url value="/ekms/images/blank.gif"/>" width="260" height="2"></td>
        </tr>
        <tr>
            <td nowrap width="50%" colspan="2" Valign="middle" bgcolor="#FFFFCE" class="companyTitleBgColor" height="27"><b><font color="#666666" class="companyTitleFont">&nbsp;<fmt:message key="general.label.ekp"/></font></b></td>
            <td width="20%" bgcolor="#CECF9C" Valign="middle" class="desktopBgColor">
                <b>&nbsp;&nbsp;<font color="#003366" class="desktopFont">
                <c:out value="${sessionScope.currentUser.username}"/>
                </font></b>
            </td>
            <td width="10%" align="right" Valign="middle" bgcolor="#CECF9C" class="desktopBgColor" nowrap>
                <font color="#003366" class="dateFont">
                <a href="<c:url value="/ekms/portalserver/personalize.jsp"/>"><b><font class="dateFont"><fmt:message key="general.label.personalize"/></font></b></a> |
                <a href="<c:url value="/ekms/profile.jsp"/>" class="dateFont"><b><font class="dateFont"><fmt:message key="general.label.profile"/></font></b></a> |
                <a href="<c:url value="/ekms/logout.jsp"/>" class="dateFont"><b><font class="dateFont"><fmt:message key="general.label.logout"/></font></b></a>
                </font>
                <IFRAME SRC="<%= request.getContextPath() %>/ekms/poll.jsp" WIDTH="0" HEIGHT="0" FRAMEBORDER="0" MARGINHEIGHT="0" MARGINWIDTH="0"></IFRAME>
            </td>
        </tr>
    </table>
    <jsp:include page="menu.jsp" flush="true"/>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <form method="post" action="<c:url value="/ekms/search/search.jsp" />">
            <tr>
                <td colspan="2" bgcolor="#CCCC99" height="32" class="searchBgColor">
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <input name="query" type="text" class="textField" size="30">&nbsp;
                    <input type="submit" class="button" value="<fmt:message key="general.label.search"/>">
                    <input type="button" class="button" value="<fmt:message key="general.label.searchOptions"/>" onClick="document.location='<c:url value="/ekms/search/search.jsp"/>?display=block';">
                    &nbsp;&nbsp;&nbsp;
                </td>
                <td align="right" width="36%" bgcolor="#CECF9C" height="32" class="searchBgColor">
                    <b>
                    <font class="dateFont">
                        <%  pageContext.setAttribute("date", new Date()); %>
                        <fmt:formatDate value="${date}"/>
                    </font>
                    </b>&nbsp;
                </td>
            </tr>
            <tr><td colspan="3" bgcolor="#000066"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="1"></td></tr>
        </form>
        <tr>

            <td colspan="3">
                <x:display name="headermain.weeklyView"/>
            </td>
        </tr>
         <tr>
         <td colspan="3" border="1"width="5" height="2" valign="top" onClick="toggleCalendarMenu();" class="sideMenuMouseOut" onMouseOver="this.className='sideMenuMouseOver';" onMouseOut="this.className='sideMenuMouseOut';"></td>
        </tr>

    </table>
	<x:display name="portal.menu" body="custom">
	<%
		ArrayList items = (ArrayList) request.getAttribute(MenuGenerator.MENU_FILE);
		if(items != null)
		{
	%>
		<table width="100%" border="0" cellpadding="1" cellspacing="0">
			<tr>
				<td width="17%" valign="top" align="center">
					<table width="100%" border="0" cellspacing="0" cellpadding="1" class="menuBgOutline">
						<tr>
							<td>
								<table width="100%" border="0" cellpadding="1" cellspacing="0" class="menuBgBackground">
									<tr>
										<td>
											<table width="100%" border="0" cellpadding="0" cellspacing="0" class="menuBgColor">
	<%
			for (Iterator i = items.iterator(); i.hasNext();)
			{
				MenuItem item =  (MenuItem) i.next();
				if(item.isHeader())
				{
				%>
					<tr>
						<td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
							<table cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
									<td class="menuHeader"><%= item.getLabel() %></td>
									<td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/system/blank.gif" width="5" height="1"></td></tr>
				<%
				}
                else
                {
                    if(item.isLinked())
                    {
                    %>
                    <tr>
                        <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                        <td height="28" width="75%" nowrap>
                                <a href="<%= item.getLink() %>" <% if(!(item.getTarget()==null || "".equals(item.getTarget()))) {%>target="<%= item.getTarget() %>"<% } %>><font color="FFFFFF" class="menuFont"><b><%= item.getLabel() %></b></font></a>
                        </td>
                        <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                    </tr>
                    <%
                    }
                    if (item.getInclude() != null)
                    {
                    %>
                        <tr><td colspan="3"><jsp:include page="<%= item.getInclude() %>"/></td></tr>
                    <%
                    }
                    %>
                    <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                    <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                <%
                }
			}
	%>
										<tr><td height="28" colspan="3" nowrap>&nbsp;</td></tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
		<td align="center" valign="top">
			<table width="100%" border="0" cellpadding="5" cellspacing="0">
				<tr>
					<td valign="top">
	<%
		}
	%>
	</x:display>
