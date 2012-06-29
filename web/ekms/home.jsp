<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.portlet.ui.PortalServer,
                 com.tms.portlet.theme.ThemeManager" %>
<%-- Event Handling --%>
<c-rt:set var="forward_personalize" value="<%= PortalServer.FORWARD_PERSONALIZE %>"/>
<c-rt:set var="forward_themes" value="<%= PortalServer.FORWARD_THEMES %>"/>
<c-rt:set var="forward_portlets" value="<%= PortalServer.FORWARD_PORTLETS %>"/>
<c-rt:set var="forward_edit" value="<%= ThemeManager.FORWARD_EDIT %>"/>
<c:if test="${(forward.name == forward_personalize)}">
    <c:redirect url="/ekms/portalserver/personalize.jsp"/>
</c:if>
<c:if test="${(forward.name == forward_themes)}">
    <c:redirect url="/ekms/portalserver/themes.jsp"/>
</c:if>
<c:if test="${(forward.name == forward_portlets)}">
    <c:redirect url="/ekms/portalserver/portlets.jsp"/>
</c:if>
<c:if test="${(forward.name == forward_edit)}">
    <%-- TODO: Fix filter forwarding so we don't have to load information in request scope --%>
    <c-rt:set var="url" value="<%= request.getAttribute(ThemeManager.FORWARD_EDIT) %>"/>
    <script>
    <c:choose>
        <c:when test="${empty url}">
            window.open("<c:url value="/ekms/portalserver/"/>entityPreferences.jsp?entityId=<c:out value="${param.entityId}"/>", "entityPreferenceWindow", "height=400,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
        </c:when>
        <c:otherwise>
            window.open("<c:out value="${url}"/>?entityId=<c:out value="${param.entityId}"/>", "entityPreferenceWindow", "height=400,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
        </c:otherwise>
    </c:choose>
    </script>
</c:if>
<%-- Render --%>
<%@ include file="includes/header.jsp" %>
<x:display name="portal.server"/>
<%@ include file="includes/footer.jsp" %>