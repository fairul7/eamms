<%@ page import="kacang.runtime.HttpController,
                 kacang.stdui.CalendarBox,
                 com.tms.collab.calendar.ui.CalendarView,
                 kacang.ui.Event,
                 com.tms.collab.calendar.model.CalendarModule,
                 java.util.Date"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="calendar" value="${widget}"/>
<c:set var="paramDateSelect" value="${calendar.dayOfMonthName}"/>
<c:set var="eventUrl" value="${calendar.eventUrl}"/>
<c:set var="paramEventSelect" value="eventId"/>
<c:set var="isSelectedMonth" value="${calendar.month == calendar.selectedMonth}"/>
<c:set var="pDate" value="${calendar.previousDate}"/>
<c:set var="nDate" value="${calendar.nextDate}"/>
<fmt:formatDate var="monthValue" value="${nDate}" pattern="M"/>
<c:set var="monthValue" value="${monthValue-1}" />
<c:set var="nextUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_WEEKLY %>&<c:out value="${calendar.dayOfMonthName}"/>=<fmt:formatDate value="${nDate}" pattern="d"/>&<c:out value="${calendar.monthName}" />=<c:out value="${monthValue}" />&<c:out value="${calendar.yearName}" />=<fmt:formatDate value="${nDate}" pattern="yyyy"/>
</c:set>
<fmt:formatDate var="monthValue" value="${pDate}" pattern="M"/>
<c:set var="monthValue" value="${monthValue-1}" />
<c:set var="previousUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_WEEKLY %>&<c:out value="${calendar.dayOfMonthName}"/>=<fmt:formatDate value="${pDate}" pattern="d"/>&<c:out value="${calendar.monthName}" />=<c:out value="${monthValue}" />&<c:out value="${calendar.yearName}" />=<fmt:formatDate value="${pDate}" pattern="yyyy"/>
</c:set>
<c:set var="dailyUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_DAILY %>
</c:set>
<c:set var="monthlyUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_MONTHLY %>
</c:set>

<c:set var="weeklyEntries" value="${calendar.weeklyEntries}"/>

<table BORDER="0" CELLSPACING="1" CELLPADDING="3" width="100%">
<tr><td class="calendarHeader" colspan="7"><fmt:message key='calendar.label.calendar'/> > <fmt:message key='calendar.label.weeklyCalendarView'/></td></tr>
<tr> <td VALIGN="BOTTOM"  BGCOLOR="#003366" CLASS="calendarSubheader" colspan="2">

<!-- Group, User Selection -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
 <x:display name="${calendar.groupSelectForm.absoluteName}" body="custom">
 <td class="calendarSubheader" align="left" valign="top">
 <fmt:message key="security.label.group"/>
 </td>
 <td class="calendarSubheader" align="left" nowrap>
 <x:display name="${calendar.groupSelectBox.absoluteName}" />
 <br>
 <x:display name="${calendar.groupSelectButton.absoluteName}" />
 </td>
 </x:display>
 <x:display name="${calendar.userSelectForm.absoluteName}" body="custom" >
 <td class="calendarSubheader" align="left" valign="top">
 <fmt:message key="security.label.user"/>
 </td>
 <td class="calendarSubheader" align="left" nowrap>
    <x:display name="${calendar.userSelectBox.absoluteName}"/>
    <x:display name="${calendar.userSelectButton.absoluteName}" />
 </td>
</x:display>
<td align="right" valign="bottom" nowrap>
<x:display name="${calendar.jumpForm.absoluteName}" ></td></x:display>
<td align="right" valign="bottom" nowrap>
<c:if test="${!entry.today}" >
    <%
        Date today = new Date();
        pageContext.setAttribute("today",today);
    %>
    <fmt:formatDate var="monthValue" value="${today}" pattern="M"/>
    <c:set var="monthValue" value="${monthValue-1}" />
    <c:set var="todayUrl">
    <%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_DAILY %>&<c:out value="${calendar.dayOfMonthName}"/>=<fmt:formatDate value="${today}" pattern="d"/>&<c:out value="${calendar.monthName}" />=<c:out value="${monthValue}" />&<c:out value="${calendar.yearName}" />=<fmt:formatDate value="${today}" pattern="yyyy"/>
    </c:set>
    <INPUT TYPE="button" class="button" value="<fmt:message key='calendar.label.today'/>" onClick="document.location = '<c:out value="${todayUrl}"/>'">
