<%@ page import="com.tms.ekms.search.ui.SearchWidget,
				 java.util.Collection,
				 java.util.ArrayList,
				 kacang.Application,
                 com.tms.collab.messenger.MessageModule"%>
<%@	page import="com.tms.portlet.taglibs.PortalServerUtil,
				 kacang.services.presence.PresenceService,
				 java.util.Collection,
				 java.util.Iterator,
				 kacang.services.security.User"%>
<%@ page import="kacang.services.security.SecurityService"%>
<%@ include file="/common/header.jsp" %>

<%
    String PERMISSION_CREATE_LIBRARY = "com.tms.collab.calendar.Calendaring"; // Permission to create new library
    Application app = Application.getInstance();
	SecurityService securityService = (SecurityService) app.getService(SecurityService.class);
	String userId = securityService.getCurrentUser(request).getId();
   	// check is user has permission on calendar
	boolean isCalendar = securityService.hasPermission(userId, PERMISSION_CREATE_LIBRARY, null, null);
    %>

<table cellpadding=0 cellspacing=0 border=0 width=166>
<div></div>
	<tr>
		<td valign=top align=left width=166>
			<table border=0 cellspacing=0 cellpadding=0 width=166>
				<tr><td valign=top align=left width=166 colspan=3 height=15><img src="<c:url value="/ekms/images/ekp2005/clear.gif"/>" height=15 width=1 border=0></td></tr>
				<tr>
					<td valign=top align=left width=13><img src="<c:url value="/ekms/images/ekp2005/clear.gif"/>" height=1 width=13 border=0></td>
					<td valign=top align=left width=150>
						<div id="leftMenuShortcuts" style="display: none">
							<!--- Collaboration --->
							<table border=0 cellspacing=1 cellpadding=1 width=146>
								<tr>
									<td valign=middle align=left width=22><img src="<c:url value="/ekms/images/ekp2005/clear.gif"/>" height=1 width=20 border=0></td>
									<td valign=middle align=left width=124><span class="sitebarHeader"><fmt:message key="theme.ekp2005.link.collaboration"/></span></td>
								</tr>
								<tr>
									<td valign=middle align=left width=22><img src="<c:url value="/ekms/images/ekp2005/icv_mesg.gif"/>" height=18 width=20 border=0></td>
									<td valign=middle align=left width=124><a href="<c:url value="/ekms/messaging/composeMessage.jsp"/>" class="siteBluelink"><fmt:message key="theme.ekp2005.link.composeMessage"/></a></td>
								</tr>
                                <tr>
                                    <td valign=middle align=left width=22><img src="<c:url value="/ekms/images/ekp2005/icv_dmail.gif"/>" height=18 width=20 border=0></td>
                                    <td valign=middle align=left width=124><a href="<c:url value="/ekms/messaging/checkEmail.jsp"/>" class="siteBluelink"><fmt:message key="theme.ekp2005.link.downloadMail"/></a></td>
                                </tr>
                                <%
                                    if (isCalendar){
                                %>
                                <tr>
                                    <td valign=middle align=left width=22><img src="<c:url value="/ekms/images/ekp2005/icv_appt.gif"/>" height=18 width=20 border=0></td>
                                    <td valign=middle align=left width=124><a href="<c:url value="/ekms/calendar/appointmentform.jsp?init=1"/>" class="siteBluelink"><fmt:message key="theme.ekp2005.link.newAppointment"/></a></td>
                                </tr>
                                <%
                                    }
                                %>
                                <tr>
                                    <td valign=middle align=left width=22><img src="<c:url value="/ekms/images/ekp2005/icv_task.gif"/>" height=18 width=20 border=0></td>
                                    <td valign=middle align=left width=124><a href="<c:url value="/ekms/calendar/todotaskform.jsp"/>" class="siteBluelink"><fmt:message key="theme.ekp2005.link.newTask"/></a></td>
								</tr>
								<tr>
									<td valign=middle align=left width=22><img src="<c:url value="/ekms/images/ekp2005/icv_contact.gif"/>" height=18 width=20 border=0></td>
									<td valign=middle align=left width=124><a href="<c:url value="/ekms/addressbook/abNewContact.jsp"/>" class="siteBluelink"><fmt:message key="theme.ekp2005.link.newContact"/></a></td>
								</tr>
							</table>
							<br>
							<!--- Content --->
							<table border=0 cellspacing=1 cellpadding=1 width=146>
								<tr>
									<td valign=middle align=left width=22><img src="/ekms/images/ekp2005/clear.gif" height=1 width=20 border=0></td>
									<td valign=middle align=left width=124><span class="sitebarHeader"><fmt:message key="theme.ekp2005.link.content"/></span></td>
								</tr>
								<tr>
									<td valign=middle align=left width=22><img src="/ekms/images/ekp2005/icv_article.gif" height=18 width=20 border=0></td>
									<td valign=middle align=left width=124><a href="" onClick="window.open('/ekms/content/popup/newArticle.jsp','newContentPopup','scrollbars=yes,resizable=yes,width=700,height=500'); return false;" class="siteBluelink"><fmt:message key="theme.ekp2005.link.newArticle"/></a></td>
								</tr>
								<tr>
									<td valign=middle align=left width=22><img src="/ekms/images/ekp2005/icv_document.gif" height=18 width=20 border=0></td>
									<td valign=middle align=left width=124><a href="" onClick="window.open('/ekms/content/popup/newDocument.jsp','newContentPopup','scrollbars=yes,resizable=yes,width=700,height=500'); return false;" class="siteBluelink"><fmt:message key="theme.ekp2005.link.newDocument"/></a></td>
								</tr>
								<tr>
									<td valign=middle align=left width=22><img src="/ekms/images/ekp2005/ic_content2.gif" height=18 width=20 border=0></td>
									<td valign=middle align=left width=124><a href="<c:url value="/ekms/cmsadmin/contentSummary.jsp"/>" class="siteBluelink"><fmt:message key="theme.ekp2005.link.manageContent"/></a></td>
								</tr>
							</table>
						</div>
						<div id="leftMenuSearch" style="display: none">
							<!--- Search --->
							<x:template type="com.tms.ekms.search.ui.SearchWidget" properties="pageSize=10" body="custom" name="leftSearch">
								<c:set var="searchWidget" value="${leftSearch}"/>
								<c:set var="searchWidget" value="${leftSearch}" scope="page"/>
								<c-rt:set var="typeSearch" value="<%= SearchWidget.LABEL_SEARCH_ONLY %>"/>
								<c-rt:set var="typeFullText" value="<%= SearchWidget.LABEL_FULLTEXT_ONLY %>"/>
								<c-rt:set var="typeAll" value="<%= SearchWidget.LABEL_ALL_SEARCHES %>"/>
								<c-rt:set var="searchType" value="<%= SearchWidget.LABEL_SEARCH_TYPE %>"/>
								<%
									SearchWidget widget = (SearchWidget) pageContext.getAttribute("searchWidget");
									String[] selectedModules = request.getParameterValues("modules");
									Collection selected = new ArrayList();
									if(selectedModules != null)
									{
										for(int i = 0; i < selectedModules.length; i++)
											selected.add(selectedModules[i]);
									}
								%>
								<table cellpadding="0" cellspacing="0" width="95%">
									<form method="post" action="<c:url value="/ekms/search/search.jsp"/>">
										<tr>
											<td><img src="/ekms/images/ekp2005/clear.gif" height=1 width=20 border=0></td>
											<td>
												<span class="textsmall"><fmt:message key="theme.ekp2005.generalSearch"/></span>
												<input type="text" name="query" value="" class="textfield_search">
												<input type="submit" value="<fmt:message key="general.label.search"/>" class="blueButton">
											</td>
										</tr>
										<tr>
											<td colspan=2>
												<table width="95%" cellspacing="1" cellpadding="2" border=0>
													<tr><td valign="top" width="50%"><img src="/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
													<c:forEach var="module" items="${searchWidget.searchableModuleList}" varStatus="stat">
														<tr>
															<td valign="top" width="50%">
																<input type="checkbox" name="modules" value="<c:out value='${module.key.class.name}'/>"
																	<%
																	String current = (String) pageContext.getAttribute("currentModule");
																	if(selected.contains(current))
																	{
																	%>
																		CHECKED
																	<%
																		}
																	%>
																>
																<span class="textsmall2"><fmt:message key="${module.key.class.name}"/></span>
																<c:if test="${typeAll == module.value}">
																	<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	<input type="radio" name="<c:out value="${module.key.class.name}${searchType}"/>" value="<c:out value="${typeSearch}"/>"
																	<% if(SearchWidget.LABEL_SEARCH_ONLY.equals(widget.getSelectedSearchType().get(current)) || (!SearchWidget.LABEL_FULLTEXT_ONLY.equals(widget.getSelectedSearchType().get(current)))) { %>
																		CHECKED
																	<% } %>
																	><span class="textsmall2"><fmt:message key="general.label.normalSearch"/></span>
																	<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	<input type="radio" name="<c:out value="${module.key.class.name}${searchType}"/>" value="<c:out value="${typeFullText}"/>"
																	<% if(SearchWidget.LABEL_FULLTEXT_ONLY.equals(widget.getSelectedSearchType().get(current))) { %>
																		CHECKED
																	<% } %>
																	><span class="textsmall2"><fmt:message key="general.label.fullTextSearch"/></span>
																</c:if>
															</td>
														</tr>
													</c:forEach>
												</table>
                                                <a href="<%=request.getContextPath()%>/ekms/search/searchProfile.jsp"><fmt:message key="searchprofile.label.title"/></a>                                                    
											</td>
										</tr>
									</form>
								</table>
							</x:template>
						</div>
						<style>
						.frameStyle {width: 100%;scrollbar-3dlight-color:#CCCCCC;scrollbar-arrow-color:#CCCCCC;scrollbar-base-color:#BBD5F2;scrollbar-darkshadow-color:#BBD5F2;scrollbar-face-color:#BBD5F2;scrollbar-highlight-color:#EFEFEF;scrollbar-shadow-color:#CCCCCC; scrollbar-track-color:#FFFFFF}
						</style>
						<div id="leftMenuHelp" style="display: block">
                            <%
                            	String helpRoot = Application.getInstance().getProperty("help.root");
                            	String context = response.encodeURL(request.getRequestURI());
                            	String file = "";
                            	String dir = "";
                            	if(!(context.indexOf("/ekms/") == -1))
                            	{
                            		file = context.substring(6);
                            		if(!(file.indexOf("/") == -1))
                            			dir = file.substring(0, file.lastIndexOf("/") + 1) + "index.jsp";
                            		else
                            			dir = "index.jsp";
                            	}
                            %>
							<c-rt:set var="helpRoot"  value="<%= helpRoot %>"/>
							<c-rt:set var="helpFile"  value="<%= file %>"/>
							<c-rt:set var="helpDir"  value="<%= dir %>"/>
							<c:set var="outputFile" value=""/>
							<c:set var="inclusionFile" value=""/>
							<c:set var="inclusionDir" value=""/>
                            <%-- in websphere, exception will be thrown when file cannot be found using the <c:import> --%>
                            <c:catch var="error">
							    <c:import url="${helpRoot}${helpFile}" var="inclusionFile"/>
							</c:catch>
                            
                            <c:choose>
                                <c:when test="${error != null}">
                                    <c:set var="outputFile" value="${helpRoot}${helpDir}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                    <c:when test="${!(empty inclusionFile)}">
                                        <c:set var="outputFile" value="${helpRoot}${helpFile}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:import url="${helpRoot}${helpDir}" var="inclusionDir"/>
                                        <c:if test="${!(empty inclusionDir)}">
                                            <c:set var="outputFile" value="${helpRoot}${helpDir}"/>
                                        </c:if>
                                    </c:otherwise>
                                    </c:choose>
                                    
                                </c:otherwise>
                            </c:choose>
                            
							<iframe src="<c:out value="${outputFile}"/>" width="150" height="420" align="left" scrolling="auto" frameborder="0" allowtransparency="true" marginheight="0" marginwidth="0" class="frameStyle"></iframe>
						</div>
                        <div id="leftMenuMessenger" style="display: none">
                        <%
                            if("1".equals(Application.getInstance().getProperty("com.tms.collab.messenger.enabled")))
                            {
                        %>
							<script>
								cui = '<%= ((User)session.getAttribute("currentUser")).getId() %>';
								path ='<c:url value="/ekms/messenger/MessengerIFrame.jsp"/>';
							</script>
							<iframe id="contactsSummaryFrame" name="contactsSummaryFrame" style="display:none;" width="0" height="0" src="<c:url value="/ekms/images/blank.gif"/>"></iframe>
							<div id="contactsSummary" valign=top>
								<jsp:include page="/ekms/messenger/MessengerIFrame.jsp"/>
							</div>
							<table>
							<tr><td colspan="4" align="left" height="2"><img src="/ekms/images/blank.gif" width="10" height="2"></td></tr>
							<tr>
								<td height="20" width="2" align = "left"><img src="/ekms/images/blank.gif" width="2" height="5"></td>
								<td height="20" valign="top" width="5"><img src="/ekms/images/ekp2005/ic_addgroup.gif"></td>
								<td height="20" valign="top" align = "left">
								<a class="messengerLeftBar" href="#" onclick="treeToggle('createGroupChat'); return false"><span id="click_createGroupChat"><font class="TextGreyOnline"><fmt:message key="com.tms.messenger.panelSideBar.createGroupChat"/></font></span></a>

									<span id="createGroupChat" style="display: none">
										<table width="100%">
											<tr>
												<td id = "createGroupChatForm" height="20" valign="left">
													<jsp:include page="/ekms/messenger/MessengerGroupPopOutSelect.jsp"/>
												</td>
											</tr>
										</table>
						            </span>
						        </td>
							</tr>
							</table>
                        <%
                            }
                        %>
                        </div>
						<br>
					</td>
					<td valign=top align=left width=3><img src="/ekms/images/ekp2005/clear.gif" height=1 width=3 border=0></td>
				</tr>
				<tr><td valign=top align=left width=166 colspan=3 height=30><img src="/ekms/images/ekp2005/clear.gif" height=30 width=1 border=0></td></tr>
			</table>
		</td>
	</tr>
</table>