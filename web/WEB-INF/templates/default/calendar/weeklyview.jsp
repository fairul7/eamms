<%@ page import="kacang.runtime.HttpController,
                 kacang.stdui.CalendarBox,
                 com.tms.collab.calendar.ui.CalendarView,
                 kacang.ui.Event,
                 com.tms.collab.calendar.model.CalendarHelperBean,
                 com.tms.collab.calendar.model.CalendarEvent,
                 com.tms.collab.calendar.model.Appointment,
                 com.tms.collab.taskmanager.model.Task,
                 java.util.Calendar,
                 kacang.Application,
                 kacang.ui.Widget,
                 kacang.services.security.User,
                 java.util.ResourceBundle,
                 kacang.ui.WidgetManager,
                 java.util.Locale,
                 kacang.util.ApplicationResourceBundle,
                 java.util.StringTokenizer"%>
<%@ page import="kacang.services.security.SecurityService"%>
<%@ page import="com.tms.cms.medialib.model.AlbumModule"%>
<%@ page import="com.tms.cms.medialib.model.LibraryModule"%>
<%@ include file="/common/header.jsp" %>
<c:set var="nq" value="cn=${widget.absoluteName}&et=next&${widget.weekOfMonthName}=${widget.weekOfMonth}" />
<c:set var="pq" value="cn=${widget.absoluteName}&et=prev&${widget.weekOfMonthName}=${widget.weekOfMonth}" />
<c:set var="tq" value="cn=${widget.absoluteName}&et=view&view=weekly&${widget.weekOfMonthName}=${widget.baseWeekOfMonth}&${widget.monthName}=${widget.baseMonth}&${widget.yearName}=${widget.baseYear}" />
<%
    String query = request.getQueryString();
    if(query==null)
        query= "";
    String mQuery = (String)session.getAttribute("weeklyViewQuery");
    if(mQuery==null)
        mQuery = "";
    String nextQuery,previousQuery;
    if(query.indexOf("cn=")==-1&&!query.equals(mQuery)){
            session.setAttribute("weeklyViewQuery",query);
    }else{
        query = mQuery;
    }
    nextQuery ="?"+ pageContext.getAttribute("nq")+"&"+query;
    previousQuery ="?"+pageContext.getAttribute("pq")+"&"+query ;
    String nextUrl = request.getRequestURI() + nextQuery;
    String prevousUrl = request.getRequestURI() + previousQuery;
    String nowUrl = request.getRequestURI() +"?"+ pageContext.getAttribute("tq")+"&"+query;
    pageContext.setAttribute("nextUrl",nextUrl);
    pageContext.setAttribute("previousUrl",prevousUrl);
    pageContext.setAttribute("nowUrl",nowUrl);
    out.flush();
%>
<script language="javascript" src="<c:url value="/common/tree/tree.js"/>"></script>
<script language="javascript">
<!--
    function toggleTopMenu()
    {
        if(document.getElementById("ekmsTopMenu").style.visibility == "hidden" || document.getElementById("ekmsTopMenu").style.visibility == "")
            document.getElementById("ekmsTopMenu").style.visibility="visible";
        else
            document.getElementById("ekmsTopMenu").style.visibility="hidden";
    }
    function toggleCalendarMenu()
    {
        if(document.getElementById("ekmsCalendarWidget").style.display == "none" || document.getElementById("ekmsCalendarWidget").style.display == "" || document.getElementById("ekmsCalendarWidget").style.display == null)
        {
            document.getElementById("ekmsCalendarWidget").style.display="block";
        }
        else
        {
            document.getElementById("ekmsCalendarWidget").style.display="none";
        }
        treeSave("ekmsCalendarWidget");
    }

    function next(){
        document.location='<c:url value="/ekms/calendar/calendar.jsp"/>?cn=calendarPage.calendarView&et=next&<c:out value="${widget.weekOfMonthName}"/>=<c:out value="${widget.weekOfMonth+2}"/>';
    }