</c:if>
</td></tr>
</table>
<!-- End Group, User Selection -->

</td></tr>

<tr BGCOLOR="#E2E2E2">
  <td VALIGN="TOP" CLASS="contentBgColor" COLSPAN="2" > <table  width="100%">
  <tr width="100%">
  <td width="20%">
      <input type="button" value="&nbsp;&nbsp;&lt;&nbsp;&nbsp;" class="button" onClick="document.location='<c:out value="${previousUrl}"/>';">
  </td>
  <td width="60%" align="center" ><b><fmt:message key='calendar.label.week'/> <c:out value="${calendar.weekOfMonth}"/>:</b>
    <c:forEach items="${weeklyEntries}" var="entry" begin="0" varStatus="status"   >
        <c:if test="${status.first }" >
           <fmt:formatDate pattern="${globalDateLong}" value="${entry.calendar.time}" />
        </c:if>
        <c:if test="${ status.last}" >
          -  <fmt:formatDate pattern="${globalDateLong}" value="${entry.calendar.time}" />
        </c:if>
    </c:forEach>

  </td>
  <td width="20%" align="right">
        <input type="button" value="&nbsp;&nbsp;&gt;&nbsp;&nbsp;" class="button" onClick="document.location='<c:out value="${nextUrl}"/>'">
  </td>
  </tr></table>
  </td>
</tr>

<c:forEach var="entry" items="${weeklyEntries}">
<tr  VALIGN="MIDDLE">
  <c:choose>
    <c:when  test="${entry.today}" >
      <TD BGCOLOR="#E2E2E2" CLASS="contentBgColor" VALIGN="TOP" ALIGN="RIGHT" width="10%"><B><FONT COLOR="#0065CE" CLASS="textFont"><fmt:message key='calendar.label.today'/></FONT>&nbsp;&nbsp;<BR>
    </c:when>
    <c:otherwise>
    <td  width="10%" class="calendarRow" VALIGN="TOP" ALIGN="RIGHT">
  </c:otherwise>
  </c:choose>
        <c:set var="dayOfWeek" value="${entry.dayOfWeekLabel}"/>
            <c:if test="${dayOfWeek=='Sun' || dayOfWeek=='Sat'}" >
                <FONT COLOR="#FF0000">
            </c:if>
        <b><c:out value="${entry.dayOfWeekLabel}"/></b></font>&nbsp;&nbsp;<br>

        <a href="<c:out value="${dailyUrl}"/>&<c:out value="${paramDateSelect}"/>=<c:out value="${entry.dayOfMonth}"/>&<c:out value="${calendar.monthName}=${entry.month}&${calendar.yearName}=${entry.year}"/>">
            <c:out value="${entry.dayOfMonth}"/></a>&nbsp;&nbsp;
  </td>
  <td  class="calendarDayBg" VALIGN="TOP">
    <c:forEach var="appointment" items="${entry.appointments}" begin="0" varStatus="status"  >
        <c:if test="${status.first}">
            <P><B><FONT COLOR="#003366"><fmt:message key='calendar.label.appointments'/></FONT></B><BR>
                          <B><FONT COLOR="#999999">. . . . . . . . . . . . . . . . . . . . . .</FONT></B>
        </c:if>
            <c:set var="event" value="${appointment}" scope="request"/>
            <jsp:include page="AppointmentSummary.jsp" flush="true"/>
        <c:if test="${status.last}">
         </p>
        </c:if>
    </c:forEach>
    <c:forEach var="meeting" items="${entry.meetings}" begin="0" varStatus="status"  >
        <c:if test="${status.first}">
            <P><B><FONT COLOR="#003366"><fmt:message key='calendar.label.e-Meetings'/></FONT></B><BR>
                          <B><FONT COLOR="#999999">. . . . . . . . . . . . . . . . . . . . . .</FONT></B>
        </c:if>
            <c:set var="meeting" value="${meeting}" scope="request"/>
            <jsp:include page="MeetingSummary.jsp" flush="true"/>
        <c:if test="${status.last}">
         </p>
        </c:if>
    </c:forEach>
   <c:forEach var="event" items="${entry.events}" begin="0" varStatus="status"  >
        <c:if test="${status.index==0}">
            <P><B><FONT COLOR="#003366"><fmt:message key='calendar.label.events'/></FONT></B><BR>
                          <B><FONT COLOR="#999999">. . . . . . . . . . . . . . . . . . . . . .</FONT></B>
        <br>
        </c:if>
