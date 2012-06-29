<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.portlet.portlets.webradio.WebRadioPreference"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="portletPreferenceWebRadio">
        <com.tms.portlet.portlets.webradio.WebRadioPreference name="preferenceForm"/>
    </page>
</x:config>
<c-rt:set var="forward_success" value="<%= WebRadioPreference.FORWARD_SUCCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= WebRadioPreference.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_failed" value="<%= WebRadioPreference.FORWARD_FAILED %>"/>
<c:if test="${! empty param.entityId}">
    <x:set name="portletPreferenceWebRadio.preferenceForm" property="entityId" value="${param.entityId}"/>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>alert("<fmt:message key='portlet.message.preference.channelsConfigurationSaved'/>");</script>
</c:if>
<c:if test="${forward.name == forward_cancel}">
    <script>window.close();</script>
</c:if>
<html>
<head>
    <title><fmt:message key='portlet.message.preference.managingWebRadioStations'/></title>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="radioRow">
    <x:display name="portletPreferenceWebRadio.preferenceForm"/>
</body>
</html>
