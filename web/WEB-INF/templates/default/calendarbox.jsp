<%@ page import="kacang.runtime.*, kacang.ui.*,
                 kacang.stdui.CalendarBox" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="calendar" value="${widget}"/>
<c:set var="isBaseMonth" value="${calendar.month == calendar.baseMonth}"/>
<c:set var="isSelectedMonth" value="${calendar.month == calendar.selectedMonth}"/>
<c:set var="paramDateSelect" value="${calendar.dayOfMonthName}"/>

<table style="border: 1px solid silver">

<thead>
<tr>
<c:forEach items="${calendar.daysInWeek}" var="day">
  <th><c:out value="${day}"/></th>
</c:forEach>
</tr>
</thead>

<tbody>
<c:forEach var="week" items="${calendar.calendarEntries}">
<tr>
  <c:forEach var="day" items="${week}">
  <td>
    <x:event name="${calendar.absoluteName}" type="<%= CalendarBox.PARAMETER_KEY_DATE_SELECT %>" param="${paramDateSelect}=${day}">
        <c:choose>
          <c:when test="${isSelectedMonth && calendar.selectedDayOfMonth == day}">
            <span style="font-weight: bold"><c:out value="${day}"/></span>
          </c:when>
          <c:when test="${isBaseMonth && calendar.baseDayOfMonth == day}">
            <span style="font-style: italic"><c:out value="${day}"/></span>
          </c:when>
          <c:otherwise>
            <c:out value="${day}"/>
          </c:otherwise>
        </c:choose>
    </x:event>
  </td>
  </c:forEach>
</tr>
</c:forEach>
</tbody>

<tfoot>
<tr>
  <td>
    <x:event name="${calendar.absoluteName}" type="<%= CalendarBox.PARAMETER_KEY_SCROLL_PREVIOUS %>" param="${calendar.monthName}=${calendar.month}">
        &lt;&lt;
    </x:event>
  </td>
  <td colspan="5" align="center" style="font-weight: bold">
    <fmt:parseDate value="${calendar.month+1}" pattern="MM" var="fm"/>
    <fmt:formatDate pattern="MMMM" value="${fm}"/>
    <c:out value="${calendar.year}"/>
  </td>
  <td align="right">
    <x:event name="${calendar.absoluteName}" type="<%= CalendarBox.PARAMETER_KEY_SCROLL_NEXT %>" param="${calendar.monthName}=${calendar.month}">
        &gt;&gt;
    </x:event>
  </td>
</tr>
</tfoot>

</table>