//-->
</script>

<c:if test="${forward.name == 'dateselected'}">

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


<%--<%--%>
<%--    // TODO: Hack fix. Need to revisit after bug fix--%>
<%--    // assumption: ApplicationResourceBundle.parseLocale() cannot parse properly??--%>
<%--    User user = WidgetManager.getWidgetManager(request).getUser();--%>
<%--    if (user != null) {--%>
<%--        String localeStr = (String)user.getProperty(User.PROPERTY_LOCALE);--%>
<%----%>
<%--        if("en".equals(localeStr)) {--%>
<%--            localeStr = "en_US";--%>
<%--        }--%>
<%--        // re-parse to pick up "en_US"--%>
<%--        Locale userLocale = ApplicationResourceBundle.parseLocale(localeStr, Locale.ENGLISH);--%>
<%--        String appBundleName = Application.getInstance().getResourceBundleName();--%>
<%--        ResourceBundle bundle = ResourceBundle.getBundle(appBundleName, userLocale);--%>
<%--        Application.setThreadLocale(userLocale, appBundleName, bundle);--%>
<%--    }--%>
<%--%>--%>
<%
    Locale userLocale = Application.getInstance().getLocale();
    String appBundleName = Application.getInstance().getResourceBundleName();
    ResourceBundle bundle = ResourceBundle.getBundle(appBundleName, userLocale);
//    System.out.println("language "+userLocale.getLanguage());
//    System.out.println("bundle's language "+bundle.getLocale().getLanguage());
	String sun = Application.getInstance().getMessage("general.label.sun");
	String sat = Application.getInstance().getMessage("general.label.sat");
	pageContext.setAttribute("sun", sun);
	pageContext.setAttribute("sat", sat);
%>


<c:set var="sun" value="${sun}"/>
<c:set var="sat" value="${sat}"/>

   <%
    String PERMISSION_CREATE_LIBRARY = "com.tms.collab.calendar.Calendaring"; // Permission to create new library
    Application app = Application.getInstance();
	SecurityService securityService = (SecurityService) app.getService(SecurityService.class);
	String userId = securityService.getCurrentUser(request).getId();
   	// check is user has permission on calendar
	boolean isCalendar = securityService.hasPermission(userId, PERMISSION_CREATE_LIBRARY, null, null);
    %>

<c:set var="calendar" value="${widget}"/>
<c:set var="weeklyEntries" value="${calendar.weeklyEntriesWithoutTasks}"/>
<c:set var="paramDateSelect" value="${calendar.dayOfMonthName}"/>
<c:set var="date" value="${calendar.date}" ></c:set>
<table width="100%" border="0" cellpadding="5" cellspacing="1">
	<tr>
		<td class="header">
      		<table border="0" cellpadding="0" cellspacing="0" align="right">
				<tr>
					<td>
                        <%
                            if (isCalendar){
                        %>
                        <input class="button" value="<fmt:message key='calendar.label.new'/>" type="button" onClick="location.href='<%= request.getContextPath() %>/ekms/calendar/appointmentform.jsp?init=1'"/>
                        <%
                            }
                        %>
                        <input type="button" class="button" value="<fmt:message key='calendar.label.previous'/>" onClick="document.location = '<c:out value="${previousUrl}"/>'"/>
						<input type="button" class="button" value="<fmt:message key='calendar.label.now'/>" onClick="document.location = '<c:out value="${nowUrl}"/>'"/>
						<input type="button" class="button" value="<fmt:message key='calendar.label.next'/>" onClick="document.location = '<c:out value="${nextUrl}"/>'"/>
					</td>
				</tr>
			</table>
			<a href="javascript:toggleCalendarMenu();" style="text-decoration: none"><font COLOR="FFFFFF"><fmt:message key='calendar.label.appointmentsWeek'/> <c:out value="${calendar.weekOfMonth}" /> <fmt:message key='calendar.label.of'/> <fmt:formatDate pattern="MMMMM yyyy"value="${date}" /></font></a>
		</td>
	</tr>
