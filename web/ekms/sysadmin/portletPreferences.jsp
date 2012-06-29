<%@ page import="kacang.ui.WidgetManager,
                 com.tms.portlet.ui.PortletPreferenceForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="portletPreference">
        <com.tms.portlet.ui.PortletPreferenceForm name="form"/>
    </page>
</x:config>
<%-- Event Handling --%>
<c:if test="${!(empty param.portletId)}">
    <x:set name="portletPreference.form" property="portletId" value="${param.portletId}"/>
</c:if>
<c:if test="${!(empty param.name)}">
    <x:set name="portletPreference.form" property="name" value="${param.name}"/>
</c:if>
<c-rt:set var="forward_cancel" value="<%= PortletPreferenceForm.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_success" value="<%= PortletPreferenceForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_failure" value="<%= PortletPreferenceForm.FORWARD_FAILURE %>"/>
<c:if test="${forward.name == forward_cancel}">
    <script>window.close()</script>
</c:if>
<c:if test="${forward.name == forward_success}">
    <%
        WidgetManager manager = WidgetManager.getWidgetManager(request);
        PortletPreferenceForm portlet = (PortletPreferenceForm) manager.getWidget("portletPreference.form");
        request.setAttribute("pid", portlet.getPortletId());
    %>
    <script>
        alert("<fmt:message key='portlet.message.portletPreferencesUpdated'/>");
        window.opener.location = "portletsOpen.jsp?portletId=<%= request.getAttribute("pid") %>";
        window.close();
    </script>
</c:if>

<html>
<head>
    <title><fmt:message key='portlet.label.portletPreferences'/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="portletRow">
    <x:display name="portletPreference.form"/>
</body>
</html>
