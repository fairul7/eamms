<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>

<x:config  reloadable="${param.reload}">
    <page name="rejectformpage">
        <com.tms.collab.calendar.ui.RejectForm name="rejectform" />

    </page>
</x:config>

<c:if test="${!empty param.eventId}" >
    <x:set name="rejectformpage.rejectform" property="eventId" value="${param.eventId}" />
</c:if>

<c:if test="${!empty param.instanceId}" >
    <x:set name="rejectformpage.rejectform" property="instanceId" value="${param.instanceId}" />
</c:if>

<c:if test="${!empty param.attendeeId}" >
    <x:set name="rejectformpage.rejectform" property="attendeeId" value="${param.attendeeId}" />
</c:if>

<c:if test="${forward.name=='rejected'}" >
    <script>
        window.opener.location.reload();
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='cancel'}" >
    <script>
        window.close();
    </script>
</c:if>

<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <title><fmt:message key='calendar.label.rejectAppointment'/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="calendarRow">
<x:display name="rejectformpage.rejectform" >
</x:display>
</body>
</html>


