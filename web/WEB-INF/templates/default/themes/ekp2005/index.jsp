<%@ include file="/common/header.jsp" %>
<%@page import="com.tms.portlet.theme.themes.*"%>
<c:set var="themeManager" value="${widget}" scope="request"/>
<script language="javascript" src="<c:url value="/ekms/includes/portlet.js"/>"></script>
<script language="javascript" src="<c:url value="/common/dragndrop/wz_dragdrop.js"/>"></script>
<c:set var="portletDragList" value=""/>
<c:set var="ekpDashboardCookieValue" scope="request" value="${cookie['ekpLockDashboard'].value}"/>
<iframe name="portalserverhidden" height="0" width="0" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="24%" valign="top">
            <%-- Announcements Spot --%>
			<table cellpadding="0" cellspacing="0">
				<tr><td><img src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height="3" width="1"></td></tr>
			</table>
			<table width="100%" border="0" cellspacing="3" cellpadding="0">
				<tr align="center" valign="top">
					<td width="100%">
						<div id="entity_announcement" style="position:relative;width:100%">
							<table cellpadding=0 cellspacing=0 border=0 width=100%>
								<tr><td valign=top align=left class="portletHeaderLine"><IMG src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
								<tr>
									<td valign=top align=left class="portletHeaderTitle">
										<table border=0 cellspacing=2 cellpadding=1 width=100%>
											<tr>
												<td valign=middle align=left width=25><IMG src="<%= request.getContextPath() %>/ekms/images/ekp2005/bar_drag2.gif" height=17 width=24 border=0></td>
												<td valign=middle align=left><span class="portletHeader"><fmt:message key="theme.ekp2005.announcements"/></span></td>
												<td valign=middle align=right width=52>&nbsp;</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td valign=top align=left><IMG src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
							</table>
							<table cellpadding=6 cellspacing=0 border=0 width=100%>
								<tr>
									<td valign=top align=left class="portletBorder">
										<table cellpadding=0 cellspacing=2 border=0 width=100% class="portletBody">
											<tr>
												<td valign=top align=left class="portletBody">
													<c:choose>
														<c:when test="${ekpDashboardCookieValue == false}">
															&nbsp;
														</c:when>
														<c:otherwise>
															<x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_DesktopAnnouncement" />
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</div>
                        <c:if test="${ekpDashboardCookieValue == false}">
                            <c:set var="portletDragList" value="\"entity_announcement\" + NO_DRAG"/>
                            <script>incrementList("portlet", "entity_announcement");</script>
                        </c:if>
					</td>
				</tr>
			</table>
			<%-- END: Announcements Spot --%>
            <c-rt:set var="leftColumn" value="<%= DefaultTheme.COLUMN_LEFT_LABEL %>"/>
            <c:forEach items="${requestScope.themeManager.portlets[leftColumn]}" var="item" varStatus="status">
                <c:set var="entity" value="${item}" scope="request"/>
				<c:if test="${ekpDashboardCookieValue == false}">
				<div id="placeholder_<c:out value="${item.entityId}"/>" class="placeholderMouseOut" style="position:relative"  onMouseOver="highlightPlaceholder('true', 'placeholder_<c:out value="${item.entityId}"/>');" onMouseOut="highlightPlaceholder('false', 'placeholder_<c:out value="${item.entityId}"/>');">
					<table cellpadding="0" cellspacing="0">
						<tr><td><img src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height="3" width="1"></td></tr>
					</table>
				</div>
				</c:if>
				<div id="entity_<c:out value="${item.entityId}"/>" style="position:relative; display: block">
					<table width="100%" border="0" cellspacing="3" cellpadding="0">
						<tr align="center" valign="top">
							<td>
								<c-rt:set var="path" value="<%= DefaultTheme.DEFAULT_PORTLET_ROOT %>" />
								<c:set var="path" value="${path}${item.portlet.portletClass}"/>
								<jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_HEADER %>"/>
                                <c:choose>
                                    <c:when test="${ekpDashboardCookieValue == false}">
                                        <fmt:message key="theme.ekp2005.dragToMove"/>
                                    </c:when>
                                    <c:otherwise>
                                        <% try { 
                                            String path = new String();
                                            path = (String) pageContext.getAttribute("path");
                                            if(path != null){
                                        %>
                                        <jsp:include page="<%= path %>"/>
                                        <% 
                                            }
                                           } catch (Exception e) { out.print((String) "!!! " + pageContext.getAttribute("path")); } 
                                        %>
                                    </c:otherwise>
                                </c:choose>
								<jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_FOOTER %>"/>
							</td>
						</tr>
					</table>
				</div>
                <c:if test="${ekpDashboardCookieValue == false}">
                    <c:set var="portletDragList" value="${portletDragList}, \"placeholder_${item.entityId}\" + NO_DRAG, \"entity_${item.entityId}\" + TRANSPARENT"/>
                    <script>
                        incrementList("portlet", "<c:out value="entity_${item.entityId}"/>");
                        incrementList("placeholder", "<c:out value="placeholder_${item.entityId}"/>");
                    </script>
                </c:if>
            </c:forEach>
            <c:if test="${ekpDashboardCookieValue == false}">
			<div id="placeholder_final_left" class="placeholderMouseOut" style="position:relative"  onMouseOver="highlightPlaceholder('true', 'placeholder_final_left');" onMouseOut="highlightPlaceholder('false', 'placeholder_final_left');">
				<table cellpadding="0" cellspacing="0" border="0" width="80%" align="center">
					<tr><td><img src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height="3" width="1"></td></tr>
				</table>
			</div>
                <script>incrementList("placeholder", "placeholder_final_left");</script>
                <c:set var="portletDragList" value="${portletDragList}, \"placeholder_final_left\" + NO_DRAG"/>
            </c:if>
        </td>
        <td width="1%">&nbsp;</td>
        <td width="50%" valign="TOP">
            <c-rt:set var="centerColumn" value="<%= DefaultTheme.COLUMN_CENTER_LABEL %>"/>
            <c:forEach items="${requestScope.themeManager.portlets[centerColumn]}" var="item" varStatus="status" >
                <c:set var="entity" value="${item}" scope="request"/>
	            <c:if test="${ekpDashboardCookieValue == false}">
				<div id="placeholder_<c:out value="${item.entityId}"/>" class="placeholderMouseOut" style="position:relative"  onMouseOver="highlightPlaceholder('true', 'placeholder_<c:out value="${item.entityId}"/>');" onMouseOut="highlightPlaceholder('false', 'placeholder_<c:out value="${item.entityId}"/>');">
					<table cellpadding="0" cellspacing="0">
						<tr><td><img src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height="3" width="1"></td></tr>
					</table>
				</div>
				</c:if>
				<div id="entity_<c:out value="${item.entityId}"/>" style="position:relative; display: block">
					<table width="100%" border="0" cellspacing="3" cellpadding="0">
						<tr valign="TOP">
							<td>
								<c-rt:set var="path" value="<%= DefaultTheme.DEFAULT_PORTLET_ROOT %>" />
								<c:set var="path" value="${path}${item.portlet.portletClass}"/>
								<jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_HEADER %>"/>
                                <c:choose>
                                    <c:when test="${ekpDashboardCookieValue == false}">
                                        <fmt:message key="theme.ekp2005.dragToMove"/>
                                    </c:when>
                                    <c:otherwise>
                                        <% try { 
                                            String path = new String();
                                            path = (String) pageContext.getAttribute("path");
                                            if(path != null){
                                        %>
                                        <jsp:include page="<%= path %>"/>
                                        <% 
                                            }
                                           } catch (Exception e) { out.print((String) "!!! " + pageContext.getAttribute("path")); } 
                                        %>
                                    </c:otherwise>
                                </c:choose>
								<jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_FOOTER %>"/>
							</td>
						</tr>
					</table>
				</div>
                <c:if test="${ekpDashboardCookieValue == false}">
                    <c:set var="portletDragList" value="${portletDragList}, \"placeholder_${item.entityId}\" + NO_DRAG, \"entity_${item.entityId}\" + TRANSPARENT"/>
                    <script>
                        incrementList("portlet", "<c:out value="entity_${item.entityId}"/>");
                        incrementList("placeholder", "<c:out value="placeholder_${item.entityId}"/>");
                    </script>
                </c:if>
            </c:forEach>
            <c:if test="${ekpDashboardCookieValue == false}">
			<div id="placeholder_final_center" class="placeholderMouseOut" style="position:relative"  onMouseOver="highlightPlaceholder('true', 'placeholder_final_center');" onMouseOut="highlightPlaceholder('false', 'placeholder_final_center');">
				<table cellpadding="0" cellspacing="0" border="0" width="80%" align="center">
					<tr><td><img src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height="3" width="1"></td></tr>
				</table>
			</div>
                <script>incrementList("placeholder", "placeholder_final_center");</script>
                <c:set var="portletDragList" value="${portletDragList}, \"placeholder_final_center\" + NO_DRAG"/>
            </c:if>
        </td>
        <td width="1%">&nbsp;</td>
        <td width="24%" valign="TOP">
            <c-rt:set var="rightColumn" value="<%= DefaultTheme.COLUMN_RIGHT_LABEL %>"/>
            <c:forEach items="${requestScope.themeManager.portlets[rightColumn]}" var="item" varStatus="status" >
                <c:set var="entity" value="${item}" scope="request"/>
            	<c:if test="${ekpDashboardCookieValue == false}">
				<div id="placeholder_<c:out value="${item.entityId}"/>" class="placeholderMouseOut" style="position:relative"  onMouseOver="highlightPlaceholder('true', 'placeholder_<c:out value="${item.entityId}"/>');" onMouseOut="highlightPlaceholder('false', 'placeholder_<c:out value="${item.entityId}"/>');">
					<table cellpadding="0" cellspacing="0">
						<tr><td><img src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height="3" width="1"></td></tr>
					</table>
				</div>
				</c:if>
				<div id="entity_<c:out value="${item.entityId}"/>" style="position:relative; display: block">
					<table width="100%" border="0" cellspacing="3" cellpadding="0">
						<tr valign="TOP">
							<td>
								<c-rt:set var="path" value="<%= DefaultTheme.DEFAULT_PORTLET_ROOT %>" />
								<c:set var="path" value="${path}${item.portlet.portletClass}"/>
								<jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_HEADER %>"/>
                                <c:choose>
                                    <c:when test="${ekpDashboardCookieValue == false}">
                                        <fmt:message key="theme.ekp2005.dragToMove"/>
                                    </c:when>
                                    <c:otherwise>
                                        <% try { 
                                            String path = new String();
                                            path = (String) pageContext.getAttribute("path");
                                            if(path != null){
                                        %>
                                        <jsp:include page="<%= path %>"/>
                                        <% 
                                            }
                                           } catch (Exception e) { out.print((String) "!!! " + pageContext.getAttribute("path")); } 
                                        %>
                                    </c:otherwise>
                                </c:choose>
								<jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_FOOTER %>"/>
							</td>
						</tr>
					</table>
				</div>
                <c:if test="${ekpDashboardCookieValue == false}">
                    <c:set var="portletDragList" value="${portletDragList}, \"placeholder_${item.entityId}\" + NO_DRAG, \"entity_${item.entityId}\" + TRANSPARENT"/>
                    <script>
                        incrementList("portlet", "<c:out value="entity_${item.entityId}"/>");
                        incrementList("placeholder", "<c:out value="placeholder_${item.entityId}"/>");
                    </script>
                </c:if>
            </c:forEach>
            <c:if test="${ekpDashboardCookieValue == false}">
			<div id="placeholder_final_right" class="placeholderMouseOut" style="position:relative"  onMouseOver="highlightPlaceholder('true', 'placeholder_final_right');" onMouseOut="highlightPlaceholder('false', 'placeholder_final_right');">
				<table cellpadding="0" cellspacing="0" border="0" width="80%" align="center">
					<tr><td><img src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height="3" width="1"></td></tr>
				</table>
			</div>
                <script>incrementList("placeholder", "placeholder_final_right");</script>
                <c:set var="portletDragList" value="${portletDragList}, \"placeholder_final_right\" + NO_DRAG"/>
            </c:if>
        </td>
    </tr>
