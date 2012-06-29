<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 kacang.ui.Event,
                 com.tms.collab.calendar.ui.CalendarView"%>
<%@include file="/common/header.jsp"%>

<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="appointmentformpage">
        <calendarform name="AppointmentForm" prefix="com.tms.collab.calendar.model.Appointment"/>
    </page>
</x:config>

<c:set var="deleteUrl">
    <%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETE %>
</c:set>

<c:if test="${not empty param.id}" >
    <x:set name="appointmentformpage" property="eventId" value="${param.id}" ></x:set>
</c:if>

<c:if test="${forward.name=='cancel'}" >
    <script>
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='event added'}" >
    <script>
        alert("<fmt:message key='calendar.message.newEventAddedSuccessfully'/>");
        window.opener.location.reload();
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='event updated'}" >
    <script>
        alert("<fmt:message key='calendar.message.appointmentUploadedSuccessfully'/>");
        window.opener.location.reload();
        window.close();
    </script>
</c:if>

<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <title><fmt:message key='calendar.label.editEvent'/></title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="calendarRow">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
    <Tr>
        <td align="center" height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
            <fmt:message key='calendar.label.editEvent'/>
        </font></b></td>

    </tr>     <Tr><td>
    <x:display name="appointmentformpage.AppointmentForm" >
</td></tr></table></x:display>
</body>
</html>

