<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="alertPopup">
        <com.tms.collab.calendar.ui.CalendarEventView name="view" width="100%" acceptReject="false" editable="false" deleteable="false"/>
    </page>
</x:config>

<x:set name="alertPopup.view" property="instanceId" value="${param.instanceId}"/>
<x:set name="alertPopup.view" property="eventId" value="${param.eventId}"/>
<x:set name="alertPopup.view" property="editable" value="${false}"/>
<x:set name="alertPopup.view" property="deleteable" value="${false}"/>

<html>
<head>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
    <title><fmt:message key="calendar.label.alert"/></title>
</head>

<body align="center">

<b class="contentFont"><fmt:message key="calendar.label.alert"/></b>
<hr size="1">

<x:display name="alertPopup.view"/>

<div align="center" width="100%">
<form>
<input type="button" class="button" value="<fmt:message key="calendar.label.close"/>" onclick="window.close()">
</form>
</div>

</body>
</html>
