<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.emeeting.ui.MeetingForm"%>
<%@include file="/common/header.jsp"%>

<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="emeetingformpage">
        <com.tms.collab.emeeting.ui.MeetingFormEdit name="editMeetingForm" prefix="com.tms.collab.emeeting.Meeting"/>
    </page>
</x:config>

<c:if test="${! empty param.eventId}" >

    <x:set name="emeetingformpage.editMeetingForm" property="eventId" value="${param.eventId}" ></x:set>
</c:if>

<c:choose>
    <c:when test="${!empty param.new}">
        <x:set name="emeetingformpage.editMeetingForm" property="newflag" value="1" ></x:set>
    </c:when>
    <c:otherwise>
        <x:set name="emeetingformpage.editMeetingForm" property="newflag" value="0" ></x:set>
    </c:otherwise>
</c:choose>

<c:if test="${forward.name=='cancel'}" >
    <script>
        <c:redirect url="/ekms/calendar/calendar.jsp" ></c:redirect>
    </script>
</c:if>

<c:if test="${forward.name=='meeting updated'}" >
    <script>
        alert("<fmt:message key='calendar.message.meetingUpdatedSuccessfully'/>");
        <%
                     WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
                     if(wm!=null){
                         MeetingForm form = (MeetingForm)wm.getWidget("emeetingformpage.editMeetingForm");
        %>
        document.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>?cn=calendarPage.calendarView&et=select&eventId=<%out.print(form.getMeeting().getEvent().getEventId());}%>";

    </script>
</c:if>

<c:if test="${forward.name=='conflict exception'}">
    <% session.setAttribute("edit",Boolean.TRUE);%>
    <c:redirect url="/ekms/calendar/meetingconflicts.jsp" ></c:redirect>
</c:if>

<c:if test="${forward.name=='conflict exception1'}">
    <% session.setAttribute("edit",Boolean.TRUE);%>
    <c:redirect url="/ekms/calendar/meetingconflicts.jsp?new=1" ></c:redirect>
</c:if>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />

<c:set var="mainBody"><x:display name="emeetingformpage.editMeetingForm"/></c:set>

<table width="100%" border="0" cellpadding="5" cellspacing="1">
    <tr><td class="calendarHeader"><fmt:message key='calendar.label.editE-Meeting'/> : <c:out value="${widgets['emeetingformpage.editMeetingForm'].title.value}"/></td></tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr><td align="center" width="100%"><c:out value="${mainBody}" escapeXml="false"/></td></tr>
</table>

<table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td class="calendarFooter">&nbsp;</td></tr>  </table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
