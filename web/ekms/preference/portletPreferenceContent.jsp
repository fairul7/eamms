<%@ page import="com.tms.cms.portlet.ContentPortletPreference,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="portletPreferenceContent">
        <com.tms.cms.portlet.ContentPortletPreference name="preferenceForm"/>
    </page>
</x:config>
<%-- Event Handling --%>
<c-rt:set var="forward_success" value="<%= ContentPortletPreference.FORWARD_SUCCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= ContentPortletPreference.FORWARD_CANCEL %>"/>
<c:if test="${forward_success == forward.name}">
    <script>
        alert("<fmt:message key='portlet.message.preference.settingsSaved'/>");
        window.close();
    </script>
</c:if>
<c:if test="${forward_cancel == forward.name}">
    <script>
        window.close();
    </script>
</c:if>
<%-- Initializing Widget Values --%>
<c:if test="${!(empty param.entityId)}">
    <x:set name="portletPreferenceContent.preferenceForm" property="entityId" value="${param.entityId}"/>
</c:if>
<html>
<head>
    <title><fmt:message key='portlet.message.preference.personalizingContentPreferences'/></title>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="articleRow">
    <x:display name="portletPreferenceContent.preferenceForm"/>
</body>
</html>
