<%@ page import="kacang.ui.Event,
                 com.tms.collab.calendar.ui.CalendarView,
                 com.tms.collab.emeeting.ui.MeetingFormView,
                 org.apache.commons.lang.StringUtils,
                 com.tms.collab.emeeting.AgendaItem,
                 com.tms.collab.calendar.ui.UserUtil,
                 kacang.services.security.User,
                 com.tms.collab.calendar.ui.CalendarEventView"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<c-rt:set var="deleted" value="<%=MeetingFormView.FORWARD_DELETE_SUCCESSFUL%>" />
<c:if test="${forward.name == deleted}">
    <script>
        alert("E-Meeting deleted successfully!");
        document.location = "<%= response.encodeURL(request.getRequestURI()) %>?cn=calendarPage.calendarView&et=view&view=daily";
    </script>
</c:if>
<%
    String url = response.encodeURL(request.getRequestURI());
    url = url.substring(0, url.indexOf('.'));
    url += MeetingFormView.DEFAULT_ACTION_ITEM_PREFIX + ".jsp?" + CalendarView.PARAMETER_KEY_EVENTID + "=";
%>
<c:set var="meeting" value="${widget.meeting}"/>
<c:set var="widget" value="${widget}"/>
<c:if test="${meeting!=null}" >
    <c:set var="actionItemsUrl"><%= url %><c:out value="${meeting.eventId}"/></c:set>
</c:if>
<c:set var="deleteUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%=MeetingFormView.EVENT_TYPE_DELETE%></c:set>
<table cellpadding="0" cellspacing="1" width="100%">
    <tr><td class="title" align="left"><fmt:message key="emeeting.label.title"/> : </td></tr>
    <tr><td class="data" align="left"><c:out value="${meeting.event.title}" /> <c:if test="${meeting.event.recurrence}" >(<fmt:message key='emeeting.label.recurrence'/>)</c:if></td></tr>
    <c:if test="${! empty meeting.event.location}">
        <tr><td class="title" align="left"><fmt:message key="emeeting.label.location"/> : </td></tr>
        <tr><td class="data" align="left"><c:out value="${meeting.event.location}" /></td></tr>
    </c:if>
    <tr><td class="title" align="left"><fmt:message key="emeeting.label.date"/> : </td></tr>
    <c:choose>
        <c:when test="${!meeting.event.allDay}" >
            <tr><td class="data" align="left"><fmt:formatDate value="${meeting.event.startDate}" pattern="${globalDatetimeShort}"/> - <fmt:formatDate value="${meeting.event.endDate}" pattern="${globalDatetimeShort}"/></td></tr>
        </c:when>
        <c:otherwise>
            <tr><td class="data" align="left">* <fmt:message key="calendar.label.AllDay"/> (<fmt:formatDate value="${meeting.event.startDate}" pattern="${globalDatetimeShort}"/>)</td></tr>
        </c:otherwise>
    </c:choose>
    <tr><td class="title" align="left"><fmt:message key="emeeting.label.classification"/> : </td></tr>
    <tr>
        <td class="data" align="left">
            <c:choose>
                <c:when test="${meeting.event.classification == 'pub'}"><fmt:message key='emeeting.label.public'/></c:when>
                <c:when test="${meeting.event.classification == 'pub'}"><fmt:message key='emeeting.label.private'/></c:when>
                <c:otherwise><fmt:message key='emeeting.label.confidential'/></c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr><td class="title" align="left"><fmt:message key="emeeting.label.compulsoryAttendees"/> : </td></tr>
    <tr>
        <td class="data" align="left">
            <c:set var="found" value="0"/>
            <c:forEach var="att" items="${meeting.event.attendees}">
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
            <c:forEach var="att" items="${meeting.event.attendees}">
                <c:if test="${!(att.compulsory || empty att.name)}">
                    [<c:out value="${att.status}"/>] <c:out value="${att.name}"/><br>
                    <c:set var="found" value="1"/>
                </c:if>
            </c:forEach>
            <c:if test="${found == '0'}">-</c:if>
        </td>
    </tr>
    <c:if test="${!empty meeting.event.description}">
        <c:set var="des" value="${meeting.event.description}"/>
        <%
            String translated = StringUtils.replace((String)pageContext.getAttribute("des"), "\n", "<br>");
            translated = StringUtils.replace(translated, "\r", "<br>");
        %>
        <tr><td class="title" align="left"><fmt:message key="emeeting.label.description"/> : </td></tr>
        <tr><td class="data" align="left"><%= translated %></td></tr>
    </c:if>
    <c:if test="${!empty meeting.event.agenda}">
        <c:set value="${meeting.event.agenda}" var="agenda" />
        <%
            String translated = StringUtils.replace((String)pageContext.getAttribute("des"), "\n", "<br>");
            translated = StringUtils.replace(translated, "\r", "<br>");
        %>
        <tr><td class="title" align="left"><fmt:message key="emeeting.label.agendaNotes"/> : </td></tr>
        <tr><td class="data" align="left"><%= translated %></td></tr>
    </c:if>
    <c:if test="${! empty widget.relatedMeetings}">
        <tr><td class="title" align="left"><fmt:message key="emeeting.label.relatedMeetings"/> : </td></tr>
        <tr>
            <td class="data" align="left">
                <c:set var="prefix"><%= CalendarView.PARAMETER_KEY_EVENTID %></c:set>
                <c:forEach var="item" items="${widget.relatedMeetings}">
                    <c:if test="${widget.meeting.eventId != item.eventId}">
                        &#149;&nbsp;<x:event name="${widget.parent.parent.absoluteName}" type="<%= CalendarView.PARAMETER_KEY_EVENT_SELECT %>" param="${prefix}=${item.eventId}"><c:out value="${item.title}"/></x:event> (<fmt:formatDate value="${item.startDate}" pattern="${globalDatetimeShort}" />)
                    </c:if>
                </c:forEach>
                <c:if test="${empty widget.relatedMeetings[1]}">
                    <fmt:message key='emeeting.label.norelatedmeetings'/>
                </c:if>
            </td>
        </tr>
    </c:if>
    <c:if test="${!widget.parent.hiddenAction}" >
        <tr><td class="data" align="left">&nbsp;</td></tr>
        <tr>
            <td class="data" align="left">
                <c:if test="${widget.editable}" >
                    <input type="button" value="Edit" class="button"  onClick="document.location='<c:url value="/mekms/calendar/editemeetingform.jsp?eventId="/><c:out value="${meeting.eventId}"/>'"/>
                </c:if>
                <c:if test="${widget.deletable}">
                    <input type="button" class="button" value="Delete" onClick="document.location='<c:out value="${deleteUrl}"/>'"/>
                </c:if>
            </td>
        </tr>
    </c:if>
</table>