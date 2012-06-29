<%@ page import="com.tms.collab.calendar.ui.CalendarView,
                 kacang.ui.Event,
                 com.tms.collab.resourcemanager.model.ResourceManager,
                 com.tms.collab.calendar.model.Appointment,
                 com.tms.collab.calendar.model.CalendarEvent,
                 org.apache.commons.lang.StringUtils,
                 com.tms.collab.forum.model.Message,
                 com.tms.collab.calendar.model.CalendarModule"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
 <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>

<c:set var="acceptUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.parent.parent.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=accept

</c:set>
<c:set var="deleteUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETE %>
</c:set>
<c:set var="deleteAllUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETEALL %>
</c:set>
<c:if test="${forward.name == 'Appointment deleted' }" >
    <script>
        alert("<fmt:message key="calendar.message.appointmentDeleted"/>");
                document.location = "<%= response.encodeURL(request.getRequestURI()) %>?cn=calendarPage.calendarView&et=view&view=daily";

    </script>
 </c:if>
<c:if test="${forward.name == 'Appointment deleted' }" >
    <script>
        alert("<fmt:message key="calendar.message.eventDeleted"/>");
                document.location = "<%= response.encodeURL(request.getRequestURI()) %>?cn=calendarPage.calendarView&et=view&view=daily";


    </script>
 </c:if>


<c:set var="event" value="${widget.event}"/>

<table  cellpadding="4" cellspacing="1" class="classBackground" width="100%">
    <tr align ="center">
        <td class="classRowLabel" align="right" width="30%" valign="top">
           <b><fmt:message key='calendar.label.title'/></td> <td class="classRow" align="left"><c:out value="${event.title}" /> <c:if test="${event.recurrence}" >(<fmt:message key='calendar.label.Recurrence'/>)</c:if></b>
        </td>
    </tr>
  <c:if test="${!event.allDay}" >
    <tr align="center">
        <td class="classRowLabel" align="right" valign="top"><fmt:message key='calendar.label.startTime'/></td>  <td class="classRow" align="left">
            <%--<c:choose>
            <c:when test="${event.allDay}" ><fmt:message key='calendar.label.allDay'/></c:when>
            <c:otherwise>--%>
            <fmt:formatDate pattern="h:mma" value="${event.startDate}" />, <fmt:formatDate pattern="${globalDateLong}" value="${event.startDate}" />
<%--
            </c:otherwise>
            </c:choose>
