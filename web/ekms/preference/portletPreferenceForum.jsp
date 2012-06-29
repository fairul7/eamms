<%@ page import="com.tms.collab.forum.ui.ForumPortletPreference,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="portletPreferenceForum">
        <com.tms.collab.forum.ui.ForumPortletPreference name="preferenceForm"/>
    </page>
</x:config>

<%-- Event Handling --%>
<c-rt:set var="forward_success" value="<%= ForumPortletPreference.FORWARD_SUCCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= ForumPortletPreference.FORWARD_CANCEL %>"/>
<c:if test="${forward_success == forward.name}">
    <script>
        alert("<fmt:message key='portlet.message.preference.settingsSaved'/>");
        window.close();
    </script>
</c:if>
<c:if test="${forward_cancel == forward.name}">
    <script>window.close();</script>
</c:if>
<%-- Initializing Widget Values --%>
<c:if test="${!(empty param.entityId)}">
    <x:set name="portletPreferenceForum.preferenceForm" property="entityId" value="${param.entityId}"/>
</c:if>
<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <title><fmt:message key='portlet.message.preference.personalizingForumPreferences'/></title>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="forumRow">
    <x:display name="portletPreferenceForum.preferenceForm"/>
</body>
</html>

