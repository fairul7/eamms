<%@ page import="java.util.Set,
                 com.tms.collab.calendar.ui.ConflictForm,
                 java.util.Collection,
                 java.util.Iterator,
                 com.tms.collab.resourcemanager.model.ResourceBooking"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="view" value="${widget}"/>
      <jsp:include page="../form_header.jsp" flush="true"/>

<table  cellpadding="4" cellspacing="1" class="forumBackground" width="100%">

<tr><Td ALIGN="LEFT" class="CalendarHeader" colspan="3"><B><fmt:message key='calendar.label.conflictsDetected'/></B>
</td></tr>

<tr>
    <td class="calendarRow" colspan="3" align="center"><b>
    <c:out value="${view.header}" />
    </b>
</tr>

<tr>
    <td class="calendarRowLabel" align="right"><fmt:message key='calendar.label.title'/></td>
    <td align="left" class="calendarRow" colspan="2">
           <FONT COLOR="#FF0000">
               <c:out value="${view.event.title}"/>
           </FONT>
    </td>
</tr>

<tr>
    <td class="calendarRowLabel" align="right">
          <c:if test="${empty view.event.startDate}" ><fmt:message key='calendar.label.due'/></c:if><fmt:message key='calendar.label.date'/></td>
    <td align="left" class="calendarRow" colspan="2">
           <FONT COLOR="#FF0000">       <c:if test="${!empty view.event.startDate}" >
  <fmt:formatDate value="${view.event.startDate}" pattern="${globalDateLong}" /> - </c:if>
                                            <fmt:formatDate value="${view.event.endDate}" pattern="${globalDateLong}" />
           </FONT>
    </td>
</tr>

<tr>
    <td class="calendarRowLabel" align="right">
    <c:if test="${empty view.event.startDate}" ><fmt:message key='calendar.label.due'/></c:if><fmt:message key='calendar.label.time'/></td>
    <td align="left" class="calendarRow" colspan="2">
           <FONT COLOR="#FF0000">         <c:if test="${!empty view.event.startDate}" >
<fmt:formatDate value="${view.event.startDate}" pattern="h:mm a" /> - </c:if>
                                            <fmt:formatDate value="${view.event.endDate}" pattern="h:mm a" />
           </FONT>
    </td>
</tr>

<c:set var="des" value="${event.description}"/>
<c:if test="${!empty des}">
    <%
        String translated = StringUtils.replace((String)pageContext.getAttribute("des"), "\r", "<br>");
        pageContext.setAttribute("des", translated);
    %>

    <tr>
        <td class="calendarRowLabel" align="right" valign="top"><fmt:message key='emeeting.label.description'/></td> <td class="calendarRow" align="left"> <FONT COLOR="#FF0000"><c:out value="${des}"escapeXml="false" /></FONT>
    </td>
    </tr>
</c:if>

 <%--<tr>
    <td class="calendarRowLabel" align="right"><fmt:message key='calendar.label.description'/></td>
    <td align="left" class="calendarRow" colspan="2">
           <FONT COLOR="#FF0000">
            <c:choose>
                <c:when test="${view.header=='E-Meeting'}" >
                    <c:out value="${view.event.event.description}"/>
                </c:when>
                <c:otherwise>
                    <c:out value="${view.event.description}"/>
                </c:otherwise>
            </c:choose>
           </FONT>
    </td>
 </tr>--%>

 <tr>
    <Td class="calendarRow" colspan="3" align="center">
       <x:event name="${widget.absoluteName}" type="<%=ConflictForm.EVENT_TYPE_VIEW_ALL%>"  ><fmt:message key='calendar.label.viewAllConflicts'/></x:event>
    </td>
 </tr>

 <tr>
    <td class="calendarRow" colspan="3">
        &nbsp;
    </td>
  </tr>
          <c:set var="conflictAttendees" value="${view.conflictAttendees}"/>
 <%
     Set conflictAttendees = (Set)pageContext.getAttribute("conflictAttendees");
     String userId;
 %>
