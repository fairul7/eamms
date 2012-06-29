<%@ page import="com.tms.portlet.ui.EntityPreferenceForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="entityPreferences">
        <com.tms.portlet.ui.EntityPreferenceForm name="form"/>
    </page>
</x:config>
<%-- Variable Assignment --%>
<c-rt:set var="forward_success" value="<%= EntityPreferenceForm.FORWARD_SUCCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= EntityPreferenceForm.FORWARD_CANCEL %>"/>
<%-- Event Handling --%>
<c:if test="${!(empty param.entityId)}">
    <x:set name="entityPreferences.form" property="entityId" value="${param.entityId}"/>
</c:if>
<c:if test="${forward.name == forward_cancel}">
    <script>window.close();</script>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>
        alert("Portlet Preferences Updated");
        window.close();
    </script>
</c:if>

<html>
<head>
    <title><fmt:message key='portlet.label.preferencePersonalization'/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="portletRow">
    <x:display name="entityPreferences.form"/>
</body>
</html>
