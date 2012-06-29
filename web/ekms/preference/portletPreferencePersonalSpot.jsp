<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.portlet.portlets.personal.PersonalSpotPreference"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="portletPreferencePersonalSpot">
        <com.tms.portlet.portlets.personal.PersonalSpotPreference name="preferenceForm"/>
    </page>
</x:config>
<c-rt:set var="forward_success" value="<%= PersonalSpotPreference.FORWARD_SUCCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= PersonalSpotPreference.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_failed" value="<%= PersonalSpotPreference.FORWARD_FAILED %>"/>
<c:if test="${! empty param.entityId}">
    <x:set name="portletPreferencePersonalSpot.preferenceForm" property="entityId" value="${param.entityId}"/>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>alert("<fmt:message key='portlet.message.preference.personalSpotSaved'/>");</script>
</c:if>
<c:if test="${forward.name == forward_cancel}">
    <script>window.close();</script>
</c:if>
<html>
<head>
    <title><fmt:message key='portlet.message.preference.managingPersonalSpot'/></title>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="bookmarkRow">
    <x:display name="portletPreferencePersonalSpot.preferenceForm"/>

    <script>
    <!--
        function updateEditor() {
            try {
                editor_setmode('portletPreferencePersonalSpot.preferenceForm.textBox', 'textedit');
            } catch(e) {
            }
        }

        updateEditor();
    //-->
    </script>

</body>
</html>