<c:if test="${!widget.ownerExcluded}" >
 <tr>
    <td class="calendarFooter" align="left"  width="30%">
        <b><fmt:message key='calendar.label.scheduler'/></b>
    </td>

    <td class="calendarFooter" align="left">
       <b><fmt:message key='calendar.label.status'/></b>
    </td>

    <td class="calendarFooter" align="left">
        <b><fmt:message key='calendar.label.confirm'/>?</b>
    </td>
 </tr>



         <c:set var="userId" value="${view.event.userId}"/>
        <%
            userId = (String)pageContext.getAttribute("userId");
            if(conflictAttendees.contains(userId))
                pageContext.setAttribute("hasConflict",Boolean.TRUE);
            else
                pageContext.setAttribute("hasConflict",Boolean.FALSE);
        %>


  <tr>
    <td class="calendarRow" align="left" valign="top">   &nbsp;
    <c:if test="${hasConflict}" >
        <x:event name="${widget.absoluteName}" type="<%=ConflictForm.EVENT_TYPE_VIEW_USER%>" param="userId=${userId}">
        <c:out value="${view.event.userName}"/></x:event>
    </c:if>
    <c:if test="${!hasConflict}" >
        <c:out value="${view.event.userName}"/>
    </c:if>
    </td>

    <td class="calendarRow" align="left">
        <c:if test="${hasConflict}">
            <FONT COLOR="#FF0000"><fmt:message key='calendar.label.conflict'/></FONT>
             <c:forEach items="${view.event.attendees}" var="att" varStatus="status"  >
                <c:if test="${att.userId==view.event.userId}" >
                    <c:if test="${!empty att.propertyMap['conflicts']}">
                        <table width="90%" align="right">
                            <tr>
                                <td style="text-decoration:underline" width="5%"><fmt:message key="general.label.number"/></td>
                                <td style="text-decoration:underline" width="60%"><fmt:message key="calendar.label.title"/></td>
                                <td style="text-decoration:underline" width="35%"><fmt:message key="calendar.label.scheduledby"/></td>
                            </tr>
                            <c:forEach items="${att.propertyMap['conflicts']}" var="conflictEvent" varStatus="stat">
                            <tr>
                                <td><c:out value="${stat.index+1}"/></td>
                                <td>
                                <c:if test="${!conflictEvent.hidden}">
                                    <c:out value="${conflictEvent.title}"/>
                                </c:if>
                                <c:if test="${conflictEvent.hidden}">
                                    <fmt:message key="calendar.label.privateAppointment"/>
                                </c:if>
                                </td>
                                <td><c:out value="${conflictEvent.userName}"/></td>
                            </tr>
                            </c:forEach>
                        </table>
                    </c:if>
                </c:if>
            </c:forEach>
         </c:if>
        <c:if test="${!hasConflict}"><fmt:message key='calendar.label.oK'/></c:if>

    </td>

    <td class="calendarRow" align="left" width="10%" valign="top">
     <c:if test="${hasConflict}">
        <input type="checkbox" name="comAttendeesCB" value="<c:out value="${view.event.userId}"/> " CHECKED>
      </c:if>
            <c:if test="${!hasConflict}">
        <input type="checkbox" name="comAttendeesCB" value="<c:out value="${view.event.userId}"/> " style="display:none" CHECKED>
      </c:if>

      </td>
 </tr>
 <tr>
    <td class="calendarRow" colspan="3">
        &nbsp;
    </td>
  </tr>
 </c:if>

 <%int i = 0;%>
 <c:forEach items="${view.event.attendees}" var="att" varStatus="status"  >
    <c:if test="${att.compulsory&&att.userId!=view.event.userId}" >
       <% if(i==0){%>
        <tr>
            <Td class="calendarFooter" align="left" width="30%">
              <b><fmt:message key='calendar.label.compulsoryAttendees'/></b>
            </td>

            <td class="calendarFooter" align="left">
               <b><fmt:message key='calendar.label.status'/></b>
            </td >

            <td class="calendarFooter" align="left">
              <b><fmt:message key='calendar.label.confirm'/>?</b>
             </td>
        </tr>
       <%}%>
       <tr>
               <c:set var="userId" value="${att.userId}"/>

        <%
            userId = (String)pageContext.getAttribute("userId");
            if(conflictAttendees.contains(userId))
                pageContext.setAttribute("hasConflict",Boolean.TRUE);
            else
                pageContext.setAttribute("hasConflict",Boolean.FALSE);
        %>

    <td class="calendarRow" align="left" valign="top" valign="top"> &nbsp;
    <c:if test="${hasConflict}" >
        <x:event name="${widget.absoluteName}" type="<%=ConflictForm.EVENT_TYPE_VIEW_USER%>" param="userId=${att.userId}">
        <c:out value="${att.name}"/></x:event>
    </c:if>
    <c:if test="${!hasConflict}" >
        <c:out value="${att.name}"/>
    </c:if>

    </td>

    <td class="calendarRow" align="left">
        <c:if test="${hasConflict}">
            <FONT COLOR="#FF0000"><fmt:message key='calendar.label.conflict'/></FONT>
            <c:if test="${!empty att.propertyMap['conflicts']}">
            <table width="90%" align="right">
                <tr>
                    <td style="text-decoration:underline" width="5%"><fmt:message key="general.label.number"/></td>
                    <td style="text-decoration:underline" width="60%"><fmt:message key="calendar.label.title"/></td>
                    <td style="text-decoration:underline" width="35%"><fmt:message key="calendar.label.scheduledby"/></td>
                </tr>
                <c:forEach items="${att.propertyMap['conflicts']}" var="conflictEvent" varStatus="stat">
                <tr>
                    <td><c:out value="${stat.index+1}"/></td>
                    <td>
                    <c:if test="${!conflictEvent.hidden}">
                        <c:out value="${conflictEvent.title}"/>
                    </c:if>
                    <c:if test="${conflictEvent.hidden}">
                        <fmt:message key="calendar.label.privateAppointment"/>
                    </c:if>
                    </td>
                    <td><c:out value="${conflictEvent.userName}"/></td>
                </tr>
                </c:forEach>
            </table>
            </c:if>
         </c:if>
        <c:if test="${!hasConflict}"><fmt:message key='calendar.label.oK'/></c:if>
    </td>

    <td class="calendarRow" align="left" width="10%" valign="top" valign="top">
     <c:if test="${hasConflict}">
        <input type="checkbox" name="comAttendeesCB" value="<c:out value="${att.userId}"/> " CHECKED>
      </c:if>
      <c:if test="${!hasConflict}">
        <input type="checkbox" name="comAttendeesCB" value="<c:out value="${att.userId}"/> " style="display:none" CHECKED>
      </c:if>

    </td>
       </tr>
       <%i++;%>
    </c:if>
    <c:if test="${status.last}" >
 <%if(i>0){%>
  <tr>
    <td class="calendarRow" colspan="3">
        &nbsp;
    </td>
  </tr>
  <%}%>
 </c:if>
 </c:forEach>



  <% i=0;%>
 <c:forEach items="${view.event.attendees}" var="att" varStatus="status"  >
    <c:if test="${!att.compulsory}" >
       <% if(i==0){%>
        <tr>
            <Td class="calendarFooter" align="left">
              <b><fmt:message key='calendar.label.optionalAttendees'/></b>
            </td>

            <td class="calendarFooter" align="left">
              <b><fmt:message key='calendar.label.status'/></b>
            </td >

            <td class="calendarFooter" align="left">
            <b><fmt:message key='calendar.label.confirm'/>?</b>
             </td>
        </tr>
       <%}%>


    <tr>
     <c:set var="userId" value="${att.userId}"/>

        <%
            userId = (String)pageContext.getAttribute("userId");
            if(conflictAttendees.contains(userId))
                pageContext.setAttribute("hasConflict",Boolean.TRUE);
            else
                pageContext.setAttribute("hasConflict",Boolean.FALSE);
        %>


   <td class="calendarRow" align="left" valign="top" valign="top">
        &nbsp;
        <c:if test="${hasConflict}" >
            <x:event name="${widget.absoluteName}" type="<%=ConflictForm.EVENT_TYPE_VIEW_USER%>" param="userId=${att.userId}">
            <c:out value="${att.name}"/></x:event>
        </c:if>
        <c:if test="${!hasConflict}" >
            <c:out value="${att.name}"/>
        </c:if>
    </td>



    <td class="calendarRow" align="left">
        <c:if test="${hasConflict}">
            <FONT COLOR="#FF0000"><fmt:message key='calendar.label.conflict'/></FONT>
            <c:if test="${!empty att.propertyMap['conflicts']}">
            <table width="90%" align="right">
                <tr>
                    <td style="text-decoration:underline" width="5%"><fmt:message key="general.label.number"/></td>
                    <td style="text-decoration:underline" width="60%"><fmt:message key="calendar.label.title"/></td>
                    <td style="text-decoration:underline" width="35%"><fmt:message key="calendar.label.scheduledby"/></td>
                </tr>
                <c:forEach items="${att.propertyMap['conflicts']}" var="conflictEvent" varStatus="stat">
                <tr>
                    <td><c:out value="${stat.index+1}"/></td>
                    <td><c:out value="${conflictEvent.title}"/></td>
                    <td><c:out value="${conflictEvent.userName}"/></td>
                </tr>
                </c:forEach>
            </table>
            </c:if>
         </c:if>
        <c:if test="${!hasConflict}"><fmt:message key='calendar.label.oK'/></c:if>
    </td>

    <td class="calendarRow" align="left" width="10%" valign="top" valign="top">
         <c:if test="${hasConflict}">
        <input type="checkbox" name="opAttendeesCB" value="<c:out value="${att.userId}"/> " CHECKED>
      </c:if>
      <c:if test="${!hasConflict}">
        <input type="checkbox" name="opAttendeesCB" value="<c:out value="${att.userId}"/> " style="display:none" CHECKED>
      </c:if>

    </td>
       </tr>
       <%i++;%>
    </c:if>
    <c:if test="${status.last}" >
  <%if(i>0){%>
   <tr>
    <td class="calendarRow" colspan="3">
        &nbsp;
    </td>
  </tr>
         <%}%>