</table>
<div id="ekmsCalendarWidget" style="display:block;">
	<table width="100%" border="0" cellpadding="5" cellspacing="1">
		<tr align="center" bgcolor="#CCCCCC" class="contentFont">
			<c:forEach var="entry" items="${weeklyEntries}">
				<c:set var="dayOfWeek" value="${entry.dayOfWeekLabel}"/>
					<td<c:choose><c:when test="${dayOfWeek==sun || dayOfWeek==sat}" > bgcolor="#FFCC99" class="calendarWeekendHeader" width="10%"</c:when><c:otherwise> class="calendarWeekdayHeader" width="16%"</c:otherwise></c:choose>>
						<x:event name="${widget.absoluteName}" type="<%=CalendarBox.PARAMETER_KEY_DATE_SELECT%>" param="${paramDateSelect}=${entry.dayOfMonth}&${calendar.monthName}=${entry.month}&${calendar.yearName}=${entry.year}" html="class=\"calendarWeekHeader\"">
							<c:choose>
								<c:when test="${entry.today}"><b><font color=FF0000></c:when>
								<c:otherwise><font color=000000></c:otherwise>
							</c:choose><c:out value="${entry.dayOfWeekLabel}"/>
							<c:out value="${entry.dayOfMonth}"/>
						</x:event>
						<c:if  test="${entry.today}" ></b></c:if>
						</font>
					</td>
			</c:forEach>
		</tr>
		<tr bgcolor="#E8E8E8" class="contentBgColor" valign="top">
			<c:forEach var="entry" items="${weeklyEntries}" varStatus="status" >
            <c:set var="dayOfWeek" value="${entry.dayOfWeekLabel}"/>
				<td <c:choose><c:when test="${dayOfWeek==sun || dayOfWeek==sat}" >width="10%" class="calendarWeekendBg"</c:when><c:otherwise>width="16%" class="calendarDayBg"</c:otherwise></c:choose>>
<%--				<td <c:choose><c:when test="${status.first||status.last}" >width="10%" class="calendarWeekendBg"</c:when><c:otherwise>width="16%" class="calendarDayBg"</c:otherwise></c:choose>>--%>
					<c:forEach var="appointment" items="${entry.eventList}" varStatus="astatus"  >
						<c:if test="${astatus.first}">
							<div id="t" style="height: 100px; width:100%; overflow: auto;">
						</c:if>
						<%
							CalendarEvent event = (CalendarEvent)pageContext.getAttribute("appointment");
							if(!event.getEventId().startsWith(Task.class.getName()))
							{
						%>
							<c:choose>
								<c:when test="${!appointment.allDay}" >
									<fmt:formatDate pattern="h:mma" value="${appointment.startDate}" /> - <fmt:formatDate pattern="h:mma" value="${appointment.endDate}" />
								</c:when>
								<c:otherwise><fmt:message key='calendar.label.allDay'/></c:otherwise>
							</c:choose>
							<c:url var="eUrl" value="/ekms/calendar/calendar.jsp">
								<c:param name="${cn}" value="calendarPage.calendarView"/>
								<c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>
								<c:param name="${eid}" value="${appointment.eventId}"/>
								<c:param name="${iid}" value="${appointment.recurrenceId}"/>
							</c:url>
							<li><a href="<c:out value="${eUrl}" escapeXml="false"/>" class="calendarWeeklink"> <c:out value="${appointment.title}" />    <c:if test="${appointment.reminder}">(<fmt:message key='calendar.label.REMINDER'/>)</c:if></a></li><br>
						<%} %>
						<c:if test="${astatus.last}" >
							</div>
						</c:if>
					</c:forEach>
					<c:if test="${status.last}">&nbsp;</c:if>
				</td>
			</c:forEach>
		</tr>
	</table>
</div>
<script>
<!--
   treeLoad("ekmsCalendarWidget");
//-->
</script>

<% out.flush(); %>

