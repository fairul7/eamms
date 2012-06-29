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
                 kacang.services.security.User"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="calendar" value="${widget}"/>
<c:set var="entry" value="${calendar.dailyEntry}"/>
<c:set var="paramDateSelect" value=""/>
<%--<c:set var="eventUrl">viewCalendarEvent.do</c:set>--%>
<c:set var="paramEventSelect" value="eventId"/>
<c:set var="editUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_EDIT %>
</c:set>
<c:set var="deleteUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETE %>
</c:set>
<c:set var="pDate" value="${calendar.previousDate}"/>
<c:set var="nDate" value="${calendar.nextDate}"/>
<fmt:formatDate var="monthValue" value="${nDate}" pattern="M"/>
<c:set var="monthValue" value="${monthValue-1}" />
<c:set var="nextUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_DAILY %>&<c:out value="${calendar.dayOfMonthName}"/>=<fmt:formatDate value="${nDate}" pattern="d"/>&<c:out value="${calendar.monthName}" />=<c:out value="${monthValue}" />&<c:out value="${calendar.yearName}" />=<fmt:formatDate value="${nDate}" pattern="yyyy"/>
</c:set>
<fmt:formatDate var="monthValue" value="${pDate}" pattern="M"/>
<c:set var="monthValue" value="${monthValue-1}" />
<c:set var="previousUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_DAILY %>&<c:out value="${calendar.dayOfMonthName}"/>=<fmt:formatDate value="${pDate}" pattern="d"/>&<c:out value="${calendar.monthName}" />=<c:out value="${monthValue}" />&<c:out value="${calendar.yearName}" />=<fmt:formatDate value="${pDate}" pattern="yyyy"/>
</c:set>
<c:set var="weeklyUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_WEEKLY %>
</c:set>
<c:set var="monthlyUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_MONTHLY %>

</c:set>
<c:set var="acceptUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=accept

</c:set>

 <c-rt:set var="cn" value="<%= Event.PARAMETER_KEY_WIDGET_NAME %>"/>
    <c-rt:set var="et" value="<%= Event.PARAMETER_KEY_EVENT_TYPE %>"/>
    <c-rt:set var="eid" value="<%= CalendarView.PARAMETER_KEY_EVENTID %>"/>
    <c-rt:set var="iid" value="<%= CalendarView.PARAMETER_KEY_INSTANCEID %>"/>


 <table BORDER="0" CELLSPACING="1" CELLPADDING="3" width="100%">
        <tr><td class="calendarHeader" ><fmt:message key='calendar.label.dailyCalendarView'/></td></tr>
        <tr><td VALIGN="BOTTOM"  BGCOLOR="#003366" CLASS="calendarSubheader" >

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

<tr >
  <td BGCOLOR="#EFEFEF" CLASS="contentBgColor">
  <table width="100%"><tr>
  <td width="20%" >
      <input type="button" value="&nbsp;&nbsp;&lt;&nbsp;&nbsp;" class="button" onClick="document.location='<c:out value="${previousUrl}"/>';">
  </td>
  <td width="60%" align="center" style="font-weight: bold">
    <fmt:formatDate value="${calendar.date}" pattern="EEEEEEE, "/>
    <c:out value="${calendar.dayOfMonth}"/>
    <fmt:parseDate value="${calendar.month+1}" pattern="MM" var="fm"/>
    <a href="<c:out value="${monthlyUrl}"/>"><fmt:formatDate pattern="MMMM" value="${fm}"/></a>
    <c:out value="${calendar.year}"/>
  </td>
  <td width="20%" align="right">
        <input type="button" value="&nbsp;&nbsp;&gt;&nbsp;&nbsp;" class="button" onClick="document.location='<c:out value="${nextUrl}"/>'">
  </td>
  </tr></table>
  </td>
</tr>

