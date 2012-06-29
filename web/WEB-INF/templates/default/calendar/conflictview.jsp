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

                    </b>
                    <br>
                    <FONT COLOR="#FF0000">
                    <c:choose>
                        <c:when test="${event.moreThanOneDay}">
                            <fmt:formatDate value="${event.startDate}" pattern="${globalDatetimeLong}" />
                            - <fmt:formatDate value="${event.endDate}" pattern="${globalDatetimeLong}" />
                        </c:when>
                        <c:otherwise>
                            <fmt:formatDate value="${event.startDate}" pattern="${globalTimeLong}" />
                            - <fmt:formatDate value="${event.endDate}" pattern="${globalTimeLong}" /> (<fmt:formatDate value="${event.startDate}" pattern="${globalDateLong}" />)
                        </c:otherwise>
                    </c:choose>
                    </FONT>
                    <br>

                    <c:choose>
                    <c:when test="${not event.hidden}" >
                    <c:out value="${event.title}"/> <c:if test="${event.recurrence}" >(<fmt:message key='calendar.label.Recurrence'/>)</c:if>
                    <c:set scope="page" value="${widget.widgetManager.user.id}" var="userId"/>

                    <c:if test="${event.attendees != null}">
                         <c:set var="userId" value="${widget.widgetManager.user.id}"/>
                    </c:if>

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
            <%  if(i==0) { %>
                <fmt:message key='calendar.label.noAppointment'/>
            <%
                }
                i=0;
            %>
         </UL>
     </p>
    </BLOCKQUOTE>

    </td>
</tr>
</table>
