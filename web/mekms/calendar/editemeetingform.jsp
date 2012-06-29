<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.emeeting.ui.MeetingForm"%>
<%@include file="/common/header.jsp"%>
<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="emeetingformpage">
        <com.tms.collab.emeeting.ui.MMeetingForm name="editMeetingForm" prefix="com.tms.collab.emeeting.Meeting"/>
    </page>
</x:config>
<c:if test="${! empty param.eventId}" >
    <x:set name="emeetingformpage.editMeetingForm" property="eventId" value="${param.eventId}" ></x:set>
</c:if>
<c:if test="${forward.name=='cancel'}" >
    <c:redirect url="/mekms/calendar/calendar.jsp"/>
</c:if>
<c:if test="${!empty param.reload}">
    <x:set name="emeetingformpage.editMeetingForm" property="populated" value="<%= Boolean.TRUE %>"/>
</c:if>
<c:if test="${forward.name=='event updated'}" >
    <script>
        alert("<fmt:message key='calendar.message.meetingUpdatedSuccessfully'/>");
        <%
            WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
            if(wm!=null){
                MeetingForm form = (MeetingForm)wm.getWidget("emeetingformpage.editMeetingForm");
        %>
        document.location = "<c:url value="/mekms/calendar/calendar.jsp" ></c:url>?cn=calendarPage.calendarView&et=select&eventId=<%out.print(form.getMeeting().getEvent().getEventId());}%>";
    </script>
</c:if>
<c:if test="${forward.name=='conflict exception'}">
    <% session.setAttribute("edit",Boolean.TRUE);%>
    <c:redirect url="/mekms/calendar/meetingconflicts.jsp" ></c:redirect>
</c:if>
<c:if test="${forward.name =='attendees'}">
    <c:redirect url="/mekms/calendar/attendee.jsp?init=emeetingformpage.editMeetingForm"/>
</c:if>
<c:set var="headerCaption" scope="request"><fmt:message key="calendar.label.e-Meetings"/></c:set>
<jsp:include page="../includes/mheader.jsp"/>
<table width="100%" border=0 cellPadding=2 cellSpacing=0>
    <tr valign="top"><td><x:display name="emeetingformpage.editMeetingForm"/></td></tr>
</table>
<jsp:include page="../includes/mfooter.jsp" />