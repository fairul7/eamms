<%@ page import="java.util.Date,
                 kacang.Application,
                 com.tms.portlet.PortletHandler,
                 com.tms.portlet.taglibs.PortalServerUtil,
				 java.util.ArrayList,
				 kacang.ui.menu.MenuGenerator,
				 java.util.Iterator,
				 kacang.ui.menu.MenuItem"%>
<%@ include file="/common/header.jsp"%>
<html>
<head>
<title><fmt:message key="general.label.ekp"/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
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
    <IFRAME SRC="<%= request.getContextPath() %>/ekms/poll.jsp" WIDTH="0" HEIGHT="0" FRAMEBORDER="0" MARGINHEIGHT="0" MARGINWIDTH="0"></IFRAME>
	<table cellpadding="1" cellspacing="0" width="98%" align="center">
		<tr><td><img src="/ekms/images/clear.gif" height="7" width="1"></td></tr>
		<tr>
			<td class="topMenuBg">
            	<table cellpadding="1" cellspacing="0" width="100%">
					<tr>
						<td	bgcolor="FFFFFF">
							<table height="29" width="100%" border="0" cellpadding="0" cellspacing="0" background="/ekms/images/windows/bg_hi.gif">
								<tr>
									<td>
										<table border="0" cellspacing="0" cellpadding="0">
											<tr align="CENTER">
												<td nowrap><b>&nbsp;&nbsp;<a href="<c:url value="/ekms/"/>"><font color="#FFFFFF" class="titleMenuSmall"><fmt:message key="general.label.dashboard"/></font></A></b></td>
											</tr>
										</table>
									</td>
									<td align="right">
										<table border="0" cellspacing="0" cellpadding="0">
											<tr align="CENTER" >
												<td><font color="#FFFFFF" class="titleMenuFont"><c:out value="${sessionScope.currentUser.propertyMap.firstName}"/> <c:out value="${sessionScope.currentUser.propertyMap.lastName}"/>&nbsp;&nbsp;</font></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr><td bgcolor="808080"><img src="/ekms/images/clear.gif"></td></tr>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr><td bgcolor="FFFFFF"><img src="/ekms/images/clear.gif"></td></tr>
							</table>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<form method="post" action="<c:url value="/ekms/search/search.jsp" />">
									<tr>
										<td height="32" width="5" class="searchBgColor">&nbsp;</td>
										<td height="32" class="searchBgColor">
											<table cellpadding="0" cellspacing="0" class="topMenuButtonMouseOut" onMouseOver="this.className='topMenuButtonMouseOver';" onMouseOut="this.className='topMenuButtonMouseOut';">
												<tr>
													<td height="25" class="topMenuButtonMouseOut">&nbsp;&nbsp;&nbsp;<a onClick="toggleTopMenu(); return false;" href="" class="atopmenulink"><b><fmt:message key="general.label.mainMenu"/></b></a>&nbsp;&nbsp;&nbsp;</td>
												</tr>
											</table>
										</td>
										<td align="right" width="70%" bgcolor="#CECF9C" height="32" class="searchBgColor">
											&nbsp;&nbsp;&nbsp;
											<input name="query" type="text" class="input" size="30">&nbsp;
											<input type="submit" class="button" value="<fmt:message key="general.label.search"/>">
											<input type="button" class="button" value="<fmt:message key="general.label.searchOptions"/>" onClick="document.location='<c:url value="/ekms/search/search.jsp"/>?display=block';">
											&nbsp;&nbsp;&nbsp;
											<b>
											<font class="dateFont">
												<%  pageContext.setAttribute("date", new Date()); %>
												<fmt:formatDate value="${date}"/>
											</font>
											</b>&nbsp;
										</td>
									</tr>
								</form>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr><td bgcolor="CCCCCC"><img src="/ekms/images/clear.gif"></td></tr>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr><td bgcolor="808080"><img src="/ekms/images/clear.gif"></td></tr>
							</table>
							<%-- Formulating DHTML Menu --%>
							<script language="javascript" src="<c:url value="/common/tree/tree.js"/>"></script>
							<script language="javascript">
								function toggleTopMenu()
								{
									if(document.getElementById("ekmsTopMenu").style.visibility == "hidden" || document.getElementById("ekmsTopMenu").style.visibility == "")
									{
										document.getElementById("ekmsTopMenu").style.visibility="visible";
										
									}
									else
										document.getElementById("ekmsTopMenu").style.visibility="hidden";
								}
								function toggleCalendarMenu()
								{
									if(document.getElementById("ekmsCalendarWidget").style.display == "none" || document.getElementById("ekmsCalendarWidget").style.display == "" || document.getElementById("ekmsCalendarWidget").style.display == null)
									{
										document.getElementById("ekmsCalendarWidget").style.display="block";
										document.getElementById("ekmsCalendarColumn").style.display="block";
										document.getElementById("ekmsCalendarColumn").style.width="150px";
									}
									else
									{
										document.getElementById("ekmsCalendarWidget").style.display="none";
										document.getElementById("ekmsCalendarColumn").style.display="none";
										document.getElementById("ekmsCalendarColumn").style.width="0px";
									}
									treeSave("ekmsCalendarWidget");
									treeSave("ekmsCalendarColumn");
								}
							</script>
							<div id="ekmsTopMenu" style="position:absolute; width:200px; height:10px; z-index:1; left: 6px; top: 77px; visibility:hidden">
								<table cellpadding="1" cellspacing="0" width="100%" border="0" class="topMenuOutline">
									<tr>
										<td>
											<table cellpadding="1" cellspacing="0" width="100%" border="0" class="topMenuRow">
												<tr>
													<td>
														<table cellpadding="2" cellspacing="0" width="100%" border="0">
															<tr><td class="topMenuHeader"><fmt:message key="general.label.ekpMenu"/></td></tr>
														</table>
														<table cellpadding="1" cellspacing="0" width="100%" border="0">
															<tr><td><img src="/ekms/images/clear.gif" height="1"></td></tr>
															<x:display name="portal.appMenu" body="custom">
																<c:forEach items="${widget.menuItems}" var="menuItem">
                                                                	<tr>
																		<td class="topMenuMouseOut" onMouseOver="this.className='topMenuMouseOver';" onMouseOut="this.className='topMenuMouseOut';" onclick="document.location='<c:out value="${menuItem.link}"/>';">
																			<table cellpadding="0" cellspacing="0" width="100%">
																				<tr>
																					<td width="1">
																						<img src="
																							<c:choose>
																								<c:when test="${empty menuItem.icon}">/ekms/images/clear.gif</c:when>
                                                                                                <c:otherwise><c:out value="${menuItem.icon}"/></c:otherwise>
																							</c:choose>
																							"
																						width="22" height="17">
																					</td>
																					<td nowrap><font class="topMenuLink"><c:out value="${menuItem.label}"/></font></td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																</c:forEach>
															</x:display>
															<tr><td class="topMenuMouseOut"><hr size="1" color="999999" class="topMenuHR"></td></tr>
															<tr>
																<td class="topMenuMouseOut" onMouseOver="this.className='topMenuMouseOver';" onMouseOut="this.className='topMenuMouseOut';" onclick="document.location='<c:url value="/ekms/portalserver/personalize.jsp"/>';">
																	<table cellpadding="0" cellspacing="1" width="100%">
																		<tr>
																			<td width="1"><img src="/ekms/images/windows/dp_personalize.gif" width="22" height="17"></td>
																			<td nowrap><font class="topMenuLink"><fmt:message key="general.label.personalize"/></font></td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr>
																<td class="topMenuMouseOut" onMouseOver="this.className='topMenuMouseOver';" onMouseOut="this.className='topMenuMouseOut';" onclick="document.location='<c:url value="/ekms/profile.jsp"/>';">
																	<table cellpadding="0" cellspacing="1" width="100%">
																		<tr>
																			<td width="1"><img src="/ekms/images/clear.gif" width="22" height="17"></td>
																			<td nowrap><font class="topMenuLink"><fmt:message key="general.label.editProfile"/></font></td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr><td class="topMenuMouseOut"><hr size="1" color="999999" class="topMenuHR"></td></tr>
															<tr>
																<td class="topMenuMouseOut" onMouseOver="this.className='topMenuMouseOver';" onMouseOut="this.className='topMenuMouseOut';" onclick="document.location='<c:url value="/ekms/logout.jsp"/>';">
																	<table cellpadding="0" cellspacing="1" width="100%">
																		<tr>
																			<td width="1"><img src="/ekms/images/windows/dp_logout.gif" width="22" height="17"></td>
																			<td nowrap><font class="topMenuLink"><fmt:message key="general.label.logout"/></font></td>
																		</tr>
																	</table>
																</td>
															</tr>
														</table>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr valign="top">
									<x:config>
										<page name="header">
											<com.tms.collab.calendar.ui.VerticalWeeklyView name="verticalCalendar"/>
										</page>
									</x:config>
									<td valign="top" id="ekmsCalendarColumn">
										<div id="ekmsCalendarWidget" style="display:none; width:150px;">
											<x:display name="header.verticalCalendar"/>
										</div>
										<script>
											treeLoad("ekmsCalendarWidget");
											treeLoad("ekmsCalendarColumn");
											if(document.getElementById("ekmsCalendarWidget").style.display == "block")
												document.getElementById("ekmsCalendarColumn").style.width = "150px";
											else
												document.getElementById("ekmsCalendarColumn").style.width = "0px";
										</script>
									</td>
									<td width="10" valign="middle" onClick="toggleCalendarMenu();" class="sideMenuMouseOut">
										<img src="/ekms/images/windows/but_toClose.gif" border="0">
									</td>
									<td width="1"><img src="<c:url value="/ekms/images/" />blank.gif" width="1"></td>
									<td valign="top" align="left">
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