<c-rt:set var="privateEvent" value="<%= CalendarModule.CLASSIFICATION_PRIVATE %>"/>
<c:choose>
<c:when test="${event.hidden}"><fmt:message key='calendar.label.privateEvent'/></c:when>
<c:otherwise>
    <c-rt:set var="cn" value="<%= Event.PARAMETER_KEY_WIDGET_NAME %>"/>
    <c-rt:set var="et" value="<%= Event.PARAMETER_KEY_EVENT_TYPE %>"/>
    <c-rt:set var="eid" value="<%= CalendarView.PARAMETER_KEY_EVENTID %>"/>
    <c-rt:set var="iid" value="<%= CalendarView.PARAMETER_KEY_INSTANCEID %>"/>
    <c:url var="eUrl" value="${eventUrl}">
    <c:param name="${eid}" value="${event.eventId}"/>
        <c:param name="${cn}" value="${calendar.absoluteName}"/>
        <c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>

        <c:param name="${iid}" value="${event.recurrenceId}"/>
        <c:if test="${event.reminder}">
            <c:param name="remindId" value="${event.id}"/>
        </c:if>
    </c:url>
    <a href="<c:out value="${eUrl}"/>">


    <c:choose>
        <c:when test="${event.eventId == calendar.selectedEventId && event.recurrenceId == calendar.selectedInstanceId}"><b><c:out value="${event.title}"/>    <c:if test="${event.reminder}">(<fmt:message key='calendar.label.REMINDER'/>)</c:if></b></c:when>
        <c:otherwise><c:out value="${event.title}"/>    <c:if test="${event.reminder}">(<fmt:message key='calendar.label.REMINDER'/>)</c:if>
</c:otherwise>
    </c:choose></a>

</c:otherwise>
</c:choose>

<c:if test="${!hideDetails}">

    <c:choose>
        <c:when test="${event.allDay}">
            ( <FONT COLOR="#FF0000"><fmt:message key='calendar.label.allDay'/></font>)
        </c:when>
        <c:when test="${event.moreThanOneDay}" >
            - <FONT COLOR="#FF0000"> <fmt:formatDate pattern="${globalDateLong}" value="${event.startDate}" />
            </font><fmt:message key='calendar.label.to'/><FONT COLOR="#FF0000"> <fmt:formatDate pattern="${globalDateLong}" value="${event.endDate}" />
        </c:when>
        <c:otherwise>
            (<FONT COLOR="#FF0000"><fmt:formatDate pattern="${globalTimeLong}" value="${event.startDate}" /> </font>
             -
             <FONT COLOR="#FF0000"><fmt:formatDate pattern="${globalTimeLong}" value="${event.endDate}" /> </font>
             )
        </c:otherwise>
    </c:choose>
    </FONT>
    <c:if test="${event.recurrence}">(<fmt:message key='calendar.label.Recurrence'/>)</c:if>
    <br>

  </c:if>
        <c:if test="${status.last}">
         </p>
        </c:if>
    </c:forEach>

    <c:forEach var="task" items="${entry.tasks}" begin="0" varStatus="status"  >
        <c:if test="${status.index==0}">
            <P><B><FONT COLOR="#003366"><fmt:message key='calendar.label.toDoList'/></FONT></B><BR>
                          <B><FONT COLOR="#999999">. . . . . . . . . . . . . . . . . . . . . .</FONT></B>
        </c:if>
            <c:set var="event" value="${task}" scope="request"/>
            <jsp:include page="TaskSummary.jsp" flush="true"/>
        <c:if test="${status.last}">
         </p>
        </c:if>
    </c:forEach>

  </td>
</tr>
<TR BGCOLOR="#E2E2E2">
                      <TD VALIGN="TOP" CLASS="contentBgColor"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="55" HEIGHT="5"></TD>
                      <TD VALIGN="TOP" CLASS="contentBgColor"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="8" HEIGHT="5"></TD>
                    </TR>
</c:forEach>

  <tr>
 <Td BGCOLOR="#003366" CLASS="calendarFooter" align="right" COLSPAN="2">
       &nbsp;</td></tr>

</table>

</form>

