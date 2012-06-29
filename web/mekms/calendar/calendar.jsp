<%@ page import="com.tms.portlet.ui.PortalServer,
                 com.tms.collab.calendar.ui.MCalendarView"%>
<%@ include file="/common/header.jsp" %>
<x:config >
    <page name="calendarPage">
        <com.tms.collab.calendar.ui.MCalendarView name="calendarView" view="mDaily"/>
    </page>
</x:config>
<%
    String sEventId = request.getParameter("eventId")==null?"":request.getParameter("eventId");
    String sHeader = "";
    String et = request.getParameter("et")==null?"":request.getParameter("et");
    String mobile = request.getParameter("mobile")==null?"":request.getParameter("mobile");
    if (sEventId.equals("")) {
        if (mobile.equals(""))
            sEventId = request.getParameter("taskId")==null?"":request.getParameter("taskId");
    }
    if (!sEventId.equals("")) {
        sHeader = sEventId.substring(sEventId.lastIndexOf(".")+1,sEventId.indexOf("_"));
    }
    else {
        if (et.equals("complete")&&mobile.equals("")) {
            sHeader="Task";
            sEventId="Task";
        }
    }
    if (et.equals("delete")) {
        sHeader="";sEventId="";
    }
    request.setAttribute("headerCaption", sHeader);
%>
<% if (!sEventId.equals("")) { %>
    <x:set name="calendarPage.calendarView" property="view" value="<%=MCalendarView.VIEW_M_EVENT%>"/>
<% if (!sEventId.equals("Task")) { %>
    <x:set name="calendarPage.calendarView" property="selectedEventId" value="<%=sEventId%>"/>
<% } %>
<% } else { %>
    <x:set name="calendarPage.calendarView" property="view" value="<%=MCalendarView.VIEW_M_DAILY%>"></x:set>
<% } %>
<x:set name="calendarPage.calendarView" property="eventUrl" value="calendar.jsp"/>
<c:if test="${forward.name=='Appointment deleted'}">
    <%--<script>alert("<fmt:message key='calendar.message.appointmentDeleted'/>");</script>--%>
    <x:set name="calendarPage.calendarView" property="view" value="<%=MCalendarView.VIEW_M_DAILY%>" ></x:set>
</c:if>
<c:if test="${forward.name=='CalendarEvent deleted'}">
    <%--<script>alert("<fmt:message key='calendar.message.eventDeleted'/>");</script>--%>
    <x:set name="calendarPage.calendarView" property="view" value="<%=MCalendarView.VIEW_M_DAILY%>" ></x:set>
</c:if>
<c:if test="${forward.name=='delete successful'}">
    <%--<script>alert("<fmt:message key='calendar.message.toDoTaskDeleted'/>");</script>--%>
    <x:set name="calendarPage.calendarView" property="view" value="<%=MCalendarView.VIEW_M_DAILY%>" ></x:set>
    <c:redirect url="eventList.jsp?eventType=2"/>
</c:if>
<jsp:include page="../includes/mheader.jsp" />
<table width="100%" border="0" cellpadding="5" cellspacing="0" valign="top">
    <tr valign="top"><td align="center" valign="top"><x:display name="calendarPage.calendarView" /></td></tr>
</table>
<jsp:include page="../includes/mfooter.jsp" />
