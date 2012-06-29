<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.calendar.ui.CalendarForm,
                 com.tms.collab.emeeting.ui.MeetingForm"%>
<%@include file="/common/header.jsp"%>

<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="emeetingformpage">
        <com.tms.collab.emeeting.ui.MeetingFormAdd name="meetingForm" prefix="com.tms.collab.emeeting.Meeting"/>
    </page>
</x:config>

<c:if test="${forward.name=='cancel'}" >
    <script>
        document.location = "calendar.jsp";
    </script>
</c:if>

<c:if test="${forward.name=='event added'}" >
    <%--<c:set var="flagId" value="${widgets['emeetingformpage.meetingForm'].flg}" scope="session"></c:set>--%>
    <%--<c:out value="${flagId}"/>--%>
    <script>
        alert("<fmt:message key='calendar.message.newMeetingAddedSuccessfully'/>");
        window.location = "<c:url value="/ekms/calendar/editemeetingform.jsp?eventId=${widgets['emeetingformpage.meetingForm'].meeting.event.eventId}&new=1" />";
    </script>
</c:if>

<c:if test="${forward.name=='conflict exception'}">
    <%    session.setAttribute("edit",Boolean.FALSE);%>

    <c:redirect url="/ekms/calendar/meetingconflicts.jsp?new=1" ></c:redirect>

</c:if>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellpadding="5" cellspacing="1">
    <tr><td class="calendarHeader"><fmt:message key='calendar.label.calendar'/> > <fmt:message key='calendar.label.addNewEMeeting'/></td></tr>  </table>
<table width="100%" border="0" cellpadding="0" cellspacing="0"        >
    <tr><td align="center"> <x:display name="emeetingformpage.meetingForm" >
    </td></tr></x:display></table>
<table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td class="calendarFooter">&nbsp;</td></tr>  </table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>

