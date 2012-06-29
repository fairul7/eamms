<%@ page import="com.tms.collab.calendar.model.Appointment,
                 com.tms.collab.calendar.model.CalendarEvent,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>

<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="appointmentformpage">
        <calendarform name="AppointmentForm" prefix="com.tms.collab.calendar.model.Appointment" />
    </page>
</x:config>

<c:if test="${!empty param.eventId}" >
    <x:set name="appointmentformpage.AppointmentForm" property="eventId" value="${param.eventId}" ></x:set>
</c:if>

<c:if test="${forward.name=='cancel'}" >
    <script>
        history.back();
        history.back();

    </script>
</c:if>

<c:if test="${forward.name=='event updated'}" >
    <script>
        alert("<fmt:message key='calendar.message.eventUpdatedSuccessfully'/>");
        document.location = "<c:url value="/ekms/calendar/calendar.jsp" />";
    </script>
</c:if>

<c:if test="${forward.name=='appointment updated'}" >
    <script>
        alert("<fmt:message key='calendar.message.appointmentUpdatedSuccessfully'/>");
        document.location = "<c:url value="/ekms/calendar/calendar.jsp" />";
    </script>
</c:if>

<html>              <jsp:include page="/ekms/init.jsp"/>
<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">

<body>
<table width="100%" border="0" cellpadding="5" cellspacing="1">

    <tr><td class="calendarHeader"><fmt:message key='calendar.label.editAppointment'/></td></tr>

</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0"        >
    <tr><td align="center"><x:display name="appointmentformpage.AppointmentForm" >
    </td></tr></x:display></table>
<table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td class="calendarFooter">&nbsp;</td></tr>  </table>
</body>
</html>
