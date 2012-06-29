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

<c-rt:set var="privateEvent" value="<%= CalendarModule.CLASSIFICATION_PRIVATE %>"/>
<c:choose>
<c:when test="${event.hidden}">
    <fmt:message key='calendar.label.privateEvent'/>
</c:when>
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
        <c:when test="${event.eventId == calendar.selectedEventId && event.recurrenceId == calendar.selectedInstanceId}"><b><c:out value="${event.title}"/></b></c:when>
        <c:otherwise><c:out value="${event.title}"/></c:otherwise>
    </c:choose></a>

</c:otherwise>
</c:choose>

<c:if test="${!hideDetails}">
    <br>
    <c:choose>
        <c:when test="${event.allDay}">
            (<fmt:message key='calendar.label.allDay'/>)
        </c:when>
        <c:otherwise>
            (<fmt:formatDate pattern="${globalTimeLong}" value="${event.startDate}" />
             <br>-
             <fmt:formatDate pattern="${globalTimeLong}" value="${event.endDate}" />
             )
        </c:otherwise>
    </c:choose>
<%--
    <c:if test="${event.universal}">!UNIVERSAL!</c:if>
    <c:if test="${event.reminder}">!REMINDER!</c:if>
    <c:if test="${event.recurrence}">!REPEAT!</c:if>
--%>
</c:if>

