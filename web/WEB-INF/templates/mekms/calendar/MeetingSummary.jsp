<%@ page import="com.tms.collab.calendar.model.CalendarModule,
                 com.tms.collab.calendar.ui.CalendarView,
                 kacang.runtime.HttpController,
                 kacang.stdui.CalendarBox,
                 kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="calendar" value="${widget}"/>
<c:set var="meeting" value="${meeting}"/>
<c:set var="eventUrl" value="${calendar.eventUrl}"/>
<c:set var="hideDetails" value="${param.hideDetails}"/>

<c:if test="${(meeting.event.userId == default_LoginUser.userId) && !hideDetails}">
    <input2 type="checkbox" name="eventId" value="<c:out value="${meeting.eventId}"/>">
</c:if>
<c:if test="${!hideDetails}">
    <br> <FONT COLOR="#FF0000">
    <c:choose>
        <c:when test="${meeting.event.allDay}">(<fmt:message key='calendar.label.allDay'/>)</c:when>
        <c:otherwise>
            <fmt:formatDate pattern="h:mma" value="${meeting.event.startDate}" />
             -
             <fmt:formatDate pattern="h:mma" value="${meeting.event.endDate}" />

        </c:otherwise>
    </c:choose>    </font>
    <c:if test="${meeting.event.universal}">!<fmt:message key='calendar.label.UNIVERSAL'/>!</c:if>
    <c:if test="${meeting.event.reminder}">(<fmt:message key='calendar.label.REMINDER'/>)</c:if>
    <c:if test="${meeting.event.recurrence}">(<fmt:message key='calendar.label.Recurrence'/>)</c:if>
    <c:if test="${meeting.event.ownerExcluded}">*<fmt:message key='calendar.label.self-excluded'/>*</c:if>
</c:if>
<c-rt:set var="privateEvent" value="<%= CalendarModule.CLASSIFICATION_PRIVATE %>"/>
<br>
<c:choose>
<c:when test="${meeting.event.hidden}"><fmt:message key='calendar.label.privateMeeting'/></c:when>
<c:otherwise>
    <c-rt:set var="cn" value="<%= Event.PARAMETER_KEY_WIDGET_NAME %>"/>
    <c-rt:set var="et" value="<%= Event.PARAMETER_KEY_EVENT_TYPE %>"/>
    <c-rt:set var="eid" value="<%= CalendarView.PARAMETER_KEY_EVENTID %>"/>
    <c-rt:set var="iid" value="<%= CalendarView.PARAMETER_KEY_INSTANCEID %>"/>
    <c:url var="eUrl" value="${eventUrl}">
        <c:param name="${cn}" value="${calendar.absoluteName}"/>
        <c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>
        <c:param name="${eid}" value="${meeting.eventId}"/>
        <c:param name="${iid}" value="${meeting.event.recurrenceId}"/>
    </c:url>
    <a href="<c:out value="${eUrl}"/>">
    <c:choose>
        <c:when test="${meeting.eventId == calendar.selectedEventId && meeting.event.recurrenceId == calendar.selectedInstanceId}"><b>
 <c:out value="${meeting.event.title}"/></b></c:when>
        <c:otherwise>
                                  <c:out value="${meeting.event.title}"/>
          </c:otherwise>
    </c:choose></a>

</c:otherwise>
</c:choose>