</table>
<br>

<c:if test="${ekpDashboardCookieValue == false}">

<script type="text/javascript">
<!--
	//Processed placeholder list
    <c:if test="${!empty portletDragList}">
    if(getCookie("ekpLockDashboard") == "false")
        SET_DHTML(CURSOR_HAND,<c:out value="${portletDragList}" escapeXml="false"/>);
	    initPortlets();
    </c:if>


	//Interception drag actions
	function my_DragFunc()
	{
		if(!currentlyDragging)
		{
			for(i = 0; i < placeholderList.length; i++)
            {
				document.getElementById(placeholderList[i]).className='placeholderMouseOver';
                document.getElementById(placeholderList[i]).innerHTML='<fmt:message key="theme.ekp2005.dropHere"/>';
                document.getElementById(placeholderList[i]).style.height='10px';
            }
			currentlyDragging=true;
		}
	}

	//Intercepting drop actions. Requires ddSourceFile and ddThemeManager to be set before invocation
	function my_DropFunc()
	{
		var goodDrop = false;
		for(i = 0; i < placeholderList.length; i++)
        {
			document.getElementById(placeholderList[i]).className='placeholderMouseOut';
            document.getElementById(placeholderList[i]).innerHTML='';
            document.getElementById(placeholderList[i]).style.height='5px';
        }
		currentlyDragging=false;
		try
		{
			//Locating target placeholders
			for (i = 0; i < dd.elements.length; i++)
			{
				var el = dd.elements[i];
				var src = dd.obj.name;
				var placeholder = el.name;
				if (placeholder.indexOf("placeholder_") == 0)
				{
					if (my_DropFunc_InTarget(dd.obj, el))
					{
						document.location="<%= response.encodeURL(request.getRequestURI()) %>?<c:out value="cn=${requestScope.themeManager.absoluteName}"/>&et=drag_drop&source=" + dd.obj.id + "&target=" + el.id;
						goodDrop = true;
						break;
					}
				}
			}
		}
		catch (e)
		{
		}
		if(!goodDrop)
			dd.obj.moveTo(dd.obj.defx, dd.obj.defy);
	}

	//Post dropped target
	function my_DropFunc_InTarget(src, target)
	{
		return (dd.e.x >= target.x && dd.e.x <= (target.x + target.w) && dd.e.y >= target.y && dd.e.y <= (target.y + target.h));
	}

	//Highlighting placeholders
	function highlightPlaceholder(mouseover, layer)
	{
		if(currentlyDragging)
		{
			if(mouseover == "true")
            	document.getElementById(layer).className = "placeholderMouseDrag";
			else
            	document.getElementById(layer).className = "placeholderMouseOver";
		}
	}
//-->
</script>

</c:if>
