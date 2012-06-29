<%@ page import="com.tms.collab.calendar.model.CalendarModule,
                 com.tms.collab.calendar.ui.CalendarView,
                 kacang.runtime.HttpController,
                 kacang.stdui.CalendarBox,
                 kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="calendar" value="${widget}"/>
<c:set var="event" value="${event}"/>
<c:set var="eventUrl" value="${calendar.eventUrl}"/>
<c:set var="hideDetails" value="${param.hideDetails}"/>

<c:if test="${(event.userId == default_LoginUser.userId) && !hideDetails}">
    <input2 type="checkbox" name="eventId" value="<c:out value="${event.eventId}"/>">
</c:if>
<c:if test="${!hideDetails}">
    <br> <FONT COLOR="#FF0000">
    <c:choose>
        <c:when test="${event.allDay}">(<fmt:message key='calendar.label.AllDay'/>)</c:when>
        <c:otherwise>
            <fmt:formatDate pattern="h:mma" value="${event.startDate}" />
             -
             <fmt:formatDate pattern="h:mma" value="${event.endDate}" />

        </c:otherwise>
    </c:choose>    </font>
    <c:if test="${event.universal}">!<fmt:message key='calendar.label.universal'/>!</c:if>
    <c:if test="${event.reminder}">(<fmt:message key='calendar.label.Reminder'/>)</c:if>
    <c:if test="${event.recurrence}">(<fmt:message key='calendar.label.Recurrence'/>)</c:if>

</c:if>
<c-rt:set var="privateEvent" value="<%= CalendarModule.CLASSIFICATION_PRIVATE %>"/>
<br>
<c:choose>
<c:when test="${event.hidden}"><fmt:message key='calendar.label.PrivateAppointment'/></c:when>
<c:otherwise>
    <c-rt:set var="cn" value="<%= Event.PARAMETER_KEY_WIDGET_NAME %>"/>
    <c-rt:set var="et" value="<%= Event.PARAMETER_KEY_EVENT_TYPE %>"/>
    <c-rt:set var="eid" value="<%= CalendarView.PARAMETER_KEY_EVENTID %>"/>
    <c-rt:set var="iid" value="<%= CalendarView.PARAMETER_KEY_INSTANCEID %>"/>
    <c:url var="eUrl" value="${eventUrl}">
        <c:param name="${cn}" value="${calendar.absoluteName}"/>
        <c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>
        <c:param name="${eid}" value="${event.eventId}"/>
        <c:param name="${iid}" value="${event.recurrenceId}"/>
    </c:url>
    <a href="<c:out value="${eUrl}"/>">
    <c:choose>
        <c:when test="${event.eventId == calendar.selectedEventId && event.recurrenceId == calendar.selectedInstanceId}">
 <c:out value="${event.title}"/></c:when>
        <c:otherwise>
                                  <c:out value="${event.title}"/>
          </c:otherwise>
    </c:choose></a><c:if test="${event.ownerExcluded}">*<fmt:message key='calendar.label.Self-excluded'/>*</c:if>

</c:otherwise>
</c:choose>