<tr VALIGN="MIDDLE">
      <%int i=0;%>
  <td colspan="3" BGCOLOR="#EFEFEF" CLASS="contentBgColor">
  <BLOCKQUOTE>
    <P><BR>
                            <B><FONT COLOR="#003366"><fmt:message key='calendar.label.appointments'/></FONT></B>
        <FONT SIZE="1" CLASS="calendarContentFont">
                              </FONT></P>
         <UL>
         <%
             CalendarHelperBean.CalendarEntry entry;
             entry = (CalendarHelperBean.CalendarEntry) pageContext.getAttribute("entry");
             Collection appointments = entry.getAppointments();
             Set appointmentSet = new TreeSet();
             appointmentSet.addAll(appointments);
             pageContext.setAttribute("appointments", appointmentSet);
         %>
            <c:forEach var="event" items="${appointments}" varStatus="status" >
                <p>
                <LI VALIGN="TOP">
                    <FONT COLOR="#FF0000">
                    <c:choose>
                    <c:when test="${event.moreThanOneDay}" >
                        <fmt:formatDate value="${event.startDate}" pattern="${globalDatetimeLong}" />
                        - <fmt:formatDate value="${event.endDate}" pattern="${globalDatetimeLong}" />
                    </c:when>
                    <c:when test="${!event.allDay}" >
                        <fmt:formatDate value="${event.startDate}" pattern="${globalTimeLong}" />
                        - <fmt:formatDate value="${event.endDate}" pattern="${globalTimeLong}" />
                    </c:when>
                    <c:otherwise><fmt:message key='calendar.label.allDay'/></c:otherwise>
                    </c:choose>
                    </FONT>  <br>

                 <c:choose>
                 <c:when test="${!event.hidden}" >
                     <c:url var="eUrl" value="${eventUrl}">
                        <c:param name="${cn}" value="${calendar.absoluteName}"/>
                        <c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>
                        <c:param name="${eid}" value="${event.eventId}"/>
                        <c:param name="${iid}" value="${event.recurrenceId}"/>
                    </c:url>
                    <A HREF="<c:out value="${eUrl}" />"><c:out value="${event.title}"/>     <c:if test="${event.reminder}">(<fmt:message key='calendar.label.REMINDER'/>)</c:if>
                    <c:if test="${event.recurrence}">(<fmt:message key='calendar.label.Recurrence'/>)</c:if>
                    </a><c:if test="${event.ownerExcluded}">*<fmt:message key='calendar.label.self-excluded'/>*</c:if>
                    <c:set scope="page" value="${sessionScope.currentUser.id}" var="userId"/>
                    <%
                        CalendarEvent event = (CalendarEvent)pageContext.getAttribute("event");
                        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                        String userId = ss.getCurrentUser(request).getId();

                        if(event.getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null) ||ss.hasPermission(userId,CalendarModule.PERMISSION_EDIT_EVENTS,null,null)){
                            pageContext.setAttribute("canEdit", Boolean.TRUE);
                        }
                        if(event.getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null) ||ss.hasPermission(userId,CalendarModule.PERMISSION_DELETE_EVENTS,null,null)){
                            pageContext.setAttribute("canDelete", Boolean.TRUE);
                        }
  %>
                    <c:if test="${event.attendees != null}">
                         <c:set var="userId" value="${sessionScope.currentUser.id}"/>
                         <c:forEach items="${event.attendees}" var="attendee">
                         <c-rt:set var="att_status" value="<%=CalendarModule.ATTENDEE_STATUS_PENDING%>"  />
                                <c:if test="${userId == attendee.userId && attendee.status == att_status}" >
                                <c:set var="attendeeId" value="${attendee.attendeeId}"/>
                                <%pageContext.setAttribute("canAccept",Boolean.TRUE);%>
                            </c:if>
                         </c:forEach>
                    </c:if>
                    <c:if test="${canEdit||canDelete||canAccept}" >
                    <br>
                    </c:if>
                    <c:if test="${canEdit}" >
                       <input value="<fmt:message key="calendar.label.buttonEdit"/>" type="button" class="button" onClick="document.location = 'editappointmentform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />'"/>
                    </c:if>

                     <c:if test="${canDelete}" >
                     <input value="<fmt:message key="calendar.label.buttonDelete"/>" type="button" class="button" onClick="if(confirm('<fmt:message key="messaging.label.confirmDelete"/>')){document.location='<c:out value="${deleteUrl}"/>&eventId=<c:out value="${event.eventId}"/>';}"/>

                    </c:if>
                    <c:if test="${canAccept}" >
                     <input value="<fmt:message key="calendar.label.buttonAccept"/>" type="button" class="button" onClick="document.location = '<c:out value="${acceptUrl}" />&eventId=<c:out value="${event.eventId}"/>&instanceId=<c:out value="${event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>';"/>
                     <input value="<fmt:message key="calendar.label.buttonReject"/>" type="button" class="button" onClick="window.open('rejectform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />&instanceId=<c:out value="${event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>','rejectform','scrollbars=yes,resizable=yes,width=400,height=200');return false;"/>

                    </c:if>
                   <%pageContext.setAttribute("canEdit",Boolean.FALSE);%>
                   <%pageContext.setAttribute("canDelete",Boolean.FALSE);%>
                   <%pageContext.setAttribute("canAccept",Boolean.FALSE);%>
</c:when>
                    <c:otherwise><fmt:message key='calendar.label.privateAppointment'/></c:otherwise>
               </c:choose>
                    <br><B><FONT COLOR="#9C9A9C">. . . . . . . . . . . . . . . . . . . . . .</FONT></B><BR>
               <%--     <FONT SIZE="2" CLASS="calendarContentFont">--%>
                    <c:if test="${event.location!=null && event.location != ''}" >
                    <B><fmt:message key='calendar.label.location'/>:</B> <c:out value="${event.location}" />
                     | </c:if>
                     <B><fmt:message key='calendar.label.scheduledby'/>:</B> <c:out value="${event.userName}" />
                    <br>
                    <%--<c:if test="${!event.personal}" >--%>
                        <%-- kurnia - test for list attendees by status --%>
                        <%
                            boolean hasAttendee=false;
                            boolean isStart=true;
                        %>
                        <c:forEach items="${event.attendees}" var="attendee">
                            <c:if test="${attendee.compulsory}">
                                <% if (isStart) { %>
                                <br><b><fmt:message key='calendar.label.compulsoryAttendees'/>:</b><br>
                                <% } isStart=false; %>
                                <% if (hasAttendee) { %>, <% } %>
                                    <c:out value="${attendee.name}"></c:out>
                                <% hasAttendee=true; %>
                            </c:if>
                        </c:forEach>
                        <% if (hasAttendee) { %><br><% } %>

                        <% hasAttendee=false; isStart=true; %>
                        <c:forEach items="${event.attendees}" var="attendee">
                            <c:if test="${not attendee.compulsory}">
                            <c:if test="${attendee.status=='C'}">
                                <% if (isStart) { %>
                                    <br><B><fmt:message key="calendar.label.confirmed"/>:</B><br>
                                <% } isStart=false; %>
                                <%  if (hasAttendee) { %>, <% } %>
                                    <c:out value="${attendee.name}"></c:out>
                                <% hasAttendee=true; %>
                            </c:if>
                            </c:if>
                        </c:forEach>
                        <% if (hasAttendee) { %><br><% } %>
                        <%-- if (!hasAttendee) { %>--<% } --%>

                        <% hasAttendee=false; isStart=true; %>
                        <c:forEach items="${event.attendees}" var="attendee">
                            <c:if test="${not attendee.compulsory}">
                            <c:if test="${attendee.status=='P'}">
                                <% if (isStart) { %>
                                    <br><B><fmt:message key="calendar.label.pending"/>:</B><br>
                                <% } isStart=false; %>
                                <%  if (hasAttendee) { %>, <% } %>
                                    <c:out value="${attendee.name}"></c:out>
                                <% hasAttendee=true; %>
                            </c:if>
                            </c:if>
                        </c:forEach>
                        <% if (hasAttendee) { %><br><% } %>
                        <%-- if (!hasAttendee) { %>--<% } --%>

                        <% hasAttendee=false; isStart=true; %>
                        <c:forEach items="${event.attendees}" var="attendee">
                            <c:if test="${not attendee.compulsory}">
                            <c:if test="${attendee.status=='R'}">
                                <% if (isStart) { %>
                                    <br><B><fmt:message key="calendar.label.rejected"/>:</B><br>
                                <% } isStart=false; %>
                                <%  if (hasAttendee) { %>, <% } %>
                                    <c:out value="${attendee.name}"></c:out>
                                <% hasAttendee=true; %>
                            </c:if>
                            </c:if>
                        </c:forEach>
                        <% if (hasAttendee) { %> <br> <% } %>
                        <%-- if (!hasAttendee) { %>--<% } --%>
                        <%-- kurnia - end --%>
                        <%--
                        <B><fmt:message key='calendar.label.attendees'/>:</B>
                        <c:forEach items="${event.attendees}" var="attendee" begin="0" varStatus="status"  >
                          <c:if test="${status.index>0}" >,</c:if>
                            <c:out value="${attendee.name}" /> (<c:out value="${attendee.status}"/>)
                        </c:forEach>
                        --%>
                    <%--</c:if>--%>
                    <c:if test="${!empty event.resources}">
                       <br> <B><fmt:message key='calendar.label.resources'/>:</B>
                        <c:forEach items="${event.resources}" var="resource" varStatus="status" >
                            <c:if test="${status.index>0}" >,</c:if>
                            <c:out value="${resource.name}" ></c:out>(<c:out value="${resource.status}" />)

                        </c:forEach>
                    </c:if> <%-- </font>--%>

                    <hr size="1" width="80%" align="left">
                </LI>
                </p>
                <%i++;%>
            </c:forEach>
            <%  if(i==0) {
            %>
                <fmt:message key='calendar.label.noAppointment'/>
            <%
                }
                i=0;
            %>
         </UL>
     </p>

     <p>
        <B><FONT COLOR="#003366"><fmt:message key='calendar.label.e-MeetingFilter'/></FONT></B>
         <UL>
          <%
              try {
                  Collection meetingcol = entry.getMeetings();
                  appointmentSet = new TreeSet();
                  appointmentSet.addAll(meetingcol);
                  pageContext.setAttribute("meetings", appointmentSet);
              } catch(Exception e) {
                  e.printStackTrace(System.out);
              }
         %>
            <c:forEach var="meeting" items="${entry.meetings}" varStatus="status" >
            <%i++;%>
                <p>
                    <LI VALIGN="TOP">
                    <FONT COLOR="#FF0000">
                    <c:choose>
                    <c:when test="${event.moreThanOneDay}" >
                        <fmt:formatDate value="${event.startDate}" pattern="${globalDatetimeLong}" />
                        - <fmt:formatDate value="${event.endDate}" pattern="${globalDatetimeLong}" />
                    </c:when>
                    <c:when test="${!meeting.event.allDay}" >
                        <fmt:formatDate value="${meeting.event.startDate}" pattern="${globalTimeLong}" />
                    - <fmt:formatDate value="${meeting.event.endDate}" pattern="${globalTimeLong}" />

                    </c:when>
                    <c:otherwise><fmt:message key='calendar.label.allDay'/></c:otherwise>

                    </c:choose>
                    </FONT>  <br>
                    <c:choose>
                    <c:when test="${!meeting.event.hidden}" >

                     <c:url var="eUrl" value="${eventUrl}">
                        <c:param name="${cn}" value="${calendar.absoluteName}"/>
                        <c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>
                        <c:param name="${eid}" value="${meeting.eventId}"/>
                        <c:param name="${iid}" value="${meeting.event.recurrenceId}"/>
                    </c:url>
                    <A HREF="<c:out value="${eUrl}" />"><c:out value="${meeting.title}"/>    <c:if test="${meeting.event.reminder}">(<fmt:message key='calendar.label.REMINDER'/>)</c:if><c:if test="${meeting.event.recurrence}">(<fmt:message key='calendar.label.Recurrence'/>)</c:if></a>
                    <c:if test="${meeting.event.ownerExcluded}">*<fmt:message key='calendar.label.self-excluded'/>*</c:if>
                    <c:if test="${meeting.event.attendees != null}">
                         <c:set var="userId" value="${sessionScope.currentUser.id}"/>
                         <c:forEach items="${meeting.event.attendees}" var="attendee">
                            <c:if test="${userId == attendee.userId && attendee.status == '<%=CalendarModule.ATTENDEE_STATUS_PENDING%>'}" >
                             <IMG SRC="pix/ic_reject.gif" WIDTH="38" HEIGHT="18" onMouseDown="MM_openBrWindow('sendmessage.htm','reject','resizable=yes,width=450,height=200')" ALT="Click here to reject appointment"> <IMG SRC="pix/ic_accept.gif" WIDTH="45" HEIGHT="18" ALT="Click here to accept appointment"><BR>


                            </c:if>
                         </c:forEach>
                    </c:if>
                    <%
                        Meeting event = (Meeting )pageContext.getAttribute("meeting");
                        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                        String userId = ss.getCurrentUser(request).getId();

                        if(event.getEvent().getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null) ||ss.hasPermission(userId,CalendarModule.PERMISSION_EDIT_EVENTS,null,null)){

                            pageContext.setAttribute("canEdit", Boolean.TRUE);
                        }
                        if(event.getEvent().getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null)
                                ||ss.hasPermission(userId,CalendarModule.PERMISSION_DELETE_EVENTS,null,null)){
                                    pageContext.setAttribute("canDelete", Boolean.TRUE);
                                }
  %>                <%pageContext.setAttribute("canAccept",Boolean.FALSE);%>
                    <c:if test="${meeting.event.attendees != null}">
                        <c:set var="userId" value="${sessionScope.currentUser.id}"/>
						<c:forEach items="${meeting.event.attendees}" var="attendee">
                         	<c-rt:set var="att_status" value="<%=CalendarModule.ATTENDEE_STATUS_PENDING%>"  />
							<c:if test="${userId == attendee.userId && attendee.status == att_status}" >
								<c:set var="attendeeId" value="${attendee.attendeeId}"/>
								<%pageContext.setAttribute("canAccept",Boolean.TRUE);%>
							</c:if>
                        </c:forEach>
                    </c:if>
					<c:if test="${canEdit||canDelete||canAccept}" >
                    <br>
                    </c:if>
                    <c:if test="${canEdit}" >
                       <input value="<fmt:message key="calendar.label.buttonEdit"/>" type="button" class="button" onClick="document.location = 'editemeetingform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${meeting.event.eventId}" />'"/>
                    </c:if>

                     <c:if test="${canDelete}" >
                     <input value="<fmt:message key="calendar.label.buttonDelete"/>" type="button" class="button" onClick="if(confirm('<fmt:message key="messaging.label.confirmDelete"/>')){document.location='<c:out value="${deleteUrl}"/>&eventId=<c:out value="${meeting.event.eventId}"/>';}"/>

                    </c:if>
                    <c:if test="${canAccept}" >
						<input value="<fmt:message key="calendar.label.buttonAccept"/>" type="button" class="button" onClick="document.location = '<c:out value="${acceptUrl}" />&eventId=<c:out value="${meeting.event.eventId}"/>&instanceId=<c:out value="${meeting.event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>';"/>
						<input value="<fmt:message key="calendar.label.buttonReject"/>" type="button" class="button" onClick="window.open('rejectform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${meeting.event.eventId}" />&instanceId=<c:out value="${meeting.event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>','rejectform','scrollbars=yes,resizable=yes,width=400,height=200');return false;"/>
                    </c:if>
                    </c:when>
                    <c:otherwise><fmt:message key='calendar.label.privateMeeting'/></c:otherwise>
                    </c:choose>
                    <br><B><FONT COLOR="#9C9A9C">. . . . . . . . . . . . . . . . . . . . . .</FONT></B><BR>
                    <!-- <FONT SIZE="2" CLASS="calendarContentFont"> -->
                    <c:if test="${meeting.event.location!=null && meeting.event.location != ''}" >
                    <B><fmt:message key='calendar.label.location'/>:</B> <c:out value="${meeting.event.location}" />
                     | </c:if>
                     <B><fmt:message key='calendar.label.scheduledby'/>:</B> <c:out value="${meeting.event.userName}" />
                    <br>
                    <c:if test="${!meeting.event.personal}" >
                        <%-- kurnia - start --%>
                        <% boolean hasAttendee=false, isStart=true; %>
                        <c:forEach items="${meeting.event.attendees}" var="attendee">
                            <c:if test="${attendee.compulsory}">
                                <% if (isStart) { %>
                                <br><b><fmt:message key='calendar.label.compulsoryAttendees'/>:</b><br>
                                <% } isStart=false; %>
                                <% if (hasAttendee) { %>, <% } %>
                                <c:out value="${attendee.name}"/>
                                <% hasAttendee=true; %>
                            </c:if>
                        </c:forEach>
                        <% if (hasAttendee) { %><BR><% } %>

                        <% hasAttendee=false; isStart=true; %>
                        <c:forEach items="${meeting.event.attendees}" var="attendee">
                            <c:if test="${not attendee.compulsory}">
                            <c:if test="${attendee.status=='C'}">
                                <% if (isStart) { %>
                                    <br><B><fmt:message key="calendar.label.confirmed"/>:</B><br>
                                <% } isStart=false; %>
                                <% if (hasAttendee) { %>, <% } %>
                                <c:out value="${attendee.name}"/>
                                <% hasAttendee=true; %>
                            </c:if>
                            </c:if>
                        </c:forEach>
                        <% if (hasAttendee) { %><BR><% } %>
                        <%-- if (!hasAttendee) { %>--<% } --%>

                        <% hasAttendee=false; isStart=true;%>

                        <c:forEach items="${meeting.event.attendees}" var="attendee">
                            <c:if test="${not attendee.compulsory}">
                            <c:if test="${attendee.status=='P'}">
                                <% if (isStart) { %>
                                    <br><B><fmt:message key="calendar.label.pending"/>:</B><br>
                                <% } isStart=false; %>
                                <% if (hasAttendee) { %>, <% } %>
                                <c:out value="${attendee.name}"/>
                                <% hasAttendee=true; %>
                            </c:if>
                            </c:if>
                        </c:forEach>
                        <% if (hasAttendee) { %> <BR> <% } %>
                        <%-- if (!hasAttendee) { %>--<% } --%>

                        <% hasAttendee=false; isStart=true; %>

                        <c:forEach items="${meeting.event.attendees}" var="attendee">
                            <c:if test="${not attendee.compulsory}">
                            <c:if test="${attendee.status=='R'}">
                                <% if (isStart) { %>
                                    <br><B><fmt:message key="calendar.label.rejected"/>:</B> <br>
                                <% } isStart=false; %>
                                <% if (hasAttendee) { %>, <% } %>
                                <c:out value="${attendee.name}"/>
                                <% hasAttendee=true; %>
                            </c:if>
                            </c:if>
                        </c:forEach>
                        <% if (hasAttendee) { %><BR><% } %>
                        <%-- if (!hasAttendee) { %>--<% } --%>

                        <%-- kurnia - end --%>
                        <%--
                        <B><fmt:message key='calendar.label.attendees'/>:</B>
                        <c:forEach items="${meeting.event.attendees}" var="attendee" begin="0" varStatus="status"  >
                          <c:if test="${status.index>0}" >,</c:if>
                            <c:out value="${attendee.name}" /> (<c:out value="${attendee.status}"/>)
                        </c:forEach>
                        --%>
                    </c:if>
                    <!-- </Font> -->
                    <hr size="1" width="80%" align="left">
                    </LI>
                </p>

            </c:forEach>
            <%  if(i==0) {
            %>
                <fmt:message key='calendar.label.noEMeeting'/>
            <%
                }
                i=0;
            %>
         </UL>
     </p>

   <p>
        <B><FONT COLOR="#003366"><fmt:message key='calendar.label.todayEvents'/></FONT></B>
         <UL>
     <c:forEach var="event" items="${entry.events}" begin="0" varStatus="status"  >
        <%i++;%>
            <c:set var="event" value="${event}" scope="request"/>
            <LI><jsp:include page="calendarEventSummary.jsp" flush="true"/> </LI> <br>
        <c:if test="${status.last}">
        <hr size="1" width="80%" align="left">
         </p>
        </c:if>
    </c:forEach>
    <%  if(i==0) {
    %>
                <fmt:message key='calendar.label.noEvent'/>
        <%
        }
        i=0;
        %>
         </UL>
            &nbsp;
     </p>

     <%--<p>
        <B><FONT COLOR="#003366"><fmt:message key='calendar.label.toDoList'/></FONT></B>
         <UL>
            <c:set var="tasks" value="${widget.taskViews}"/>
            <c:forEach var="task" items="${tasks}" varStatus="status" >
            <%i++;%>
                <p>
                    <%--<c:set var="event" value="${event}" scope="request"/>
                    <jsp:include page="calendarEventSummary.jsp" flush="true"/>--%>
     <%--           <LI VALIGN="TOP">

                        <c:set var="id" value="${task.absoluteName}"/>

                        <x:display name="<%=(String)pageContext.getAttribute("id")%>" ></x:display>
                        <hr size="1" width="80%" align="left">
                    </LI>
                </p>

            </c:forEach>
            <%  if(i==0) {
            %>
                <fmt:message key='calendar.label.noToDo'/>
        <%
                }
                i=0;
            %>
         </UL>
     </p>--%>

  </BLOCKQUOTE>
  </td>
</tr>
 <Tr>
    <td class="calendarRow" ><fmt:message key='calendar.label.statusLabel'/></td>

 </tr>

  <tr>
 <Td BGCOLOR="#003366" CLASS="calendarFooter" align="right">
&nbsp;
</td></tr>




</table>

<%--
    <p>
    <input type="submit" class="button" value="Delete">
--%>


