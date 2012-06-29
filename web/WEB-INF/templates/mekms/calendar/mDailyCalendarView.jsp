<%@ page import="kacang.runtime.HttpController,
                 kacang.stdui.CalendarBox,
                 com.tms.collab.calendar.ui.CalendarView,
                 kacang.ui.Event,
                 com.tms.collab.calendar.model.CalendarModule,
                 com.tms.collab.calendar.model.CalendarEvent,
                 kacang.services.security.SecurityService,
                 kacang.Application,
                 com.tms.collab.emeeting.Meeting,
                 com.tms.collab.calendar.model.CalendarHelperBean,
                 java.util.*,
                 kacang.services.security.User,
                 com.tms.collab.calendar.ui.MCalendarView"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="calendar" value="${widget}"/>
<c:set var="entry" value="${calendar.dailyEntry}"/>
<c:set var="paramDateSelect" value=""/>
<c:set var="eventUrl" value="calendar.jsp"/>
<c:set var="paramEventSelect" value="eventId"/>
<c:set var="editUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_EDIT %></c:set>
<c:set var="deleteUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETE %></c:set>
<c:set var="pDate" value="${calendar.previousDate}"/>
<c:set var="nDate" value="${calendar.nextDate}"/>
<fmt:formatDate var="monthValue" value="${nDate}" pattern="M"/>
<c:set var="monthValue" value="${monthValue-1}" />
<c:set var="nextUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= MCalendarView.VIEW_M_DAILY %>&<c:out value="${calendar.dayOfMonthName}"/>=<fmt:formatDate value="${nDate}" pattern="d"/>&<c:out value="${calendar.monthName}" />=<c:out value="${monthValue}" />&<c:out value="${calendar.yearName}" />=<fmt:formatDate value="${nDate}" pattern="yyyy"/></c:set>
<fmt:formatDate var="monthValue" value="${pDate}" pattern="M"/>
<c:set var="monthValue" value="${monthValue-1}" />
<c:set var="previousUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= MCalendarView.VIEW_M_DAILY %>&<c:out value="${calendar.dayOfMonthName}"/>=<fmt:formatDate value="${pDate}" pattern="d"/>&<c:out value="${calendar.monthName}" />=<c:out value="${monthValue}" />&<c:out value="${calendar.yearName}" />=<fmt:formatDate value="${pDate}" pattern="yyyy"/></c:set>
<c:set var="weeklyUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %></c:set>
<c:set var="monthlyUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %></c:set>
<c:set var="acceptUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=accept</c:set>
<c-rt:set var="cn" value="<%= Event.PARAMETER_KEY_WIDGET_NAME %>"/>
<c-rt:set var="et" value="<%= Event.PARAMETER_KEY_EVENT_TYPE %>"/>
<c-rt:set var="eid" value="<%= CalendarView.PARAMETER_KEY_EVENTID %>"/>
<c-rt:set var="iid" value="<%= CalendarView.PARAMETER_KEY_INSTANCEID %>"/>
<c-rt:set var="v" value="<%=CalendarView.PARAMETER_KEY_EVENT_VIEW%>"/>

