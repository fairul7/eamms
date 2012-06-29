<%@ page import="com.tms.portlet.ui.PortalServer,
                 com.tms.collab.calendar.ui.CalendarView,
                 com.tms.collab.calendar.model.CalendarEvent,
                 com.tms.collab.calendar.model.Appointment,
                 com.tms.collab.emeeting.Meeting,
                 com.tms.collab.taskmanager.model.Task"%>
<%@ include file="/common/header.jsp" %>
<x:config >
    <page name="mcalendarEventList">
        <com.tms.collab.calendar.ui.MCalendarEventListTable name="table" width="100%" template="calendar/mEventListTable" pageSize="10"/>
    </page>
</x:config>
<c:choose>
    <c:when test="${param.et == 'sel' && !empty param.eventId}">
        <c:redirect url="/mekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=${param.eventId}" />
    </c:when>
</c:choose>
<c:choose>
    <c:when test="${!empty param.eventType}" >
        <c:set var="eventType"  value="${param.eventType}" scope="session" />
    </c:when>
    <c:otherwise>
        <c:set var="eventType"  value="${sessionScope.eventType}"  />
    </c:otherwise>
</c:choose>
<c-rt:set var="event" value="<%= CalendarEvent.class.getName()%>"/>
<c-rt:set var="appointment" value="<%= Appointment.class.getName()%>"/>
<c-rt:set var="meeting" value="<%= Meeting.class.getName()%>"/>
<c-rt:set var="task" value="<%= Task.class.getName()%>"/>
<c:set var="headerCaption" value="Today's Appointment" scope="request"/>
<c:choose>
    <c:when test="${eventType=='1'}" >
        <x:set name="mcalendarEventList.table" property="eventType" value="${appointment}"/>
    </c:when>
    <c:when test="${eventType==2}" >
        <c:set var="headerCaption" value="Tasks" scope="request"/>
        <x:set name="mcalendarEventList.table" property="eventType" value="${task}"/>
    </c:when>
    <c:when test="${eventType=='3'}" >
        <c:set var="headerCaption" value="Today's Meetings" scope="request"/>
        <x:set name="mcalendarEventList.table" property="eventType" value="${meeting}"/>
    </c:when>
    <c:when test="${eventType=='4'}" >
        <c:set var="headerCaption" value="Today's Events" scope="request"/>
        <x:set name="mcalendarEventList.table" property="eventType" value="${event}"/>
    </c:when>
    <c:otherwise>
        <x:set name="mcalendarEventList.table" property="eventType" value="${event}"/>
    </c:otherwise>
</c:choose>
<jsp:include page="../includes/mheader.jsp" />
<table width="100%" border=0 cellpadding=2 cellspacing=0>
    <tbody>
        <tr valign="top">
            <td>
                <table width="100%" border="0" cellpadding="3" cellspacing="0" valign="top" align="left" class="contentBgColor">
                    <tr><td class="calendarHeader"><font class="f06"><strong><c:out value="${subheader}" /></strong></font></td></tr>
                    <tr valign="top"><td align="center" valign="top"><x:display name="mcalendarEventList.table" /></td></tr>
                </table>
            </td>
        </tr>
    </tbody>
</table>
<jsp:include page="../includes/mfooter.jsp" />