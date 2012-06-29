<%@ page import="kacang.runtime.*, kacang.ui.*,
                 com.tms.collab.calendar.ui.CalendarView,
                 kacang.stdui.CalendarBox" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="calendar" value="${widget}"/>
<c:set var="isBaseMonth" value="${calendar.month == calendar.baseMonth}"/>
<c:set var="isSelectedMonth" value="${calendar.month == calendar.selectedMonth}"/>
<c:set var="paramDateSelect" value="${calendar.dayOfMonthName}"/>
<c:set var="nextUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarBox.PARAMETER_KEY_SCROLL_NEXT %>&<c:out value="${calendar.monthName}"/>=<c:out value="${calendar.month}"/>
</c:set>
<c:set var="previousUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarBox.PARAMETER_KEY_SCROLL_PREVIOUS %>&<c:out value="${calendar.monthName}"/>=<c:out value="${calendar.month}"/>
</c:set>
<c:set var="dailyUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_DAILY %>
</c:set>
<c:set var="weeklyUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_WEEKLY %>
</c:set>

<br>

<table cellspacing="0" style="border: 1px solid silver">

<thead>
<tr>
  <td width="15%">
    <a href="<c:out value="${previousUrl}"/>">&lt;&lt;</a>
  </td>
  <td width="70%" colspan="5" align="center" style="font-weight: bold">
    <fmt:parseDate value="${calendar.month+1}" pattern="MM" var="fm"/>
    <fmt:formatDate pattern="MMMM" value="${fm}"/>
    <c:out value="${calendar.year}"/>
  </td>
  <td width="15%" align="right">
    <a href="<c:out value="${nextUrl}"/>">&gt;&gt;</a>
  </td>
</tr>
</thead>


<thead>
<tr>
<c:forEach items="${calendar.daysInWeek}" var="day">
  <th style="border: 1px solid silver"><c:out value="${day}"/></th>
</c:forEach>
</tr>
</thead>

<tbody>
<c:forEach var="week" items="${calendar.monthlyEntries}">
<tr>
  <c:forEach var="entry" items="${week}">
  <td valign="top" style="border: 1px solid silver; height:50px; width: 80px; background: white">
    <div align="center">
    <c:choose>
      <c:when test="${isSelectedMonth && calendar.selectedDayOfMonth == entry.dayOfMonth}">
        <span style="font-weight: bold"><c:out value="${entry.dayOfMonth}"/></span>
      </c:when>
      <c:when test="${isBaseMonth && calendar.baseDayOfMonth == entry.dayOfMonth}">
        <span style="font-style: italic"><c:out value="${entry.dayOfMonth}"/></span>
      </c:when>
      <c:otherwise>
        <c:out value="${entry.dayOfMonth}"/>
      </c:otherwise>
    </c:choose>
    </div>
    <div>
    <c:forEach var="event" items="${entry.eventList}">
        <li>
            <c:set var="event" value="${event}" scope="request"/>
            <jsp:include page="calendarEventSummary.jsp" flush="true"/>
        </li>
    </c:forEach>
    </div>
  </td>
  </c:forEach>
</tr>
</c:forEach>
</tbody>

<tfoot>
<tr>
  <td colspan="7" align="right">
  </td>
</tr>
</tfoot>

</table>
