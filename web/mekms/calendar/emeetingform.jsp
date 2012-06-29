<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.calendar.ui.CalendarForm,
                 com.tms.collab.emeeting.ui.MMeetingForm"%>
<%@include file="/common/header.jsp"%>
<x:config reloadable="${param.reload}">
    <page name="emeetingformpage">
        <com.tms.collab.emeeting.ui.MMeetingForm name="meetingForm" prefix="com.tms.collab.emeeting.Meeting"/>
    </page>
</x:config>
<c:if test="${!empty param.reload}">
    <x:set name="emeetingformpage.editMeetingForm" property="populated" value="<%= Boolean.TRUE %>"/>
</c:if>
<c:if test="${forward.name=='cancel'}" >
    <c:redirect url="/mekms/calendar/calendar.jsp"/>
</c:if>
<c:if test="${forward.name=='selectAttendees'}">
    <script>alert("<fmt:message key="calendar.message.atLeastOneAttendee"/>");</script>
</c:if>
<c:if test="${forward.name=='event added'}" >
    <script>
         alert("<fmt:message key='calendar.message.newMeetingAddedSuccessfully'/>");
         document.location = "<c:url value="/mekms/calendar/calendar.jsp"/>";
    </script>
</c:if>
<c:if test="${forward.name=='conflict exception'}">
    <% session.setAttribute("edit",Boolean.FALSE);%>
    <c:redirect url="/mekms/calendar/meetingconflicts.jsp" ></c:redirect>
</c:if>
<c:if test="${forward.name =='attendees'}">
    <c:redirect url="/mekms/calendar/attendee.jsp?init=emeetingformpage.meetingForm"/>
</c:if>
<c:set var="headerCaption" scope="request"><fmt:message key='calendar.label.addNewEMeeting'/></c:set>
<jsp:include page="../includes/mheader.jsp"/>
<table width="100%" border=0 cellPadding=2 cellSpacing=0>
    <tbody><tr valign="top"><td><x:display name="emeetingformpage.meetingForm" /></td></tr></tbody>
</table>
<jsp:include page="../includes/mfooter.jsp" />

