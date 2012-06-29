<%@include file="/common/header.jsp"%>
<x:config  reloadable="${param.reload}">
    <page name="calendarformattendee">
        <com.tms.collab.calendar.ui.MAttendeeTable name="table"/>
    </page>
</x:config>
<c:if test="${!empty param.init}">
    <x:set name="calendarformattendee.table" value="${param.init}" property="callingWidget"/>
</c:if>
<c:url var="redirect" value="/mekms/calendar/calendarform.jsp"/>
<c:set var="origin" value="${widgets['calendarformattendee.table'].callingWidget}"/>
<c:choose>
    <c:when test="${origin=='appointmentformpage.AppointmentForm'}">
        <c:url var="redirect" value="/mekms/calendar/editappointmentform.jsp?reload=false"/>
    </c:when>
    <c:when test="${origin=='emeetingformpage.editMeetingForm'}">
        <c:url var="redirect" value="/mekms/calendar/editemeetingform.jsp?reload=false"/>
    </c:when>
    <c:when test="${origin=='emeetingformpage.meetingForm'}">
        <c:url var="redirect" value="/mekms/calendar/emeetingform.jsp?reload=false"/>
    </c:when>
</c:choose>
<c:set var="headerCaption" scope="request"><fmt:message key="calendar.label.Attendees"/></c:set>
<jsp:include page="../includes/mheader.jsp"/>
<table width="100%" border="0" cellpadding="5" cellspacing="0" valign="top">
    <tr valign="top">
        <td align="left" valign="top">
            <table cellpadding="0" cellspacing="1" width="100%">
                <tr><td align="left" valign="top" class="data"><x:display name="calendarformattendee"/></td></tr>
                <tr>
                    <td align="left" valign="top" class="data">
                        <input type="button" value="<fmt:message key="general.label.submit"/>" class="button" onClick="document.location='<c:out value="${redirect}"/>';">
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<jsp:include page="../includes/mfooter.jsp" />
