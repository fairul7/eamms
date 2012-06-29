<%@ page import="com.tms.collab.calendar.model.Appointment,
                 com.tms.collab.calendar.model.CalendarEvent"%>
<%@include file="/common/header.jsp"%>
<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="appointmentformpage">
        <com.tms.collab.calendar.ui.MCalendarForm name="AppointmentForm" prefix="com.tms.collab.calendar.model.Appointment" />
    </page>
</x:config>
<c:if test="${!empty param.eventId}" >
    <x:set name="appointmentformpage.AppointmentForm" property="eventId" value="${param.eventId}" ></x:set>
</c:if>
<c:if test="${!empty param.reload}">
    <x:set name="appointmentformpage.AppointmentForm" property="populated" value="<%= Boolean.TRUE %>"/>
</c:if>
<c:if test="${forward.name=='cancel'}" >
    <c:redirect url="/mekms/calendar/calendar.jsp"/>
</c:if>
<c:if test="${forward.name=='event updated'}" >
    <script>
        alert("<fmt:message key='calendar.message.eventUpdatedSuccessfully'/>");
        document.location = "<c:url value="/mekms/calendar/calendar.jsp" />";
    </script>
</c:if>
<c:if test="${forward.name=='appointment updated'}" >
    <script>
        alert("<fmt:message key='calendar.message.appointmentUpdatedSuccessfully'/>");
        document.location = "<c:url value="/mekms/calendar/calendar.jsp" />";
    </script>
</c:if>
<c:if test="${forward.name=='conflict exception'}">
    <% session.setAttribute("edit",Boolean.TRUE); %>
    <c:redirect url="/mekms/calendar/conflictsedit.jsp"/>
</c:if>
<c:if test="${forward.name=='selectAttendees'}">
    <script>alert("<fmt:message key="calendar.message.atLeastOneAttendee"/>");</script>
</c:if>
<c:if test="${forward.name =='attendees'}">
    <c:redirect url="/mekms/calendar/attendee.jsp?init=appointmentformpage.AppointmentForm"/>
</c:if>
<c:set var="headerCaption" scope="request" value="Edit Appointment"/>
<jsp:include page="../includes/mheader.jsp" />
<table width="100%" border=0 cellPadding=5 cellSpacing=0>
    <tr valign="top" class="f07"><td width="100%"><x:display name="appointmentformpage.AppointmentForm"/></td></tr>
</table>
<jsp:include page="../includes/mfooter.jsp" />

