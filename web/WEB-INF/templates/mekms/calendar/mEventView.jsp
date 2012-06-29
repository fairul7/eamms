<%@ page import="kacang.ui.Event,
                 com.tms.collab.calendar.ui.CalendarView,
                 com.tms.collab.calendar.ui.CalendarEventView,
                 java.util.Date,
                 com.tms.collab.calendar.ui.MCalendarView"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:if test="${forward.name == 'cancel'}">
    <script>
        <c:set target="${widget.calendarEventView.state}" value="0"/>
        history.back();
        history.back();
    </script>
</c:if>
<c:set var="calendar" value="${widget}"/>
<c:set var="paramDateSelect" value=""/>
<c:set var="eventUrl">viewCalendarEvent.do</c:set>
<c:set var="nextUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_NEXT%></c:set>
<c:set var="previousUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_PREVIOUS%></c:set>
<c:set var="dailyUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= MCalendarView.VIEW_M_DAILY %></c:set>
<c:set var="weeklyUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_WEEKLY %></c:set>
<c:set var="monthlyUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_MONTHLY %></c:set>
<c:set var="editUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_EDIT %></c:set>
<c:set var="deleteUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETE %></c:set>
<c:set var="deleteAllUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETEALL %></c:set>
<% pageContext.setAttribute("EDIT",String.valueOf(CalendarEventView.EDIT));%>
<table border="0" cellspacing="0" cellpadding="0" width="100%" valign="top">
	<tr>
		<td>
            <c:set value="${calendar.selectedEventId}" var="selectedEventId"  ></c:set>
            <%
                String eId = (String)pageContext.getAttribute("selectedEventId");
           	
            //    String header = eId.substring(eId.lastIndexOf(".")+1,eId.indexOf("_"));
              //  pageContext.setAttribute("headline",header);
            %>
            <a href="<c:out value="${dailyUrl}"/>&<c:out value="${paramDateSelect}"/>=<c:out value="${calendar.dayOfMonth}"/>" class="title"><fmt:formatDate value="${calendar.date}" pattern="EEEEEEE, "/><c:out value="${calendar.dayOfMonth}"/></a>
            <fmt:parseDate value="${calendar.month+1}" pattern="MM" var="fm"/>
            <fmt:formatDate pattern="MMMM" value="${fm}"/>
            <c:out value="${calendar.year}"/>
		</td>
	</tr>
	<tr><td><x:display name="${calendar.calendarEventView.absoluteName}" /></td></tr>
</table>
