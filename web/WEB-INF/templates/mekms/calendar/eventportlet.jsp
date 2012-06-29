<%@ page import="kacang.ui.Event,
                 com.tms.collab.taskmanager.ui.TaskListing,
                 com.tms.collab.calendar.ui.CalendarView"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<link ref="stylesheet" href="images/style.css">
<c:set var="view" value="${widget}"/>
<c-rt:set var="cn" value="<%= Event.PARAMETER_KEY_WIDGET_NAME %>"/>
<c-rt:set var="et" value="<%= Event.PARAMETER_KEY_EVENT_TYPE %>"/>
<c-rt:set var="eid" value="<%= CalendarView.PARAMETER_KEY_EVENTID %>"/>
<c-rt:set var="iid" value="<%= CalendarView.PARAMETER_KEY_INSTANCEID %>"/>
<table cellpadding="0" cellspacing="0" width="100%">
    <tr >
        <td  BGCOLOR="#EFEFEF" CLASS="contentBgColor">
            <%int i=0;%>
            <c:forEach items="${view.events}" var="appointment"  >
                <li>
                    <b>
                    <c:choose>
                        <c:when test="${appointment.allDay}"><fmt:message key='calendar.label.allDay'/></c:when>
                        <c:otherwise>
                            <fmt:formatDate value="${appointment.startDate}" pattern="${globalTimeLong}" />
                        </c:otherwise>
                    </c:choose>
                    </b><br>
                    <c:url var="eUrl" value="calendar/calendar.jsp">
                        <c:param name="${cn}" value="calendarPage.calendarView"/>
                        <c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>
                        <c:param name="${eid}" value="${appointment.eventId}"/>
                        <c:param name="${iid}" value="${appointment.recurrenceId}"/>
                    </c:url>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="<c:out value="${eUrl}" ></c:out>" ><c:out value="${appointment.title}" /></a>
                </li>
                <%i++;%>
            </c:forEach>
            <%if(i==0){%><fmt:message key='calendar.label.noevent'/>.<%}%>
        </td>
    </tr>
    <tr>
        <td>
            &nbsp;
        </td>
    </tr>
    <tr>
        <td CLASS="portletFooter" >
            &nbsp;
            <input class="button" value="<fmt:message key='calendar.label.newEvent'/>" type="button" onClick="javascript:window.open('<c:url value="/ekms/calendar/eventformpop.jsp"/>','addappointment','scrollbars=yes,resizable=yes,width=600,height=420')"/>
        </td>
    </tr>
</table>
