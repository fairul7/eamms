<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.resourcemanager.ui.MailApproveForm,
                 com.tms.collab.resourcemanager.ui.MailRejectForm"%>
<%@include file="/common/header.jsp"%>

<x:config >
    <page name="mailrejectpage">
        <com.tms.collab.resourcemanager.ui.MailRejectForm name="mailrejectForm" />
    </page>
</x:config>

<c:if test="${!empty param.eventId}" >
    <x:set name="mailrejectpage.mailrejectForm" property="eventId" value="${param.eventId}" />
</c:if>

<c:if test="${!empty param.userId}" >
    <x:set name="mailrejectpage.mailrejectForm" property="userId" value="${param.userId}" />
</c:if>

<c-rt:set var="rejected" value="<%=MailRejectForm.FORWARD_RESOURCES_REJECTED%>" />
<c:if test="${forward.name==rejected}" >
    <script>
        alert("<fmt:message key='resourcemanager.message.resourceBookingsRejected'/>!");
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
<x:display name="mailrejectpage" />
</body>

</html>
