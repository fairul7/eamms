<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>
<x:config>
    <page name="emeetingPrint">
        <com.tms.collab.emeeting.ui.MeetingPrint name="form"/>
    </page>
</x:config>
<c:if test="${! empty param.eventId}">
    <x:set name="emeetingPrint.form" property="eventId" value="${param.eventId}"/>
</c:if>
<html>
<head>
    <title><fmt:message key='calendar.label.eMeetingPrintout'/></title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">

    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body>
    <x:display name="emeetingPrint.form"/>
</body>
</html>
