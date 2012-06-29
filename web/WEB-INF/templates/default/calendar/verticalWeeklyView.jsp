<%@ page import="kacang.runtime.HttpController,
                 kacang.stdui.CalendarBox,
                 com.tms.collab.calendar.ui.CalendarView,
                 kacang.ui.Event,
                 com.tms.collab.calendar.model.CalendarHelperBean,
                 com.tms.collab.calendar.model.CalendarEvent,
                 com.tms.collab.calendar.model.Appointment,
                 com.tms.collab.taskmanager.model.Task"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="nq" value="cn=${widget.absoluteName}&et=next&${widget.weekOfMonthName}=${widget.weekOfMonth}" />
<c:set var="pq" value="cn=${widget.absoluteName}&et=prev&${widget.weekOfMonthName}=${widget.weekOfMonth}" />
<c:set var="tq" value="cn=${widget.absoluteName}&et=view&view=weekly&${widget.weekOfMonthName}=${widget.baseWeekOfMonth}&${widget.monthName}=${widget.baseMonth}&${widget.yearName}=${widget.baseYear}" />

<%
    String query = request.getQueryString();
    if(query==null)
        query= "";
    String mQuery = (String)request.getSession().getAttribute("query");
    if(mQuery==null)
        mQuery = "";
    String nextQuery,previousQuery;
    if(/*query!=null&&!query.equals("")&&*/query.indexOf("cn=")==-1&&!query.equals(mQuery)){
        request.getSession().setAttribute("query",query);
    }else{
        query = mQuery;
/*
        nextQuery = "?"+(String)pageContext.getAttribute("nq");
        previousQuery = "?"+pageContext.getAttribute("pq");
*/
    }
    nextQuery ="?"+ pageContext.getAttribute("nq")+"&"+query;
    previousQuery ="?"+pageContext.getAttribute("pq")+"&"+query ;
    String nextUrl = request.getRequestURI() + nextQuery;
    String prevousUrl = request.getRequestURI() + previousQuery;
    String nowUrl = request.getRequestURI() +"?"+ pageContext.getAttribute("tq")+"&"+query;
    pageContext.setAttribute("nextUrl",nextUrl);
    pageContext.setAttribute("previousUrl",prevousUrl);
    pageContext.setAttribute("nowUrl",nowUrl);
%>

<c:if test="${forward.name == 'dateselected'}" >
    <c:url value="/ekms/calendar/calendar.jsp" var="eUrl"   >
        <c:param name="cn" value="calendarPage.calendarView" />
        <c:param name="et" value="view"  ></c:param>
        <c:param name="view" value="daily" ></c:param>
        <c:param name="calendarPage.calendarView*day" value="${dayOfMonth}" ></c:param>
    </c:url>
    <x:set name="calendarPage.calendarView" property="date" value="${selectedDate}" ></x:set>
    <script>document.location="<c:out value="${eUrl}" escapeXml="false"/>";</script>
</c:if>
<c-rt:set var="cn" value="<%= Event.PARAMETER_KEY_WIDGET_NAME %>"/>
<c-rt:set var="et" value="<%= Event.PARAMETER_KEY_EVENT_TYPE %>"/>
<c-rt:set var="eid" value="<%= CalendarView.PARAMETER_KEY_EVENTID %>"/>
<c-rt:set var="iid" value="<%= CalendarView.PARAMETER_KEY_INSTANCEID %>"/>
<c:set var="calendar" value="${widget}"/>
<c:set var="weeklyEntries" value="${calendar.weeklyEntries}"/>
<c:set var="paramDateSelect" value="${calendar.dayOfMonthName}"/>
<table class="verticalCalendarBackground" width="100%" border="0" cellpadding="5" cellspacing="1">
    <tr><td class="verticalCalendarHeader" nowrap><fmt:message key='calendar.label.weeklyAppointments'/>:<br><fmt:message key='calendar.label.week'/> <c:out value="${calendar.weekOfMonth}" /> <fmt:message key='calendar.label.of'/> <fmt:formatDate pattern="MMMMM yyyy"value="${widget.date}" /></td></tr>
    <c:forEach var="entry" items="${weeklyEntries}">
        <tr align="center">
        <c:set var="dayOfWeek" value="${entry.dayOfWeekLabel}"/>
        <td
            <c:choose>
                <c:when test="${dayOfWeek=='Sun' || dayOfWeek=='Sat'}" >
                    class="verticalCalendarSubheaderWeekend" width="10%"
                </c:when>
                <c:otherwise>
                    class="verticalCalendarSubheaderWeekday" width="16%"
                </c:otherwise>
            </c:choose>
        >
        <x:event name="${widget.absoluteName}" type="<%=CalendarBox.PARAMETER_KEY_DATE_SELECT%>" param="${paramDateSelect}=${entry.dayOfMonth}&${calendar.monthName}=${entry.month}&${calendar.yearName}=${entry.year}" >
            <c:choose>
                <c:when  test="${entry.today}" ><b><FONT COLOR=FF0000></c:when>
                <c:otherwise> <FONT COLOR=000000>   </c:otherwise>
            </c:choose>
                <c:out value="${entry.dayOfWeekLabel}"/>
                <c:out value="${entry.dayOfMonth}"/>
        </x:event>
        <c:if  test="${entry.today}" ></b></c:if>
        </font>
        </td>
        </tr>

