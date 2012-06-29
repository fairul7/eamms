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
                 com.tms.collab.taskmanager.model.Task,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>



<%
    pageContext.setAttribute("conflicts",session.getAttribute("conflicts"));
%>

<html>
    <title><fmt:message key='taskmanager.label.conflicts'/></title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
    <link rel="stylesheet" href="../images/default.css">

 <table BORDER="0" CELLSPACING="1" CELLPADDING="3" width="100%">
<tr>
    <td class="calendarHeader" align="LEFT">
        <fmt:message key='taskmanager.label.conflicts'/>
    </td>
</tr>

<tr>
    <td class="calendarRow" align="LEFT">
     <BLOCKQUOTE>
         <UL>

         <%int i = 0;%>
            <c:forEach var="event" items="${conflicts}" varStatus="status" >
                <p>
                <LI VALIGN="TOP">
                    <b>
                        <%
                            String eventId = ((CalendarEvent)pageContext.getAttribute("event")).getEventId();
                            if(eventId!=null){
                                if(eventId.startsWith(CalendarEvent.class.getName())){
                                    out.print("Event");
                                }
                                else if(eventId.startsWith(Appointment.class.getName()))
                                    out.print("Appointment");
                                else if(eventId.startsWith(Meeting.class.getName()))
                                    out.print("E-Meeting");
                                else if(eventId.startsWith(Task.class.getName()))
                                    out.print("To Do Task");
                            }
                           CalendarEvent event = (CalendarEvent)pageContext.getAttribute("event");
                            if(event.getStartDate()==null)
                                event.setStartDate(event.getCreationDate());
                        %>

                    </b>   <br>
                    <FONT COLOR="#FF0000"><fmt:formatDate value="${event.startDate}" pattern="${globalTimeLong}" />
                    - <fmt:formatDate value="${event.endDate}" pattern="${globalTimeLong}" />(<fmt:formatDate value="${event.startDate}" pattern="${globalDateLong}" />
                    - <fmt:formatDate value="${event.endDate}" pattern="${globalDateLong}" />)
                    </FONT>  <br>
                    <x:event name="${widget.absoluteName}" type="select" param="eventId=${event.eventId}"  >
                    <c:out value="${event.title}"/> <c:if test="${event.recurrence}" >(<fmt:message key='taskmanager.label.recurrence'/>)</c:if>   </x:event>
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
                    <B><fmt:message key='taskmanager.label.location'/>:</B> <c:out value="${event.location}" />
                     | </c:if>
                     <B><fmt:message key='taskmanager.label.scheduledby'/>:</B> <c:out value="${event.userName}" />
                    <br>
                    <c:if test="${!event.personal}" >
                        <B><fmt:message key='taskmanager.label.attendees'/>:</B>
                        <c:forEach items="${event.attendees}" var="attendee" begin="0" varStatus="status"  >
                          <c:if test="${status.index>0}" >,</c:if>
                            <c:out value="${attendee.name}" /> (<c:out value="${attendee.status}"/>)
                        </c:forEach>
                    </c:if>
                    <c:if test="${!empty event.resources}" >
                            <b>    <fmt:message key='taskmanager.label.resources'/>: </b>
                       <c:forEach items="${event.resources}" var="resource" begin="0" varStatus="status" >
                                <c:if test="${status.index>0}" >,</c:if> <c:out value="${resource.name}" />(<c:out value="${resource.status}" />)
                                </c:forEach>

                        </c:if>
                    <hr size="1" width="90%" align="left">
                </LI>
                </p>
                <%i++;%>
            </c:forEach>
            <%if(i==0)
                out.println("No appoinment found.");
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
--%> </body>
</html>
