<%@ page import="java.util.Collection,
                 java.util.Set,
                 java.util.TreeSet,
                 com.tms.collab.calendar.ui.CalendarView,
                 com.tms.collab.calendar.model.CalendarEvent,
                 kacang.services.security.SecurityService,
                 kacang.Application,
                 com.tms.collab.calendar.model.CalendarModule,
                 com.tms.collab.calendar.model.Appointment,
                 com.tms.collab.emeeting.Meeting,
                 com.tms.collab.taskmanager.model.Task"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="view" value="${widget}"/>

 <table BORDER="0" CELLSPACING="1" CELLPADDING="3" width="100%">
<tr>
    <td class="calendarHeader" align="LEFT"><fmt:message key='calendar.label.conflicts'/></td>
</tr>

<tr>
    <td class="calendarRow" align="LEFT">
     <BLOCKQUOTE>
         <UL>

         <%int i = 0;%>
            <c:forEach var="event" items="${view.conflicts}" varStatus="status" >
                <p>
                <LI VALIGN="TOP">
                    <b>
                        <%
                            String eventId = ((CalendarEvent)pageContext.getAttribute("event")).getEventId();
                            String eventType = null;
                            if(eventId!=null){
                                if(eventId.startsWith(CalendarEvent.class.getName())){
                                    eventType = "Event";
                                }
                                else if(eventId.startsWith(Appointment.class.getName()))
                                    eventType = "Appointment";
                                else if(eventId.startsWith(Meeting.class.getName()))
                                    eventType = "E-Meeting";
                                else if(eventId.startsWith(Task.class.getName()))
                                    eventType = "To Do Task";
                            }
                            out.print(eventType);
                            pageContext.setAttribute("eventType",eventType);

                        %>

                    </b>   <br>
                    <FONT COLOR="#FF0000"><fmt:formatDate value="${event.startDate}" pattern="${globalTimeLong}" />
                    - <fmt:formatDate value="${event.endDate}" pattern="${globalTimeLong}" />(<fmt:formatDate value="${event.startDate}" pattern="${globalDateLong}" />
                    - <fmt:formatDate value="${event.endDate}" pattern="${globalDateLong}" />)
                    </FONT>  <br>
                    <c:choose>
                    <c:when test="${not event.hidden}" >
                    <c:out value="${event.title}"/> <c:if test="${event.recurrence}" >(<fmt:message key='calendar.label.Recurrence'/>)</c:if>
                    <c:set scope="page" value="${widget.widgetManager.user.id}" var="userId"/>

                    <c:if test="${event.attendees != null}">
                         <c:set var="userId" value="${widget.widgetManager.user.id}"/>
                       <%--   <c:forEach items="${event.attendees}" var="attendee">
                         <c-rt:set var="att_status" value="<%=CalendarModule.ATTENDEE_STATUS_PENDING%>"  />

                               <c:if test="${userId == attendee.userId && attendee.status == att_status}" >
                                <c:set var="attendeeId" value="${attendee.attendeeId}"/>
                                <%pageContext.setAttribute("canAccept",Boolean.TRUE);%>
                            </c:if>
                         </c:forEach>--%>
                    </c:if>
                  <%--  <c:if test="${canEdit||canDelete||canAccept}" >
                    <br>
                    </c:if>
                    <c:if test="${canEdit}" >
                       <input value="Edit" type="button" class="button" onClick="document.location = 'editappointmentformpop.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />'"/>
                    </c:if>

                     <c:if test="${canDelete}" >
                     <input value="Delete" type="button" class="button" onClick="if(confirm('Are you sure to delete this appointment')){document.location='<c:out value="${deleteUrl}"/>&eventId=<c:out value="${event.eventId}"/>';}"/>

                    </c:if>
                    <c:if test="${canAccept}" >
                     <input value="Accept" type="button" class="button" onClick="document.location = '<c:out value="${acceptUrl}" />&eventId=<c:out value="${event.eventId}"/>&instanceId=<c:out value="${event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>';"/>
                     <input value="Reject" type="button" class="button" onClick="window.open('rejectform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />&instanceId=<c:out value="${event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>','rejectform','scrollbars=yes,resizable=yes,width=400,height=200');return false;"/>

                    </c:if>--%>
                  <%-- <%pageContext.setAttribute("canEdit",Boolean.FALSE);%>
                   <%pageContext.setAttribute("canDeletet",Boolean.FALSE);%>
                   <%pageContext.setAttribute("canAccept",Boolean.FALSE);%>--%>

                    <br><B><FONT COLOR="#9C9A9C">. . . . . . . . . . . . . . . . . . . . . .</FONT></B><BR>
                    <FONT SIZE="2" CLASS="calendarContentFont">
                    <c:if test="${event.location!=null && event.location != ''}" >
                    <B><fmt:message key='calendar.label.location'/>:</B> <c:out value="${event.location}" />
                     | </c:if>
                     <B><fmt:message key='calendar.label.scheduledby'/>:</B> <c:out value="${event.userName}" />
                    <br>
                    <c:if test="${!event.personal}" >
                        <B><fmt:message key='calendar.label.attendees'/>:</B>
                        <c:forEach items="${event.attendees}" var="attendee" begin="0" varStatus="status"  >
                          <c:if test="${status.index>0}" >,</c:if>
                            <c:out value="${attendee.name}" /> (<c:out value="${attendee.status}"/>)
                        </c:forEach>
                    </c:if>
                    <c:if test="${!empty event.resources}" >
                            <b><fmt:message key='calendar.label.resources'/>:</b>
                       <c:forEach items="${event.resources}" var="resource" begin="0" varStatus="status" >
                                <c:if test="${status.index>0}" >,</c:if> <c:out value="${resource.name}" />(<c:out value="${resource.status}" />)
                                </c:forEach>

                        </c:if>
                    <hr size="1" width="90%" align="left">
                </LI>
                </p>
                </c:when>
                <c:otherwise>
                  Private <c:out value="${eventType}"/>
                </c:otherwise>
                </c:choose>
                <%i++;%>
            </c:forEach>
            <%if(i==0)
                %>
                <fmt:message key='calendar.label.noAppointment'/>
            <%
                i=0;
            %>
         </UL>
     </p>
    </BLOCKQUOTE>

    </td>
</tr>
</table>
             <%--<%
                        String userId = (String)pageContext.getAttribute("userId");
                        CalendarEvent event = (CalendarEvent)pageContext.getAttribute("event");
                        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);


                        if(event.getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null) ||ss.hasPermission(userId,CalendarModule.PERMISSION_EDIT_EVENTS,null,null)){

                            pageContext.setAttribute("canEdit", Boolean.TRUE);
                        }
                        if(event.getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null)
                                ||ss.hasPermission(userId,CalendarModule.PERMISSION_DELETE_EVENTS,null,null)){
                                    pageContext.setAttribute("canDelete", Boolean.TRUE);
                                }
  %>
--%>
