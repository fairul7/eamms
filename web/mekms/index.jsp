<%@ page import="kacang.services.security.SecurityService,
                 kacang.Application,
                 kacang.services.security.User,
                 com.tms.collab.calendar.model.CalendarModule,
                 java.util.Date,
                 com.tms.collab.calendar.model.Appointment,
                 com.tms.collab.emeeting.Meeting,
                 com.tms.collab.calendar.model.CalendarEvent,
                 com.tms.collab.taskmanager.model.TaskManager,
                 java.util.Calendar"%>
<%@ include file="/common/header.jsp" %>
<%
    SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
    User user = security.getCurrentUser(request);

    CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
    Date date = Calendar.getInstance().getTime();
    int numAppts = cm.getCalendarEventsCount(null,Appointment.class.getName(),date,user.getId(),new String[]{user.getId()},null,false,false,false);
    int numMeetings = cm.getCalendarEventsCount(null,Meeting.class.getName(),date,user.getId(),new String[]{user.getId()},null,false,false,false);
    int numEvents = cm.getCalendarEventsCount(null,CalendarEvent.class.getName(),date,user.getId(),new String[]{},null,false,false,false);
    TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.DAY_OF_MONTH,1);
    cal.set(Calendar.HOUR_OF_DAY,0);
    cal.set(Calendar.MINUTE,0);
    cal.set(Calendar.SECOND,0);
    cal.set(Calendar.MILLISECOND,0);
    int numTasks = tm.countCalendarTasks(cal.getTime(),new String[]{user.getId()},true);
%>
<jsp:include page="includes/mheader.jsp" flush="true" />
<table border="0" cellpadding="0" cellspacing="8" align="center">
    <tr><td align="center" class="body"><a href="<c:url value="/mekms/calendar/eventList.jsp?eventType=2"/>" class="summary">Tasks</a> (<%= numTasks %>)</td></tr>
    <tr><td align="center" class="body"><a href="<c:url value="/mekms/calendar/eventList.jsp?eventType=1"/>" class="summary">Appointment</a> (<%= numAppts %>) </td></tr>
    <tr><td align="center" class="body"><a href="<c:url value="/mekms/calendar/eventList.jsp?eventType=3"/>" class="summary">E-Meeting</a> (<%= numMeetings %>) </td></tr>
    <tr><td align="center" class="body"><a href="<c:url value="/mekms/calendar/eventList.jsp?eventType=4"/>" class="summary">Event</a> (<%=  numEvents %>)</td></tr>
    <%--<tr><td align="center" class="body"><a href="<c:url value="/mekms/"/>" class="summary">Messages</a> (0)</td></tr>--%>
    <tr><td align="center" class="body"><a href="<c:url value="/mekms/logout.jsp"/>" class="summary">Logout</a></td></tr>
</table>
<jsp:include page="includes/mfooter.jsp" flush="true" />