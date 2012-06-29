<%@ page import="com.tms.collab.calendar.ui.MCalendarForm,
                 com.tms.collab.calendar.ui.MCalendarUserTable,
                 kacang.ui.WidgetManager,
                 com.tms.collab.calendar.ui.CalendarForm"%>
<%@include file="/common/header.jsp"%>
<x:config  reloadable="${param.reload}">
    <page name="calendarformpage">
        <com.tms.collab.calendar.ui.MCalendarForm name="AppointmentForm" prefix="com.tms.collab.calendar.model.CalendarEvent" />
    </page>
</x:config>
<c:if test="${forward.name=='cancel'}" >
    <c:redirect url="/mekms/calendar/calendar.jsp"/>
</c:if>
<c:if test="${forward.name=='event added'}" >
    <script>
         alert("<fmt:message key='calendar.message.newEventAddedSuccessfully'/>");
         <%
            WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
            if(wm!=null){
                CalendarForm form = (CalendarForm)wm.getWidget("calendarformpage.AppointmentForm");
         %>
         document.location = "<c:url value="/mekms/calendar/calendar.jsp" ></c:url>?cn=calendarPage.calendarView&et=select&eventId=<%out.print(form.getEvent().getEventId());}%>";
    </script>
</c:if>
<c:if test="${forward.name=='conflict exception'}">
    <% session.setAttribute("edit",Boolean.FALSE);%>
    <c:redirect url="/mekms/calendar/conflicts.jsp" ></c:redirect>
</c:if>
<c:set var="headerCaption" scope="request"><fmt:message key='calendar.label.addNewEvent'/></c:set>
<jsp:include page="../includes/mheader.jsp"/>
<table width="100%" border=0 cellPadding=2 cellSpacing=0>
    <tbody><tr valign="top"><td><x:display name="calendarformpage.AppointmentForm" /></td></tr></tbody>
</table>
<jsp:include page="../includes/mfooter.jsp" />





