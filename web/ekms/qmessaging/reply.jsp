<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.qmessaging.ui.QuickMessenger,
                 com.tms.collab.qmessaging.ui.QuickMessengerForm"%>
<%@ include file="/common/header.jsp" %>

<x:permission module="kacang.services.security.SecurityService" permission="kacang.services.security.ekms.ekmsUser">

<x:config>
    <page name="qMessagingReply">
        <com.tms.collab.qmessaging.ui.QuickMessengerReply name="form"/>
    </page>
</x:config>
<%-- Constants Initialization --%>
<c-rt:set var="key_message" value="<%= QuickMessenger.ATTRIBUTE_MESSAGE_KEY %>"/>
<c-rt:set var="forward_cancel" value="<%= QuickMessengerForm.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_success" value="<%= QuickMessengerForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_failed" value="<%= QuickMessengerForm.FORWARD_FAILED %>"/>
<%-- Event Handling --%>
<c:if test="${!empty param[key_message]}">
    <x:set name="qMessagingReply.form" property="messageId" value="${param[key_message]}"/>
</c:if>
<c:if test="${forward.name == forward_cancel}">
    <script>window.close();</script>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>
        alert("Message Sent");
        window.close();
    </script>
</c:if>
<html>
<head>
    <title><fmt:message key='qmessaging.label.replyingQuickMessage'/></title>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="qMessagingRow">
    <x:display name="qMessagingReply.form"/>
</body>
</html>

</x:permission>
