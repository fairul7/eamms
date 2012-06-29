<%@ page import="kacang.ui.Event,
                 com.tms.collab.calendar.ui.CalendarView,
                 com.tms.collab.calendar.ui.CalendarEventView,
                 java.util.Date"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:if test="${forward.name == 'cancel'}">
	<%-- <%
	// out.println(pageContext.toString());
	CalendarView cview = (CalendarView)session.getAttribute("widget");
	if(cview==null)
	out.println("NULL");
	//CalendarEventView eview = cview.getCalendarEventView();
	//out.println(eview.getEventId());
	//      view.setState(0);
	//    view.setEventId(view.getEventId());
	%>--%>
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
<c:set var="dailyUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_DAILY %></c:set>
<c:set var="weeklyUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_WEEKLY %></c:set>
<c:set var="monthlyUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_MONTHLY %></c:set>
<c:set var="editUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_EDIT %></c:set>
<c:set var="deleteUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETE %></c:set>
<c:set var="deleteAllUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETEALL %></c:set>
<% pageContext.setAttribute("EDIT",String.valueOf(CalendarEventView.EDIT));%>
<table BORDER="0" CELLSPACING="0" CELLPADDING="0" width="100%" valign="top" class="contentBgColor">
	<tr>
		<td>
			<table BORDER="0" CELLSPACING="1" CELLPADDING="3" width="100%" valign="top" class="contentBgColor">
				<c:set value="${calendar.selectedEventId}" var="selectedEventId"  ></c:set>
				<%
					String eId = (String)pageContext.getAttribute("selectedEventId");
					String header = eId.substring(eId.lastIndexOf(".")+1,eId.indexOf("_"));
					pageContext.setAttribute("headline",header);
				%>
				<tr>
					<td class="calendarHeader">
						<c:choose>
							<c:when test="${headline == 'Task'}" ><fmt:message key='calendar.label.toDoTask'/> :</c:when>
							<c:when test="${headline=='CalendarEvent'}" ><fmt:message key='calendar.label.event'/> :</c:when>
							<c:when test="${headline == 'Meeting'}" ><fmt:message key='calendar.label.e-Meeting'/> :</c:when>
							<c:otherwise><fmt:message key='calendar.label.appointment'/> :</c:otherwise>
						</c:choose>
						&nbsp; <c:out value="${calendar.selectedEvent.title}" />
						<c:if test="${calendar.selectedEvent.deleted}" >&nbsp;<fmt:message key='calendar.label.DELETED'/></c:if>
					</td>
				</tr>
				<tr>
					<td class="calendarSubheader">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" class="contentBgColor">
							<tr class="calendarSubheader">
								<td align="left" valign="bottom"><x:display name="${calendar.jumpForm.absoluteName}" ></td></x:display>
								<td align="right" valign="bottom">
									<c:if test="${!entry.today}" >
										<%
											Date today = new Date();
											pageContext.setAttribute("today",today);
										%>
										<fmt:formatDate var="monthValue" value="${today}" pattern="M"/>
										<c:set var="monthValue" value="${monthValue-1}" />
										<c:set var="todayUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_DAILY %>&<c:out value="${calendar.dayOfMonthName}"/>=<fmt:formatDate value="${today}" pattern="d"/>&<c:out value="${calendar.monthName}" />=<c:out value="${monthValue}" />&<c:out value="${calendar.yearName}" />=<fmt:formatDate value="${today}" pattern="yyyy"/></c:set>
										<INPUT TYPE="button" class="button" value="<fmt:message key='calendar.label.today'/>" onClick="document.location = '<c:out value="${todayUrl}"/>'">
									</c:if>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td CLASS="classRow">
						<table width="100%">
							<tr>
								<td width="20%">

								</td>
								<td width="60%" align="center" style="font-weight: bold">
									<a href="<c:out value="${dailyUrl}"/>&<c:out value="${paramDateSelect}"/>=<c:out value="${calendar.dayOfMonth}"/>">
										<fmt:formatDate value="${calendar.date}" pattern="EEEEEEE, "/><c:out value="${calendar.dayOfMonth}"/>
									</a>
									<fmt:parseDate value="${calendar.month+1}" pattern="MM" var="fm"/>
									<a href="<c:out value="${monthlyUrl}"/>"><fmt:formatDate pattern="MMMM" value="${fm}"/></a>
									<c:out value="${calendar.year}"/>
								</td>
								<td width="20%" align="right">

								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="classRow">
			<table BORDER="0" CELLSPACING="1" CELLPADDING="0" width="100%" valign="top" class="contentBgColor">
				<tr><td class="classRow">&nbsp;</td></tr>
			</table>
		</td>
	</tr>
	<tr border="0" cellspacing="0" cellpadding="0">
		<td CLASS="contentBgColor" border="0" cellspacing="0" cellpadding="0">
			<x:display name="${calendar.calendarEventView.absoluteName}" />
		</td>
	</tr>
	<tr>
		<td class="classRow">
			<table BORDER="0" CELLSPACING="1" CELLPADDING="3" width="100%" valign="top" class="contentBgColor">
				<tr width="100%">
					<td align="left" class="classRow">&nbsp;
			            <br /><br />
                        <fmt:message key="calendar.label.statusLabel" />

					</td>
				</tr>
				<tr>
				<td CLASS="classRow" align="right">&nbsp;</td></tr>
			</table>
		</td>
	</tr>
</table>
