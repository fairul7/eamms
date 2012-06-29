<%@ page import="com.tms.collab.calendar.ui.CalendarView,
                 kacang.ui.Event,
                 com.tms.collab.resourcemanager.model.ResourceManager,
                 com.tms.collab.calendar.model.Appointment,
                 com.tms.collab.calendar.model.CalendarEvent,
                 org.apache.commons.lang.StringUtils,
                 com.tms.collab.forum.model.Message,
                 com.tms.collab.calendar.model.CalendarModule"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
 <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<c:set var="acceptUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.parent.parent.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=accept</c:set>
<c:set var="deleteUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETE %></c:set>
<c:set var="deleteAllUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETEALL %></c:set>
<c:if test="${forward.name == 'Appointment deleted' }" >
    <script>
        alert("<fmt:message key="calendar.message.appointmentDeleted"/>");
        document.location = "<%= response.encodeURL(request.getRequestURI()) %>?cn=calendarPage.calendarView&et=view&view=daily";
    </script>
</c:if>
<c:if test="${forward.name == 'Appointment deleted' }" >
    <script>
        alert("<fmt:message key="calendar.message.eventDeleted"/>");
        document.location = "<%= response.encodeURL(request.getRequestURI()) %>?cn=calendarPage.calendarView&et=view&view=daily";
    </script>
