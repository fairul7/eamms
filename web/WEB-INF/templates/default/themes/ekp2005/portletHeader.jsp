<%@include file="/common/header.jsp" %>
<%@ page import="com.tms.portlet.theme.themes.DefaultTheme,
                 com.tms.portlet.theme.ThemeManager"%>
<%-- Initializing Constants --%>
<c-rt:set var="leftColumn" value="<%= DefaultTheme.COLUMN_LEFT_LABEL %>"/>
<c-rt:set var="rightColumn" value="<%= DefaultTheme.COLUMN_RIGHT_LABEL %>"/>
<c-rt:set var="cmdUp" value="<%= DefaultTheme.COMMAND_MOVE_UP %>"/>
<c-rt:set var="cmdDown" value="<%= DefaultTheme.COMMAND_MOVE_DOWN %>"/>
<c-rt:set var="cmdLeft" value="<%= DefaultTheme.COMMAND_MOVE_LEFT %>"/>
<c-rt:set var="cmdRight" value="<%= DefaultTheme.COMMAND_MOVE_RIGHT %>"/>
<c-rt:set var="cmdMaximize" value="<%= DefaultTheme.COMMAND_MAXIMIZE %>"/>
<c-rt:set var="cmdEdit" value="<%= DefaultTheme.COMMAND_EDIT %>"/>
<c-rt:set var="cmdClose" value="<%= DefaultTheme.COMMAND_CLOSE %>"/>
<c-rt:set var="keyLabel" value="<%= ThemeManager.LABEL_ENTITY_KEY %>"/>
<%-- Initializing Variables --%>
<c:set var="entity" value="${requestScope.entity}"/>
<c:set var="status" value="${requestScope.columnStatus}"/>
<table cellpadding=0 cellspacing=0 border=0 width=100%>
	<tr><td valign=top align=left class="portletHeaderLine"><IMG src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
	<tr>
		<td valign=top align=left class="portletHeaderTitle">
			<table border=0 cellspacing=2 cellpadding=1 width=100%>
				<tr>
					<td valign=middle align=left width=25><IMG src="<%= request.getContextPath() %>/ekms/images/ekp2005/bar_drag.gif" height=17 width=24 border=0></td>
					<td valign=middle align=left nowrap><span class="portletHeader"><c:out value="${entity.portlet.portletTitle}"/></span></td>
					<td valign=middle align=right width=52>
						<table border=0 cellspacing=0 cellpadding=0>
							<tr>
								<c:if test="${!(empty entity.preferences.edit)}">
									<c:choose>
										<c:when test="${empty entity.preferences.edit.preferenceValue}">
											<c:set var="editUrl"><c:url value="/ekms/portalserver/entityPreferences.jsp"/>?entityId=<c:out value="${entity.entityId}"/></c:set>
										</c:when>
										<c:otherwise>
											<c:set var="editUrl"><c:out value="${entity.preferences.edit.preferenceValue}"/>?entityId=<c:out value="${entity.entityId}"/></c:set>
										</c:otherwise>
									</c:choose>
									<td valign=top align=left width=14><a href="" onClick="portletEdit('<c:out value="${editUrl}"/>'); return false;"><img src="<%= request.getContextPath() %>/ekms/images/ekp2005/but_edit.gif" height=11 width=11 border=0></a></td>
								</c:if>
								<c:if test="${ekpDashboardCookieValue == true}"><td valign=top align=left width=14><a href="" onClick="blockToggle('<c:out value="${entity.entityId}"/>'); return false;" name="toggle_<c:out value="${entity.entityId}"/>" id="toggle_<c:out value="${entity.entityId}"/>"><IMG src="<%= request.getContextPath() %>/ekms/images/ekp2005/but_minimize.gif" height=11 width=11 border=0></a></td></c:if>
								<c:set var="closeOnClick"><%= response.encodeURL(request.getRequestURI()) %>?<c:out value="cn=${requestScope.themeManager.absoluteName}"/>&et=<c:out value="${cmdClose}"/>&<c:out value="${keyLabel}=${entity.entityId}"/></c:set>
								<c:if test="${ekpDashboardCookieValue == false}"><td valign=top align=left width=14><a href="" onClick="closeWindow('<c:out value="entity_${entity.entityId}"/>','<c:out value="${closeOnClick}"/>'); return false;"><IMG src="<%= request.getContextPath() %>/ekms/images/ekp2005/but_close.gif" height=11 width=11 border=0></a></td></c:if>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td valign=top align=left><IMG src="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td></tr>
</table>
<c:set var="entity" value="${requestScope.entity}"/>
<c:set var="cookieValue"><c:choose><c:when test="${cookie[entity.entityId].value != 'none'}">block</c:when><c:otherwise>none</c:otherwise></c:choose></c:set>
<div id="<c:out value="${entity.entityId}"/>" style="display: <c:out value="${cookieValue}"/>">
<table cellpadding=6 cellspacing=0 border=0 width=100%>
	<tr>
		<td valign=top align=left class="portletBorder">
			<table cellpadding=0 cellspacing=2 border=0 width=100% class="portletBody">
				<tr>
					<td valign=top align=center class="portletBody">