--%>
         </td>
    </tr>
    <tr align="center">
        <td class="classRowLabel" align="right" valign="top"><fmt:message key='calendar.label.endTime'/></td>  <td class="classRow" align="left">

            <fmt:formatDate pattern="h:mma" value="${event.endDate}" />, <fmt:formatDate pattern="${globalDateLong}" value="${event.endDate}" />

        </td>
    </tr>
 </c:if>
  <c:if test="${event.allDay}" >
        <tr align="center">
        <td class="classRowLabel" align="right" valign="top"><fmt:message key='calendar.label.startDate'/></td>  <td class="classRow" align="left">
                       <fmt:formatDate pattern="${globalDateLong}" value="${event.startDate}" />
            </td>
            </tr>
        <tr align="center">
        <td class="classRowLabel" align="right" valign="top"><fmt:message key='calendar.label.endDate'/></td>  <td class="classRow" align="left">
                       <fmt:formatDate pattern="${globalDateLong}" value="${event.endDate}" />
            </td>
            </tr>

       <tr align="center">
        <td class="classRowLabel" align="right" valign="top"><fmt:message key='calendar.label.duration'/></td>  <td class="classRow" align="left"><fmt:message key='calendar.label.allDay'/></td>
            </tr>

 </c:if>

    <c:if test="${not empty event.location}">
    <tr align="center">
        <td class="classRowLabel"  align="right" valign="top"><fmt:message key='calendar.label.location'/></td><td class="classRow" align="left">
            <c:out value="${event.location}" />
       </td>
    </tr>
   </c:if>


    <tr align ="center">
        <td class="classRowLabel" align="right" valign="top"><fmt:message key='calendar.label.classification'/></td>  <td class="classRow" align="left" valign="top">
           <c:set var="cla" value="${event.classification}"/>
           <c:if test="${cla == 'pub'}" ><fmt:message key='calendar.label.public'/></c:if>
           <c:if test="${cla == 'pri'}" ><fmt:message key='calendar.label.private'/></c:if>
           <c:if test="${cla == 'con'}" ><fmt:message key='calendar.label.confidential'/></c:if>
        </td>
    </tr>
      <% int i=0;%>
    <c:set var="eventId" value="${event.eventId}"/>
    <%
        String eventId = (String)pageContext.getAttribute("eventId");
        if(eventId.startsWith(Appointment.class.getName())){
    %>
    <tr>
        <td class="classRowLabel" align="right" valign="top"><fmt:message key='calendar.label.compulsoryAttendees'/></td> <td class="classRow" align="left" valign="top"><c:forEach var="att" items="${event.attendees}"  >
                                   <c:if test="${att.compulsory}" ><%if(i>0){%>,<%}%> <c:out value="${att.name}" /><%--(<c:out value="${att.status}" />)--%><%i++;%></c:if>
                                   </c:forEach> <%if(i==0){%> - <%}%>
        </td>
    </tr>

    <%-- customisation on 2005-07-13 - start --%>
    <% i=0; boolean isStart=true; %>
    <tr>
        <td class="classRowLabel" align="right" valign="top"><fmt:message key="calendar.label.optionalAttendees"/></td>
        <td class="classRow">

        <c:forEach var="attendee" items="${event.attendees}">
            <c:if test="${not attendee.compulsory}">
                <c:if test="${attendee.status=='C'}">
                    <% if (isStart) { %>
                        <b><fmt:message key="calendar.label.confirmed"/></b><br>
                    <% } isStart=false; %>
                    <% if (i>0) { %>, <%}%>
                    <c:out value="${attendee.name}"/>
                    <% i++; %>
                </c:if>
            </c:if>
        </c:forEach>
        <% if (i>0) { %> <br><% } %>

        <% i=0; isStart=true; %>

        <c:forEach var="attendee" items="${event.attendees}">
            <c:if test="${not attendee.compulsory}">
                <c:if test="${attendee.status=='P'}">
                    <% if (isStart) { %>
                        <b><fmt:message key="calendar.label.pending"/></b><br>
                    <% } isStart=false; %>
                    <% if (i>0) { %>, <%}%>
                    <c:out value="${attendee.name}"/>
                    <% i++; %>
                </c:if>
            </c:if>
        </c:forEach>
        <% if (i>0) { %><BR><% } %>

        <% i=0; isStart=true; %>

        <c:forEach var="attendee" items="${event.attendees}">
            <c:if test="${not attendee.compulsory}">
                <c:if test="${attendee.status=='R'}">
                    <% if (isStart) { %>
                        <b><fmt:message key="calendar.label.rejected"/></b><br>
                    <% } isStart=false; %>
                    <% if (i>0) { %>, <%}%>
                    <c:out value="${attendee.name}"/>
                    <% i++; %>
                </c:if>
            </c:if>
        </c:forEach>
        <% if (i>0) { %><BR><% } %>
        </td>
    </tr>
    <%--
    <%i=0;%>
    <c:forEach var="att" items="${event.attendees}"  >
        <c:if test="${not att.compulsory}" >
          <%if(i==0){%>
            <tr>
                <td class="classRowLabel" align="right" valign="top"><fmt:message key='calendar.label.optionalAttendees'/></td> <td class="classRow" align="left" valign="top">
          <%}%>
          <%if(i>0){%>,<%}%>
          <c:out value="${att.name}" />(<c:out value="${att.status}" />)
          <%i++;%>
        </c:if>
    </c:forEach>
        <%if(i>0){%>
                </td>
            </tr>
        <%}%>                                                       meeting

    <%-- customisation on 2005-07-13 - end --%>
    <%}%>

    <c:set var="des" value="${event.description}"/>
    <c:if test="${! empty des}">
     <%
         String translated = StringUtils.replace((String)pageContext.getAttribute("des"), "\n", "<br>");
         pageContext.setAttribute("des", translated);
                                    %>

        <tr>
        <td class="classRowLabel" align="right" valign="top"><fmt:message key='calendar.label.description'/></td> <td class="classRow" align="left"><c:out value="${des}"escapeXml="false"  />
        </td>
    </tr>
    </c:if>
      <c:set var="agenda" value="${event.agenda}"/>
        <c:if test="${not empty agenda}">
        <%
            String translated = StringUtils.replace((String)pageContext.getAttribute("agenda"), "\n", "<br>");
            pageContext.setAttribute("agenda", translated);
                                    %>
    <tr>
        <td class="classRowLabel" align="right" valign="top"><fmt:message key='calendar.label.agenda'/></td> <td class="classRow"><table><tr><td class="classRow" align="left"><c:out value="${agenda}" escapeXml="false"  /> </td></tr></table>
        </td>
    </tr>
    </c:if>

    <c:if test="${!empty event.resources}" >
    <tr>
        <td class="classRowLabel" align="right" valign="top"><fmt:message key='calendar.label.resources'/></td> <td class="classRow" align="left">
            <%i=0;%>
            <c:forEach items="${event.resources}" var="resource"  >
            <%if(i>0) out.print(",");%>
            <c:out value="${resource.name}" />(<c:out value="${resource.status}" />)<%i++;%>
            </c:forEach>

        </td>
    </tr>
    </c:if>
    <c:if test="${event.lastModifiedBy!=null}" >
    <tr>
        <td class="classRowLabel" align="right" valign="top"><fmt:message key='calendar.label.lastmodified'/></td> <td class="classRow" align="left"><fmt:formatDate pattern="${globalDatetimeLong}" value="${event.lastModified}" />
            <c:set var="lastModifiedName" value="${event.lastModifiedName}"/>
            <c:if test="${!empty lastModifiedName}"><fmt:message key='calendar.label.by'/> <c:out value="${lastModifiedName}"/></c:if>
        </td>
    </tr>
    </c:if>

    <c:if test="${event.attendees != null}">
                         <c:set var="userId" value="${widget.widgetManager.user.id}"/>
                         <c:forEach items="${event.attendees}" var="attendee">
                         <c-rt:set var="att_status" value="<%=CalendarModule.ATTENDEE_STATUS_PENDING%>"  />

                                <c:if test="${userId == attendee.userId && attendee.status == att_status}" >
                                <c:set var="attendeeId" value="${attendee.attendeeId}"/>
                                <%pageContext.setAttribute("canAccept",Boolean.TRUE);%>
                            </c:if>
                         </c:forEach>
                    </c:if>
   <c:if test="${!widget.parent.hiddenAction}" >
    <tr>
        <td class="classRowLabel" align="right" valign="top">
            </td> <td class="classRow" align="left">
            <c:if test="${widget.parent.editable}" >
            <%
                if(eventId.startsWith(Appointment.class.getName())){
            %>
            <input value="Edit" type="button" class="button" onClick="document.location = 'editappointmentform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />'"/>
            <%} else if(eventId.startsWith(CalendarEvent.class.getName())){
            %>
            <input value="Edit" type="button" class="button" onClick="document.location = 'editeventform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />'"/>
            <%}%>


            </c:if>
            <c:if test="${widget.parent.deleteable}" >
                <c:if test="${event.recurrence}">
                    <INPUT type="button" class="button" value="<fmt:message key="calendar.label.buttonDelete"/>" onClick="if(confirm('<fmt:message key="calendar.message.confirmDelete"/>')){document.location = '<c:out value="${deleteUrl}" />'}"/>
                </c:if>
                <INPUT type="button" class="button" value="<c:if test="${empty event.recurrenceRule}"><fmt:message key="calendar.label.buttonDelete"/></c:if><c:if test="${not empty event.recurrenceRule}"><fmt:message key="calendar.label.buttonDeleteAll"/></c:if>" onClick="if(confirm('<fmt:message key="calendar.message.confirmDelete"/>')){document.location = '<c:out value="${deleteAllUrl}" />'}"/>
            </c:if>

            <c:if test="${widget.parent.acceptReject && canAccept}" >
                 <input value="<fmt:message key="calendar.label.buttonAccept"/>" type="button" class="button" onClick="document.location = '<c:out value="${acceptUrl}" />&eventId=<c:out value="${event.eventId}"/>&instanceId=<c:out value="${event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>';"/>
                 <input value="<fmt:message key="calendar.label.buttonReject"/>" type="button" class="button" onClick="window.open('rejectform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />&instanceId=<c:out value="${event.instanceId}" />&attendeeId=<c:out value="${attendeeId}"/>','rejectform','scrollbars=yes,resizable=yes,width=400,height=200');return false;"/>
            </c:if>

        </td>
    </tr>
 </c:if>


</table>

