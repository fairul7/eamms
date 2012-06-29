<%@ page import="com.tms.collab.calendar.ui.MCalendarForm,
                 com.tms.collab.calendar.ui.MCalendarUserTable,
                 kacang.ui.WidgetManager,
                 com.tms.collab.calendar.ui.MCalendarForm"%>
<%@include file="/common/header.jsp"%>
<x:config  reloadable="${param.reload}">
    <page name="calendarformpage">
        <com.tms.collab.calendar.ui.MCalendarForm name="AppointmentForm" prefix="com.tms.collab.calendar.model.Appointment" />
    </page>
</x:config>
<c:if test="${forward.name =='appointment added' }" >
    <c:redirect url="/mekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=${widgets['calendarformpage.AppointmentForm'].event.eventId}"/>
</c:if>
<c:if test="${forward.name =='attendees'}">
    <c:redirect url="/mekms/calendar/attendee.jsp?init=calendarformpage.AppointmentForm"/>
</c:if>
<c:if test="${forward.name=='conflict exception'}">
    <% session.setAttribute("edit",Boolean.FALSE);%>
    <c:redirect url="/mekms/calendar/conflicts.jsp" ></c:redirect>
</c:if>
<c:if test="${forward.name=='cancel'}">
    <c:redirect url="/mekms/calendar/calendar.jsp?defaultDay=today"/>
</c:if>
<c:set var="headerCaption" scope="request"><fmt:message key="calendar.label.new"/></c:set>
<jsp:include page="../includes/mheader.jsp"/>
<table width="100%" border="0" cellpadding="5" cellspacing="0" valign="top">
    <tr valign="top"><td align="center" valign="top"><x:display name="calendarformpage.AppointmentForm" /></td></tr>
</table>
<jsp:include page="../includes/mfooter.jsp" />