</c:if>
<c:set var="event" value="${widget.event}"/>
<table cellpadding="0" cellspacing="1" width="100%">
    <tr><td class="title" align="left"><fmt:message key="calendar.label.title"/> : </td></tr>
    <tr><td class="data" align="left"><c:out value="${event.title}" /></td></tr>
    <tr><td class="title" align="left"><fmt:message key="calendar.label.date"/> : </td></tr>
    <c:choose>
        <c:when test="${!event.allDay}">
            <tr><td class="data" align="left"><fmt:formatDate pattern="${globalDatetimeShort}" value="${event.startDate}" /> - <fmt:formatDate pattern="${globalDatetimeShort}" value="${event.endDate}" /></td></tr>
        </c:when>
        <c:otherwise>
            <tr><td class="data" align="left">* <fmt:message key="calendar.label.AllDay"/> (<fmt:formatDate pattern="${globalDateShort}" value="${event.startDate}" />)</td></tr>
        </c:otherwise>
    </c:choose>
    <c:if test="${!empty event.location}">
        <tr><td class="title" align="left"><fmt:message key="calendar.label.location"/> : </td></tr>
        <tr><td class="data" align="left"><c:out value="${event.location}" /></td></tr>
    </c:if>
    <tr><td class="title" align="left"><fmt:message key="calendar.label.classification"/> : </td></tr>
    <tr>
        <td class="data" align="left">
            <c:choose>
                <c:when test="${event.classification == 'pub'}"><fmt:message key='calendar.label.public'/></c:when>
                <c:when test="${event.classification == 'pub'}"><fmt:message key='calendar.label.private'/></c:when>
                <c:otherwise><fmt:message key='calendar.label.confidential'/></c:otherwise>
            </c:choose>
        </td>
    </tr>
    <c:set var="eventId" value="${event.eventId}"/>
    <c:set var="found" value="0"/>
    <%
        String eventId = (String)pageContext.getAttribute("eventId");
        if(eventId.startsWith(Appointment.class.getName()))
        {
    %>
    <tr><td class="title" align="left"><fmt:message key="calendar.label.compulsoryAttendees"/> : </td></tr>
    <tr>
        <td class="data" align="left">
            <c:forEach var="att" items="${event.attendees}">
                <c:if test="${att.compulsory}" >
                    <c:out value="${att.name}"/><br>
                    <c:set var="found" value="1"/>
                </c:if>
            </c:forEach>
            <c:if test="${found == '0'}">-</c:if>
        </td>
    </tr>
    <tr><td class="title" align="left"><fmt:message key="calendar.label.optionalAttendees"/> : </td></tr>
    <tr>
        <td class="data" align="left">
            <c:set var="found" value="0"/>
            <c:forEach var="att" items="${event.attendees}">
                <c:if test="${!att.compulsory}">
                    [<c:out value="${att.status}"/>] <c:out value="${att.name}"/><br>
                    <c:set var="found" value="1"/>
                </c:if>
            </c:forEach>
            <c:if test="${found == '0'}">-</c:if>
        </td>
    </tr>
    <% } %>
    <c:set var="des" value="${event.description}"/>
    <c:if test="${!empty des}">
        <%
            String translated = StringUtils.replace((String)pageContext.getAttribute("des"), "\n", "<br>");
            pageContext.setAttribute("des", translated);
        %>
        <tr><td class="title" align="left"><fmt:message key="calendar.label.description"/> : </td></tr>
        <tr><td class="data" align="left"><c:out value="${des}"escapeXml="false"  /></td></tr>
    </c:if>
    <c:set var="agenda" value="${event.agenda}"/>
    <c:if test="${!empty agenda}">
        <%
            String translated = StringUtils.replace((String)pageContext.getAttribute("agenda"), "\n", "<br>");
            pageContext.setAttribute("agenda", translated);
        %>
        <tr><td class="title" align="left"><fmt:message key="calendar.label.agenda"/> : </td></tr>
        <tr><td class="data" align="left"><c:out value="${agenda}"escapeXml="false"  /></td></tr>
    </c:if>
    <%-- Determine available actions --%>
    <c:if test="${event.attendees != null}">
        <c:set var="userId" value="${widget.widgetManager.user.id}"/>
        <c:forEach items="${event.attendees}" var="attendee">
            <c-rt:set var="att_status" value="<%=CalendarModule.ATTENDEE_STATUS_PENDING%>"  />
            <c:if test="${userId == attendee.userId && attendee.status == att_status}" >
                <c:set var="attendeeId" value="${attendee.attendeeId}"/>
                <%pageContext.setAttribute("canAccept",Boolean.TRUE);%>
            </c:if>
        </c:forEach>
    </c:if>
    <c:if test="${!widget.parent.hiddenAction}" >
        <tr><td class="data" align="left">&nbsp;</td></tr>
        <tr>
            <td class="data" align="left">
                <c:if test="${widget.parent.editable}" >
                    <% if(eventId.startsWith(Appointment.class.getName())) { %>
                        <input value="Edit" type="button" class="button" onClick="document.location = 'editappointmentform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />'"/>
                    <% } else if(eventId.startsWith(CalendarEvent.class.getName())) { %>
                        <input value="Edit" type="button" class="button" onClick="document.location = 'editeventform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />'"/>
                    <%}%>
                </c:if>
                <c:if test="${widget.parent.deleteable}" >
                    <c:if test="${event.recurrence}">
                        <input type="button" class="button" value="<fmt:message key="calendar.label.buttonDelete"/>" onClick="document.location = '<c:out value="${deleteUrl}" />';"/>
                    </c:if>
                    <input type="button" class="button" value="<c:if test="${empty event.recurrenceRule}"><fmt:message key="calendar.label.buttonDelete"/></c:if><c:if test="${not empty event.recurrenceRule}"><fmt:message key="calendar.label.buttonDeleteAll"/></c:if>" onClick="document.location = '<c:out value="${deleteAllUrl}" />';"/>
                </c:if>
                <c:if test="${widget.parent.acceptReject && canAccept}" >
                    <input value="<fmt:message key="calendar.label.buttonAccept"/>" type="button" class="button" onClick="document.location = '<c:out value="${acceptUrl}" />&eventId=<c:out value="${event.eventId}"/>&instanceId=<c:out value="${event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>';"/>
                    <input value="<fmt:message key="calendar.label.buttonReject"/>" type="button" class="button" onClick="window.open('rejectform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />&instanceId=<c:out value="${event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>','rejectform','scrollbars=yes,resizable=yes,width=400,height=200');return false;"/>
                </c:if>
            </td>
        </tr>
    </c:if>
</table>