</c:if>

 </c:forEach>




 <% i=0;%>
 <c:forEach items="${view.event.resources}" var="resource" >

       <% if(i==0){%>
        <tr>
            <Td class="calendarFooter" align="left"  width="30%">
              <b><fmt:message key='calendar.label.resources'/></b>
            </td>

            <td class="calendarFooter" align="left">
              <b><fmt:message key='calendar.label.status'/></b>
            </td >

            <td class="calendarFooter" align="left">
            <b><fmt:message key='calendar.label.confirm'/>?</b>
             </td>
        </tr>
       <%}%>

       <c:set var="resourceId" value="${resource.id}"/>
       <c:set var="resourceList" value="${view.resourceConflicts}"/>
        <%
            pageContext.setAttribute("hasConflict",Boolean.FALSE);
            String resourceId = (String)pageContext.getAttribute("resourceId");
            Collection conflicts = (Collection)pageContext.getAttribute("resourceList");
            for (Iterator iterator = conflicts.iterator(); iterator.hasNext();)
            {
                ResourceBooking resourceBooking = (ResourceBooking) iterator.next();
                if(resourceBooking.getResourceId().equals(resourceId)){
                    pageContext.setAttribute("hasConflict",Boolean.TRUE);
                    break;
                }else {
                    pageContext.setAttribute("hasConflict",Boolean.FALSE);
                }
            }

        %>


       <tr>
        <td class="calendarRow" align="left">
        <c:if test="${hasConflict}" >
           &nbsp; <x:event name="${widget.absoluteName}" type="<%=ConflictForm.EVENT_TYPE_VIEW_RESOURCE%>" param="resourceId=${resource.id}">
            <c:out value="${resource.name}"/></x:event>
         </c:if>
        <c:if test="${!hasConflict}" >
            &nbsp;<c:out value="${resource.name}"/>
         </c:if>
        </td>

        <td class="calendarRow" align="left">
       <c:if test="${hasConflict}">
            <FONT COLOR="#FF0000"><fmt:message key='calendar.label.conflict'/></FONT>
         </c:if>
        <c:if test="${!hasConflict}"><fmt:message key='calendar.label.oK'/></c:if> </td>

        <td class="calendarRow" align="left">

         <c:if test="${hasConflict}">
        <input type="checkbox" name="resourcesCB" value="<c:out value="${resource.id}"/> " CHECKED>
      </c:if>
      <c:if test="${!hasConflict}">
        <input type="checkbox" name="resourcesCB" value="<c:out value="${resource.id}"/> " style="display:none" CHECKED>
      </c:if>
       </td>

       <%i++;%>

