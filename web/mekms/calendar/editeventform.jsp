<%@ page import="com.tms.collab.calendar.model.Appointment,
    com.tms.collab.calendar.model.CalendarEvent"%>
<%@include file="/common/header.jsp"%>
<x:config reloadable="${param.reload}">
    <page name="calendarformpage">
        <com.tms.collab.calendar.ui.MCalendarForm name="AppointmentForm" prefix="com.tms.collab.calendar.model.CalendarEvent"/>
    </page>
</x:config>
<c:if test="${!empty param.eventId}" >
    <x:set name="calendarformpage.AppointmentForm" property="eventId" value="${param.eventId}" ></x:set>
</c:if>
<c:if test="${forward.name=='cancel'}" >
    <c:redirect url="/mekms/calendar/calendar.jsp" ></c:redirect>
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
    <c:redirect url="/mekms/calendar/conflicts.jsp"/>
</c:if>
<c:set var="headerCaption" scope="request"><fmt:message key="calendar.label.editEvent"/></c:set> 
<jsp:include page="../includes/mheader.jsp"/>
<table width="100%" border=0 cellPadding=2 cellSpacing=0>
    <tbody><tr valign="top"><td><x:display name="calendarformpage.AppointmentForm"/></td></tr></tbody>
</table>
<jsp:include page="../includes/mfooter.jsp" />


