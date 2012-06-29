<%@ page import="kacang.ui.Event,
                 com.tms.collab.taskmanager.ui.TaskListing,
                 com.tms.collab.calendar.ui.CalendarView,
                 com.tms.collab.calendar.model.CalendarEvent,
                 kacang.services.security.SecurityService,
                 kacang.Application,
                 com.tms.collab.calendar.model.CalendarModule,
                 com.tms.collab.taskmanager.model.TaskManager,
                 com.tms.collab.taskmanager.model.Task,
                 com.tms.collab.taskmanager.model.Assignee"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="task" value="${widget.task}" />
<c-rt:set var="cn" value="<%= Event.PARAMETER_KEY_WIDGET_NAME %>"/>
    <c-rt:set var="et" value="<%= Event.PARAMETER_KEY_EVENT_TYPE %>"/>
    <c-rt:set var="eid" value="<%= CalendarView.PARAMETER_KEY_EVENTID %>"/>
    <c-rt:set var="iid" value="<%= CalendarView.PARAMETER_KEY_INSTANCEID %>"/>
<%--<table cellpadding="0" border="0" cellspacing = "0">
<tr>
    <td>--%>
        <b> <c:if test="${task.overdue}" ><U><fmt:message key='taskmanager.label.Overdue'/></U></c:if><fmt:message key='taskmanager.label.Deadline'/></b>
        <FONT COLOR="#FF0000"><fmt:formatDate value="${task.dueDate}" pattern="${globalDatetimeLong}"/> </FONT>|  <FONT SIZE="2" CLASS="calendarContentFont"><B><fmt:message key='taskmanager.label.Category'/>: </b></font><c:out value="${task.category}"  /><BR>

      <c:choose>
      <c:when test="${!task.hidden}" >
        <SPAN CLASS="textFont"><FONT COLOR="#0065CE">
        <c:url var="eUrl" value="${eventUrl}">
        <c:param name="${cn}" value="${widget.view.absoluteName}"/>
        <c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>
        <c:param name="${eid}" value="${task.eventId}"/>
        <c:param name="${iid}" value="${task.recurrenceId}"/>
    </c:url>
        <a href="<c:out value="${eUrl}" ></c:out>"><c:out value="${task.title}"/> <c:if test="${task.reminder}">(<fmt:message key='taskmanager.label.REMINDER'/>)</c:if> </a></FONT></SPAN>
         <c:set scope="page" value="${widget.widgetManager.user.id}" var="userId"/>
        <%
                        String userId = (String)pageContext.getAttribute("userId");
                        Task task = (Task)pageContext.getAttribute("task");
                        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                if(task.getAssignerId().equals(userId)||ss.hasPermission(userId,TaskManager.PERMISSION_MANAGETASK,null,null) ){

                    pageContext.setAttribute("canEdit", Boolean.TRUE);
                }
       %>
        <c:if test="${widget.completeable}" >
            <c-rt:set var="canComplete" value="<%=Boolean.TRUE%>"  />
        </c:if>
        <c:if test="${widget.reassignable}" >
            <c-rt:set var="canReassign" value="<%=Boolean.TRUE%>"  />
        </c:if>
        <c:if test="${canEdit||canComplete||canReassign}" >
            <br>
        </c:if>


        <c:if test="${canEdit}" >

<%--
           <input value="<fmt:message key="calendar.label.buttonEdit"/>" type="button" class="button" onClick="document.location = 'edittodotaskform.jsp?id=<c:out value="${task.eventId}" />'"/>
          <c:url var="deleteUrl" value="${eventUrl}">
            <c:param name="${cn}" value="${widget.absoluteName}"/>
            <c:param name="${et}" value="delete"/>
            <c:param name="taskId" value="${task.eventId}"/>
        </c:url>
--%>

<%--
           <input value="<fmt:message key="calendar.label.buttonDelete"/>" type="button" class="button" onClick="if(confirm('Are you sure you want to delete this task?')){document.location='<c:out value="${deleteUrl}"/>';}"/>
--%>

        </c:if>

        <%
            pageContext.setAttribute("notstarted",new Integer(Assignee.TASK_STATUS_NOT_STARTED));

        %>
        <c:if test="${widget.thisAssignee.taskStatus == notstarted}" >
          <c:url var="startUrl" value="${eventUrl}">
                <c:param name="${cn}" value="${widget.absoluteName}"/>
                <c:param name="${et}" value="start"/>
                <c:param name="taskId" value="${task.eventId}"/>
          </c:url>
        <INPUT TYPE="button" value="<fmt:message key="calendar.label.buttonStart"/>" class="button" onClick="document.location = '<c:out value="${startUrl}"/>';"/>
        </c:if>


        <c:if test="${canComplete}" >
          <c:url var="completeUrl" value="${eventUrl}">
        <c:param name="${cn}" value="${widget.absoluteName}"/>
        <c:param name="${et}" value="complete"/>
        <c:param name="taskId" value="${task.eventId}"/>
    </c:url>
        <INPUT TYPE="button" value="<fmt:message key="calendar.label.buttonCompleted"/>" class="button" onClick="document.location = '<c:out value="${completeUrl}"/>';"/>
        </c:if>
<%--
        <c:if test="${canReassign}" >
            <INPUT TYPE="BUTTON" CLASS="button" value="<fmt:message key="calendar.label.buttonReassign"/>" onClick="window.open('<c:url value="/ekms/taskmanager/taskreassignform.jsp?taskId=${task.id}" />','reassignForm','height=400,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');" />
        </c:if>
--%>

        <br><B><FONT COLOR="#9C9A9C">. . . . . . . . . . . . . . . . . . . . . .</FONT></B><BR>
        <FONT SIZE="2" CLASS="calendarContentFont"><B><fmt:message key='taskmanager.label.Assignedby'/></B> <c:out value="${task.assigner}" />
        <BR>
        <B><fmt:message key='taskmanager.label.Assignedto'/></B>
        <c:forEach items="${task.attendees}" var="attendee" begin="0" varStatus="status"  >
                          <c:if test="${status.index>0}" >,</c:if>
                            <c:out value="${attendee.name}" />
                        </c:forEach>
        <BR>
        <c:if test="${widget.fileListing.moreFiles}" ><B><fmt:message key='taskmanager.label.Attachments'/></B>
            <c:forEach items="${widget.fileListing.files}" var="file" begin="0" varStatus="status" >
              <c:if test="${status.index>0}" >,</c:if>
              <a href="<c:out value='${pageContext.request.contextPath}'/>/storage/<c:out value="${widget.fileListing.folderId}" />/<c:out value="${file.name}" />">
              <IMG SRC="../images/note.gif" WIDTH="13" HEIGHT="16" ALIGN="ABSMIDDLE" BORDER="0" ALT="File Attachement">
               <c:out value="${file.name}" />
              </a>
           </c:forEach>
<br>

        </c:if> </c:when>
        <c:otherwise><fmt:message key='taskmanager.label.PrivateTask'/></c:otherwise>
        </c:choose>
        </FONT>


    <%--</td>
</tr>
</table>--%>