</c:forEach>


 <tr>
    <td class="calendarRow" colspan="3">
        &nbsp;
    </td>
 </tr>

 <tr>   <td class="calendarRow">
        &nbsp;
    </td>
    <td class="calendarRow" colspan="2">
       <x:display name="${view.addButton.absoluteName}" /> <x:display name="${view.cancelButton.absoluteName}" ></x:display>
    </td>
 </tr>
 <tr>
    <td class="calendarFooter" colspan="3">
        &nbsp;
    </td>
 </tr><jsp:include page="../form_footer.jsp" flush="true"/>

</table>

<%--
<tr><td><fmt:message key='calendar.message.promptConflict'/></td></tr>
<tr><td><ul>
<c:forEach items="${conflict.conflictList}" var="conflict" >
    <li><a href="" onClick="javascript:window.open('appointmentview.jsp?id=<c:out value="${conflict.eventId}" />&instanceId=<c:out value="${conflict.instanceId}" />','aview','scrollbars=yes,resizable=yes,width=450,height=380'); return false;"><c:out value="${conflict.description}"/> </a>
 (<fmt:formatDate value="${conflict.startDate}" pattern="MMM d, yyyy h:mma"/> - <fmt:formatDate value="${conflict.endDate}" pattern="MMM d, yyyy h:mma"/>)</li><br>

 </c:forEach></ul></td></tr></table>
<hr>--%>


