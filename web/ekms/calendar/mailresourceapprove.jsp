<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.resourcemanager.ui.MailApproveForm"%>
<%@include file="/common/header.jsp"%>

<x:config >
    <page name="mailapprovepage">
        <com.tms.collab.resourcemanager.ui.MailApproveForm name="mailapproveForm" />
    </page>
</x:config>

<c:if test="${!empty param.eventId}" >
    <x:set name="mailapprovepage.mailapproveForm" property="eventId" value="${param.eventId}" />
</c:if>

<c:if test="${!empty param.userId}" >
    <x:set name="mailapprovepage.mailapproveForm" property="userId" value="${param.userId}" />
</c:if>

<c-rt:set var="approved" value="<%=MailApproveForm.FORWARD_RESOURCES_APPROVED%>" />
<c:if test="${forward.name==approved}" >
    <script>
        alert("<fmt:message key='resourcemanager.message.resourceBookingsApproved'/>!");
        window.close();
    </script>
</c:if>

<html>
<jsp:include page="/ekms/init.jsp"/>
<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
<title>
    <fmt:message key='resourcemanager.message.resourceBookingsApproval'/>
</title>

<body>
<x:display name="mailapprovepage" />
</body>

</html>