<table cellpadding="0" cellspacing="1" width="100%">
    <tr>
        <td class="title">
            <a href="<c:out value="${previousUrl}"/>" class="nav">&laquo;</a>&nbsp;
            <fmt:formatDate value="${calendar.date}" pattern="EEEEEEE, dd MMM yyyy"/>
            &nbsp;<a href="<c:out value="${nextUrl}"/>" class="nav">&raquo;</a>
        </td>
    </tr>
    <tr><td align="left" valign="top" class="data">&nbsp;</td></tr>
    <tr><td align="left" valign="top" class="subheader"><fmt:message key='calendar.label.appointments'/> : </td></tr>
    <tr>
        <td align="left" valign="top" class="data">
            <%
                CalendarHelperBean.CalendarEntry entry;
                entry = (CalendarHelperBean.CalendarEntry) pageContext.getAttribute("entry");
                Collection appointments = entry.getAppointments();
                Set appointmentSet = new TreeSet();
                appointmentSet.addAll(appointments);
                pageContext.setAttribute("appointments", appointmentSet);
            %>
            <c:set var="found" value="0"/>
            <c:forEach var="event" items="${appointments}" varStatus="status" >
                <c:choose>
                    <c:when test="${!event.hidden}" >
                        <c:url var="eUrl"  value="${eventUrl}">
                            <c:param name="${cn}" value="${calendar.absoluteName}"/>
                            <c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>
                            <c:param name="${eid}" value="${event.eventId}"/>
                            <c:param name="${iid}" value="${event.recurrenceId}"/>
                            <c:param name="${v}"><%=MCalendarView.VIEW_M_EVENT%></c:param>
                        </c:url>
                        &#149;&nbsp;<a href="<c:out value="${eUrl}" />" class="title"><c:out value="${event.title}"/></a><br>
                        <c:choose>
                            <c:when test="${!event.allDay}">
                                <fmt:formatDate value="${event.startDate}" pattern="${globalDatetimeShort}"/> - <fmt:formatDate value="${event.endDate}" pattern="${globalDatetimeShort}"/>
                            </c:when>
                            <c:otherwise>
                                * <fmt:message key="calendar.label.AllDay"/> (<fmt:formatDate value="${event.startDate}" pattern="${globalDateShort}"/>)
                            </c:otherwise>
                        </c:choose><br>
                        <c:if test="${event.ownerExcluded}">*<fmt:message key='calendar.label.self-excluded'/><br></c:if>
                        <c:if test="${!empty event.attendees}">
                            <c:set var="userId" value="${sessionScope.currentUser.id}"/>
                            <c:forEach items="${event.attendees}" var="attendee">
                                <c-rt:set var="att_status" value="<%=CalendarModule.ATTENDEE_STATUS_PENDING%>"  />
                                <c:if test="${userId == attendee.userId && attendee.status == att_status}" >
                                    <c:set var="attendeeId" value="${attendee.attendeeId}"/>
                                    <%pageContext.setAttribute("canAccept",Boolean.TRUE);%>
                                </c:if>
                            </c:forEach>
                        </c:if>
                        <%
                            CalendarEvent event = (CalendarEvent)pageContext.getAttribute("event");
                            SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                            String userId = ss.getCurrentUser(request).getId();
                            if(event.getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null) ||ss.hasPermission(userId,CalendarModule.PERMISSION_EDIT_EVENTS,null,null))
                                pageContext.setAttribute("canEdit", Boolean.TRUE);
                            if(event.getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null) ||ss.hasPermission(userId,CalendarModule.PERMISSION_DELETE_EVENTS,null,null))
                                pageContext.setAttribute("canDelete", Boolean.TRUE);
                        %>
                        <c:if test="${canEdit}" >
                           <input value="<fmt:message key="calendar.label.buttonEdit"/>" type="button" class="button" onClick="document.location = 'editappointmentform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />'"/>
                        </c:if>
                        <c:if test="${canDelete}" >
                            <input value="<fmt:message key="calendar.label.buttonDelete"/>" type="button" class="button" onClick="if(confirm('Are you sure to delete this appointment')){document.location='<c:out value="${deleteUrl}"/>&eventId=<c:out value="${event.eventId}"/>';}"/>
                        </c:if>
                        <c:if test="${canAccept}" >
                            <input value="<fmt:message key="calendar.label.buttonAccept"/>" type="button" class="button" onClick="document.location = '<c:out value="${acceptUrl}" />&eventId=<c:out value="${event.eventId}"/>&instanceId=<c:out value="${event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>';"/>
                            <input value="<fmt:message key="calendar.label.buttonReject"/>" type="button" class="button" onClick="window.open('rejectform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />&instanceId=<c:out value="${event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>','rejectform','scrollbars=yes,resizable=yes,width=400,height=200');return false;"/>
                        </c:if>
                        <c:if test="${canEdit || canDelete || canAccept}">
                            <br>
                        </c:if>
                       <% pageContext.setAttribute("canEdit",Boolean.FALSE); %>
                       <% pageContext.setAttribute("canDelete",Boolean.FALSE); %>
                       <% pageContext.setAttribute("canAccept",Boolean.FALSE); %>
                    </c:when>
                    <c:otherwise><fmt:message key='calendar.label.privateEvent'/></c:otherwise>
                </c:choose>
                <c:set var="found" value="1"/>
            </c:forEach>
            <c:if test="${found == '0'}">
                <fmt:message key="calendar.label.noAppointment"/>
            </c:if>
        </td>
    </tr>
    <tr><td align="left" valign="top" class="data">&nbsp;</td></tr>
    <tr><td align="left" valign="top" class="subheader"><fmt:message key="calendar.label.e-MeetingFilter"/> : </td></tr>
    <tr>
        <td align="left" valign="top" class="data">
            <%
                try {
                    Collection meetingcol = entry.getMeetings();
                    appointmentSet = new TreeSet();
                    appointmentSet.addAll(meetingcol);
                    pageContext.setAttribute("meetings", appointmentSet);
                } catch(Exception e) {
                }
            %>
            <c:set var="found" value="0"/>
            <c:forEach var="meeting" items="${entry.meetings}" varStatus="status">
                <c:choose>
                    <c:when test="${!meeting.event.hidden}">
                        <c:url var="eUrl" value="${eventUrl}">
                            <c:param name="${cn}" value="${calendar.absoluteName}"/>
                            <c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>
                            <c:param name="${eid}" value="${meeting.eventId}"/>
                            <c:param name="${iid}" value="${meeting.event.recurrenceId}"/>
                            <c:param name="${v}"><%=MCalendarView.VIEW_M_EVENT%></c:param>
                        </c:url>
                        &#149;&nbsp;<a href="<c:out value="${eUrl}" />" class="title"><c:out value="${meeting.event.title}"/></a><br>
                        <c:choose>
                            <c:when test="${!meeting.event.allDay}">
                                <fmt:formatDate value="${meeting.event.startDate}" pattern="${globalDatetimeShort}"/> - <fmt:formatDate value="${meeting.event.endDate}" pattern="${globalDatetimeShort}"/>
                            </c:when>
                            <c:otherwise>
                                * <fmt:message key="calendar.label.AllDay"/> (<fmt:formatDate value="${meeting.event.startDate}" pattern="${globalDateShort}"/>)
                            </c:otherwise>
                        </c:choose><br>
                        <c:if test="${meeting.event.ownerExcluded}">*<fmt:message key='calendar.label.self-excluded'/><br></c:if>
                        <c:if test="${!empty meeting.event.attendees}">
                            <c:set var="userId" value="${sessionScope.currentUser.id}"/>
                            <c-rt:set var="att_status" value="<%=CalendarModule.ATTENDEE_STATUS_PENDING%>"  />
                            <c:forEach items="${meeting.event.attendees}" var="attendee">
                                <c:if test="${userId == attendee.userId && attendee.status == att_status}" >
                                    <c:set var="attendeeId" value="${attendee.attendeeId}"/>
                                    <%pageContext.setAttribute("canAccept",Boolean.TRUE);%>
                                </c:if>
                            </c:forEach>
                        </c:if>
                       <%
                            String userId = (String)pageContext.getAttribute("userId");
                            Meeting event = (Meeting )pageContext.getAttribute("meeting");
                            SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                            if(event.getEvent().getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null) ||ss.hasPermission(userId,CalendarModule.PERMISSION_EDIT_EVENTS,null,null))
                                pageContext.setAttribute("canEdit", Boolean.TRUE);
                            if(event.getEvent().getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null) ||ss.hasPermission(userId,CalendarModule.PERMISSION_DELETE_EVENTS,null,null))
                                pageContext.setAttribute("canDelete", Boolean.TRUE);
                        %>
                        <c:if test="${canEdit}" >
                            <input value="<fmt:message key="calendar.label.buttonEdit"/>" type="button" class="button" onClick="document.location = 'editemeetingform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${meeting.event.eventId}" />'"/>
                        </c:if>
                        <c:if test="${canDelete}" >
                            <input value="<fmt:message key="calendar.label.buttonDelete"/>" type="button" class="button" onClick="if(confirm('Are you sure to delete this meeting')){document.location='<c:out value="${deleteUrl}"/>&eventId=<c:out value="${meeting.event.eventId}"/>';}"/>
                        </c:if>
                        <c:if test="${canAccept}" >
                            <input value="<fmt:message key="calendar.label.buttonAccept"/>" type="button" class="button" onClick="if(confirm('Are you sure to delete this appointment')){document.location='<c:out value="${deleteUrl}"/>&eventId=<c:out value="${event.eventId}"/>';}"/>
                            <input value="<fmt:message key="calendar.label.buttonReject"/>" type="button" class="button" onClick="if(confirm('Are you sure to delete this appointment')){document.location='<c:out value="${deleteUrl}"/>&eventId=<c:out value="${event.eventId}"/>';}"/>
                        </c:if>
                        <c:if test="${canEdit || canDelete || canAccept}">
                            <br>
                        </c:if>
                        <% pageContext.setAttribute("canEdit",Boolean.FALSE); %>
                        <% pageContext.setAttribute("canDelete",Boolean.FALSE); %>
                        <% pageContext.setAttribute("canAccept",Boolean.FALSE); %>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key='calendar.label.privateMeeting'/>
                    </c:otherwise>
                </c:choose>
                <c:set var="found" value="1"/>
            </c:forEach>
            <c:if test="${found == '0'}">
                <fmt:message key="calendar.label.noEMeeting"/>
            </c:if>
        </td>
    </tr>
    <tr><td align="left" valign="top" class="data">&nbsp;</td></tr>
    <tr><td align="left" valign="top" class="subheader"><fmt:message key="calendar.label.todayEvents"/> : </td></tr>
    <tr>
        <td align="left" valign="top" class="data">
            <c:set var="found" value="1"/>
            <c:forEach var="event" items="${entry.events}" begin="0" varStatus="status"  >
                <c:choose>
                    <c:when test="${!event.hidden}" >
                        <c:url var="eUrl"  value="${eventUrl}">
                            <c:param name="${cn}" value="${calendar.absoluteName}"/>
                            <c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>
                            <c:param name="${eid}" value="${event.eventId}"/>
                            <c:param name="${iid}" value="${event.recurrenceId}"/>
                            <c:param name="${v}"><%=MCalendarView.VIEW_M_EVENT%></c:param>
                        </c:url>
                        &#149;&nbsp;<a href="<c:out value="${eUrl}" />" class="title"><c:out value="${event.title}"/></a><br>
                        <c:choose>
                            <c:when test="${!event.allDay}">
                                <fmt:formatDate value="${event.startDate}" pattern="${globalDatetimeShort}"/> - <fmt:formatDate value="${event.endDate}" pattern="${globalDatetimeShort}"/>
                            </c:when>
                            <c:otherwise>
                                * <fmt:message key="calendar.label.AllDay"/> (<fmt:formatDate value="${event.startDate}" pattern="${globalDateShort}"/>)
                            </c:otherwise>
                        </c:choose><br>
                        <%
                            CalendarEvent event = (CalendarEvent)pageContext.getAttribute("event");
                            SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                            String userId = ss.getCurrentUser(request).getId();
                            if(event.getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null) ||ss.hasPermission(userId,CalendarModule.PERMISSION_EDIT_EVENTS,null,null))
                                pageContext.setAttribute("canEdit", Boolean.TRUE);
                            if(event.getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null) ||ss.hasPermission(userId,CalendarModule.PERMISSION_DELETE_EVENTS,null,null))
                                pageContext.setAttribute("canDelete", Boolean.TRUE);
                        %>
                        <c:if test="${canEdit}" >
                           <input value="<fmt:message key="calendar.label.buttonEdit"/>" type="button" class="button" onClick="document.location = 'editeventform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />'"/>
                        </c:if>
                        <c:if test="${canDelete}" >
                            <input value="<fmt:message key="calendar.label.buttonDelete"/>" type="button" class="button" onClick="if(confirm('Are you sure to delete this appointment')){document.location='<c:out value="${deleteUrl}"/>&eventId=<c:out value="${event.eventId}"/>';}"/>
                        </c:if>
                        <c:if test="${canAccept}" >
                            <input value="<fmt:message key="calendar.label.buttonAccept"/>" type="button" class="button" onClick="document.location = '<c:out value="${acceptUrl}" />&eventId=<c:out value="${event.eventId}"/>&instanceId=<c:out value="${event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>';"/>
                            <input value="<fmt:message key="calendar.label.buttonReject"/>" type="button" class="button" onClick="window.open('rejectform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />&instanceId=<c:out value="${event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>','rejectform','scrollbars=yes,resizable=yes,width=400,height=200');return false;"/>
                        </c:if>
                        <c:if test="${canEdit || canDelete || canAccept}">
                            <br>
                        </c:if>
                       <% pageContext.setAttribute("canEdit",Boolean.FALSE); %>
                       <% pageContext.setAttribute("canDelete",Boolean.FALSE); %>
                       <% pageContext.setAttribute("canAccept",Boolean.FALSE); %>
                    </c:when>
                    <c:otherwise><fmt:message key='calendar.label.privateEvent'/></c:otherwise>
                </c:choose>
                <c:set var="found" value="1"/>
            </c:forEach>
            <c:if test="${found == '0'}">
                <fmt:message key="calendar.label.noEvent"/>
            </c:if>
        </td>
    </tr>
</table>