<tr bgcolor="#E8E8E8" class="verticalCalendarRow" valign="top">
 <%int i=0;%>
<td width="<c:choose><c:when test="${status.first||status.last}" >10%</c:when><c:otherwise>16%</c:otherwise></c:choose>">

<c:set var="hasAppointment" value="false" />
 <c:forEach var="appointment" items="${entry.eventList}" varStatus="astatus"  >
     <%
        CalendarEvent event = (CalendarEvent)pageContext.getAttribute("appointment");
        if(!event.getEventId().startsWith(Task.class.getName()))
        {
    %>
          <c:set var="hasAppointment" value="true" />
          <c:if test="${astatus.first}">
          <div id="t" style="height: 100px; width:100%; overflow: auto;" class="verticalCalendarScrollbar">
          </c:if>
               <c:choose>
            <c:when test="${!appointment.allDay}" >
             <fmt:formatDate pattern="h:mma" value="${appointment.startDate}" />
              -
             <fmt:formatDate pattern="h:mma" value="${appointment.endDate}" />
           </c:when>
           <c:otherwise><fmt:message key='calendar.label.allDay'/></c:otherwise>
           </c:choose>
             <c:url var="eUrl" value="/ekms/calendar/calendar.jsp">
                        <c:param name="${cn}" value="calendarPage.calendarView"/>
                        <c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>
                        <c:param name="${eid}" value="${appointment.eventId}"/>
                        <c:param name="${iid}" value="${appointment.recurrenceId}"/>
                    </c:url>
             <li>
             <a href="<c:out value="${eUrl}" escapeXml="false"/>" > <c:out value="${appointment.title}" />    <c:if test="${appointment.reminder}">(<fmt:message key='calendar.label.REMINDER'/>)</c:if>
</a>
             </li><br>
          <c:if test="${astatus.last}" >
            </div>
            </c:if><%i++;%>
            <%}%>
          </c:forEach>
          <%if(i==0){%>&nbsp;<%}%>


          <c:if test="${!hasAppointment}">
            &nbsp;
            <%--no appointment--%>
          </c:if>
        </td>

</tr>
</c:forEach>
    <tr bgcolor="#E8E8E8" class="verticalCalendarRow" valign="top">
    <td class="verticalCalendarSubheaderWeekend">
        <input class="button" value="<fmt:message key='calendar.label.new'/>" type="button" onClick="javascript:window.open('/ekms/calendar/appointmentformpop.jsp','addappointment','scrollbars=yes,resizable=yes,width=600,height=380')"/>
        <input type="BUTTON" class="button" value="<fmt:message key='calendar.label.previous'/>" onClick="document.location = '<c:out value="${previousUrl}"/>'"/>
        <input type="BUTTON" class="button" value="<fmt:message key='calendar.label.now'/>" onClick="document.location = '<c:out value="${nowUrl}"/>'"/>
        <INPUT TYPE="BUTTON" class="button" value="<fmt:message key='calendar.label.next'/>" onClick="document.location = '<c:out value="${nextUrl}"/>'"/>
    </td>
    </tr>
</table>
