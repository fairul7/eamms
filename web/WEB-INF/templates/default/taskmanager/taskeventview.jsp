<%@ page import="com.tms.collab.calendar.ui.CalendarView,
                 kacang.ui.Event,
                 com.tms.collab.taskmanager.ui.TaskEventView,
                 java.util.Set,
                 java.util.TreeSet,
                 org.apache.commons.lang.StringUtils,
                 com.tms.collab.taskmanager.model.Assignee,
                 kacang.Application"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="task" value="${widget.task}"/>
<c:set var="view" value="${widget}"/>
<c:set var="deleteUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%=TaskEventView.EVENT_TYPE_DELETE%>
</c:set>

<c:if test="${forward.name == 'delete successful'}" >
    <script>
        alert("Task deleted!");
        <c:choose>
    <c:when test="${from == 'task' }" >
        document.location = '<c:url value="/ekms/taskmanager/taskmanager.jsp" />';
   </c:when>
   <c:otherwise>
        document.location = "<%= response.encodeURL(request.getRequestURI()) %>?cn=calendarPage.calendarView&et=view&view=daily";
</c:otherwise>
</c:choose>
    </script>
</c:if>

<c-rt:set var="reassign" value="<%=TaskEventView.FORWARD_REASSIGN%>" />
<c:if test="${forward.name == reassign}">
    <script>
        window.open("<c:url value="/ekms/taskmanager/taskreassignform.jsp?taskId=${id}" />","reassignForm","height=400,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>

<c-rt:set var="set" value="<%=TaskEventView.FORWARD_SET_PROGRESS%>"></c-rt:set>
<c:if test="${forward.name == set}" >
    <script>
        window.open("<c:url value="/ekms/taskmanager/progressform.jsp?taskId=${taskId}&userId=${userId}" />","reassignForm","height=200,width=300,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>

<c-rt:set var="start" value="<%=TaskEventView.FORWARD_START_TASK%>"></c-rt:set>
<c:if test="${forward.name == start}">
    <script type="">
        window.open("<c:url value="/ekms/taskmanager/setDateForm.jsp?taskId=${taskId}"/>","startTaskForm","height=300,width=400,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>

<c-rt:set var="completion" value="<%=TaskEventView.FORWARD_TASK_COMPLETION%>"/>
<c:if test="${forward.name == completion}">
    <script type="">
        window.open("<c:url value="/ekms/taskmanager/setDateForm.jsp?taskId=${taskId}&taskEventType=completed"/>","startTaskForm","height=300,width=400,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>





        <%-- table  cellpadding="4" cellspacing="1" class="forumBackground" width="100%" --%>
        
        <table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td>
			<table width="100%" cellpadding="3" cellspacing="1">
        
        
        
        
        
        
   <tr>
    <td class="classRowLabel" align="right" width="30%" ><fmt:message key='taskmanager.label.Title'/></td><td class="classRow" align="left"><c:out value="${task.title}" />
</tr>
      <tr>
    <td class="classRowLabel" align="right" width="30%" ><fmt:message key='taskmanager.label.ScheduleStartDate'/></td><td class="classRow" align="left"><fmt:formatDate value="${task.startDate}" pattern="${globalDateLong}"/>
</tr>
    <tr>
         <td class="classRowLabel" align="right" ><fmt:message key='taskmanager.label.ScheduleStartTime'/></td><td class="classRow" align="left"> <fmt:formatDate value="${task.startDate}" pattern="${globalTimeLong}" />
  </tr>
      <tr>
    <td class="classRowLabel" align="right" width="30%" ><fmt:message key='taskmanager.label.ScheduleDueDate'/></td><td class="classRow" align="left"><fmt:formatDate value="${task.dueDate}" pattern="${globalDateLong}"/>
</tr>
    <tr>
         <td class="classRowLabel" align="right" ><fmt:message key='taskmanager.label.ScheduleDueTime'/></td><td class="classRow" align="left"> <fmt:formatDate value="${task.dueDate}" pattern="${globalTimeLong}" />
  </tr>
  <tr>
         <td class="classRowLabel" align="right" ><fmt:message key='taskmanager.label.estimation'/></td>
         <td class="classRow" align="left"> <c:out value="${task.estimation}" /> <c:out value="${task.estimationType}" />
  </tr>
    <tr>
         <td class="classRowLabel" align="right" ><fmt:message key='taskmanager.label.Classification'/></td><td class="classRow" align="left">
           <c:set var="cla" value="${task.classification}"/>
           <c:if test="${cla == 'pub'}" ><fmt:message key='taskmanager.label.Public'/></c:if>
           <c:if test="${cla == 'pri'}" ><fmt:message key='taskmanager.label.Private'/></c:if>
           <c:if test="${cla == 'con'}" ><fmt:message key='taskmanager.label.Confidential'/></c:if>
    </td>
  </tr>

            <c:set var="des" value="${task.description}"/>
            <c:if test="${!empty des}">
                <%
                    String translated = "";
                    translated = StringUtils.replace((String)pageContext.getAttribute("des"), "\n", "<br>");
                    translated = StringUtils.replace(translated, "\r", "<br>");
                    pageContext.setAttribute("translated", translated);
                %>
                <tr>
                    <td class="classRowLabel" align="right" valign="top"><fmt:message key='taskmanager.label.Description'/></td>
                    <td class="classRow" align="left">
                        <c:out value="${translated}" escapeXml="false"/><br>
                    </td>
                </tr>
            </c:if>

       <tr>
            <td class="classRowLabel" align="right" ><fmt:message key='taskmanager.label.Category'/></td>
            <td class="classRow" align="left">
            <c:out value="${task.category}" />
            </td>
         </tr>

       <!-- add for task priority -->
       <c:if test="${! empty task.taskPriority}">
       <tr>
            <td class="classRowLabel" align="right" ><fmt:message key='taskmanager.label.priority'/></td>
            <td class="classRow" align="left">
            <c:out value="${task.priorityDescription}" />
            </td>
       </tr>
       </c:if>

       <tr>
            <td class="classRowLabel" align="right" ><fmt:message key='taskmanager.label.CreatedBy'/></td>
            <td class="classRow" align="left">
             <c:out value="${task.assigner}"/></td>
         </tr>

       <tr>
            <td class="classRowLabel" align="right" valign="top"><fmt:message key='taskmanager.label.Assignees'/></td>
             <%
                pageContext.setAttribute("not_started",new Integer(Assignee.TASK_STATUS_NOT_STARTED));
                pageContext.setAttribute("progress",new Integer(Assignee.TASK_STATUS_IN_PROGRESS));
                pageContext.setAttribute("completed",new Integer(Assignee.TASK_STATUS_COMPLETED));
             %>
            <td class="classRow" align="left">
               <table width="100%">
                   <tr>
                       <td width="20%">&nbsp;</td>
                       <td width="20%"><fmt:message key='taskmanager.label.Started'/></td>
                       <td>&nbsp;</td>
                       <td width="20%"><fmt:message key='taskmanager.label.Completed'/></td>
                       <td>&nbsp;</td>
                       <%-- <td></td> --%>
                   </tr>
               <c:forEach items="${task.attendees}" var="att" >
                <tr>
                    <TD width="20%" valign="top">
                           <c:if test="${view.widgetManager.user.id == att.userId}" >
                <b>
                </c:if>
                      <c:out value="${att.name}" />&nbsp;&nbsp;&nbsp;&nbsp;
                           <c:if test="${view.widgetManager.user.id == att.userId}" >
                </b>
                </c:if>

                    </TD>
                    <TD valign="top">
                    <c:choose>
                        <c:when test="${att.taskStatus == not_started}">
                            <fmt:message key='taskmanager.label.NotStarted'/>
                            </td><td>&nbsp;</td><td>&nbsp;</td><td>
                            <!-- if assigner, can change the completion date for assignee -->
                            <c:if test="${widget.editable}">
                                <input type="button" name="edit" value="<fmt:message key='taskmanager.label.editDate'/>" class="button" onclick="window.open('/ekms/taskmanager/adSetDateForm.jsp?taskId=<c:out value="${task.id}" />&userId=<c:out value="${att.userId}"/>','changeCompletionDateForm','height=300,width=400,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes')">
                            </c:if>
                        </c:when>
                        <c:when test="${att.taskStatus == completed}">
                            <fmt:formatDate pattern="${globalDatetimeLong}" value="${att.startDate}" /></td>
                            <td>&nbsp;</td>
                            <td valign="top" ><fmt:formatDate pattern="${globalDatetimeLong}" value="${att.completeDate}" /></td>
                            <td valign="top">
                            <!-- if assigner, can change the completion date for assignee -->
                            <c:if test="${widget.editable}">
                                <input type="button" name="edit" value="Edit Date" class="button" onclick="window.open('/ekms/taskmanager/adSetDateForm.jsp?taskId=<c:out value="${task.id}" />&userId=<c:out value="${att.userId}"/>','changeCompletionDateForm','height=300,width=400,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes')">
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <fmt:formatDate pattern="${globalDatetimeLong}" value="${att.startDate}" /></td>
                            <td valign="top" width="26%">
                            <table>
                            <tr>
                            <td width="<fmt:formatNumber value="${att.progress}"minFractionDigits="0" maxFractionDigits="0" />%">
                              <table bgcolor="red" border="0" cellpadding="0" cellspacing="0" width="100%" class="style{border:1px}">
                                  <TR><TD align="right" height="15"></TD>
                                  </TR>
                                </table><%--%--%>
                              </td><td><c:out value="${att.progress}" />%</td> <%-- <td>(from <fmt:formatDate value="${att.startDate}" pattern="d, MMM yyyy"/>)</td>--%>
                              </tr>
                            </table>
                            </td>
                            <td>&nbsp;</td>
                            <td valign="top">
                            <!-- if assigner, can change the completion date for assignee -->
                            <c:if test="${widget.editable}">
                                <input type="button" name="edit" value="Edit Date" class="button" onclick="window.open('/ekms/taskmanager/adSetDateForm.jsp?taskId=<c:out value="${task.id}" />&userId=<c:out value="${att.userId}"/>','changeCompletionDateForm','height=300,width=400,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes')">
                            </c:if>
                    </c:otherwise>
                    </c:choose>
                    <%--</TD>
                    <TD valign="top">--%>
                        <c:if test="${att.userId == widget.widgetManager.user.id}" >
                    <c:choose>
<%--
                        <c:when test="${att.taskStatus == not_started}">
                            &nbsp;&nbsp;<INPUT type="button" class="button" value="Start" />
                        </c:when>
--%>
                        <c:when test="${att.taskStatus == progress}">
                                        <c:set var="setUrl">
                <%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%=TaskEventView.EVENT_TYPE_SET%>&taskId=<c:out value="${task.id}" />&userId=<c:out value="${att.userId}" ></c:out>
                </c:set>

                           <br><INPUT type="button" class="button" value="<fmt:message key='taskmanager.label.setProgress'/>" onClick="document.location = '<c:out value="${setUrl}" ></c:out>'" />
                        </c:when>
<%--
                        <c:otherwise>
                        </c:otherwise>
--%>
                    </c:choose>

                        </c:if>
                    </TD>
                </tr>
                </c:forEach>
               </table>

<%--
                <%int i=0;%>
                <b>  </b>
                <%if(i>0){   %>
                ,
                <%}%>
                <c:if test="${view.widgetManager.user.id == att.userId}" >
                <b>
                </c:if>



                <c:if test="${view.widgetManager.user.id == att.userId}" >
                </b>
                </c:if>
                <% i++; %>
--%>
<%--
                </c:forEach>
--%>
            </td>
         </tr>

     <tr>
        <td class="classRowLabel" align="right"><fmt:message key='taskmanager.label.OverallProgress'/></td>
        <td class="classRow">
           <fmt:formatNumber value="${task.overallProgress}"minFractionDigits="0" maxFractionDigits="0" />%
        </td>
     </tr>


    <c:if test="${task.reassignments!=null}" >
   <%
       Set assignees = new TreeSet();
       pageContext.setAttribute("ReassigneeNames",assignees);
     %>
    <c:forEach items="${task.reassignments}"  var="reassignment" >
    <c:choose>

            <c:when test="${reassignment.assignerId == widget.userId}">
                <c:set value="${reassignment.assigneeName}" var="assigneeName"/>
                <%
                    assignees.add(pageContext.getAttribute("assigneeName"));
                %>
                <%pageContext.setAttribute("to",Boolean.TRUE);%>
            </c:when>
            <c:when test="${reassignment.assigneeId == widget.userId}" >
                <%pageContext.setAttribute("to",Boolean.FALSE);%>
                <c:set value="${reassignment.assignerName}" var="reassigner" />
            </c:when>
        </c:choose>
    </c:forEach>
            <c:if test="${to}">
    <tr valign="top">
       <td class="classRowLabel" align="right" align="top"><fmt:message key='taskmanager.label.ReassignedTo'/></td>  <td class="classRow">
            <%int i=0;%>
            <c:forEach items="${ReassigneeNames}" var="assigneeName" >
             <%if(i>0){   %>
                ,
             <%}%>
                <c:out value="${assigneeName}" /> <% i++; %>
            </c:forEach>
            <%if (i==0){%>
                -
            <%}%>
       </td>
            </c:if>
            <c:if test="${!to&&reassigner!=null}">
      <tr valign="top">
       <td class="classRowLabel" align="right" align="top"><fmt:message key='taskmanager.label.ReassignedBy'/></td>  <td class="classRow" align="left"> <c:out value="${reassigner}"/></td>
            </c:if>


    </tr>
    </c:if>

    <!-- add comments -->
       <tr>
      <td class="classRowLabel"align="right"valign="top"><fmt:message key='taskmanager.label.comments'/></td>
      <td class="classRow"cellspacing="0" cellpadding="0" valign="top">
        <c:out value="${task.comments}" escapeXml="false"/><br>
          <input type="button" name="comment" value="<fmt:message key='taskmanager.label.addComments'/>" class="button" onclick="window.open('/ekms/taskmanager/commentsForm.jsp?taskId=<c:out value="${task.id}"/>','taskCommentForm','height=300,width=400,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes')">
      </td>
   </tr>


         <tr>
        <td class="classRowLabel"align="right"valign="top"><fmt:message key='taskmanager.label.AttachedFiles'/></td>
        <td class="classRow"cellspacing="0" cellpadding="0" valign="top">
        <x:display name="${view.fileListing.absoluteName}" />
        </td>
     </tr>


     <c:if test="${!empty task.resources}" >
    <tr>
        <td class="classRowLabel"align="right" valign="top"><fmt:message key='taskmanager.label.Resources'/></td>
        <td  class="classRow">
        <%int i=0;%>
            <c:forEach items="${task.resources}" var="resource" >
             <%if(i>0){   %>
                ,
             <%}%>
                <c:out value="${resource.name}" /> (<c:out value="${resource.status}" />)<% i++; %>
            </c:forEach>
            <%if (i==0){%>
                -
            <%}%>
        </td>
     </tr>
    </c:if>

     <c:if test="${task.completed}" >
<tr>
    <td class="classRowLabel"align="right"><fmt:message key='taskmanager.label.Completed'/></td>
    <td  class="classRow">
       <fmt:formatDate value="${task.completeDate}" pattern="${globalDatetimeLong}" />
    </td>
</tr>

</c:if>

    <c:if test="${task.lastModified!=null}" >
    <tr>
        <td class="classRowLabel"align="right"><fmt:message key='taskmanager.label.Lastmodified'/></td><td class="classRow"><fmt:formatDate pattern="${globalDatetimeLong}" value="${task.lastModified}" /> <fmt:message key='taskmanager.label.by'/> <c:out value="${task.lastModifiedName}"/>
        </td>
    </tr>
    </c:if>

    <%--task.reassign && widget.widgetManager.user.id == task.assignerId &&!task.completed--%>
<c:if test="${!widget.parent.hiddenAction}" >
    <Tr>
    <td class="classRow"> &nbsp</td>
        <td  class="classRow" align="left">
          <c:if test="${!task.completed}" >
           <%-- <c:choose>
                <c:when test="${widget.userId== task.assignerId}" >
                     <%pageContext.setAttribute("canComplete",Boolean.TRUE);%>
                </c:when>
                <c:otherwise>--%>
                     <c:forEach items="${task.attendees}" var="att"  >
                        <c:if test="${widget.userId == att.userId}" >
                             <c:if test="${att.taskStatus == progress}" >
                                <%pageContext.setAttribute("canComplete",Boolean.TRUE);%>
                                <%pageContext.setAttribute("canStart",Boolean.FALSE);%>
                             </c:if>
                             <c:if test="${att.taskStatus == not_started}" >
                                <%pageContext.setAttribute("canComplete",Boolean.FALSE);%>
                                <%pageContext.setAttribute("canStart",Boolean.TRUE);%>
                            </c:if>
                        </c:if>
                    </c:forEach>
                <%--</c:otherwise>
            </c:choose>--%>
          </c:if>
          <c:choose>
          <c:when test="${canComplete}" >
                <c:set var="completeUrl">
                <%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%=TaskEventView.EVENT_TYPE_COMPLETE%>
                </c:set>
                <input type="button" class="button" value="<fmt:message key="taskmanager.label.completed"/>" onClick="document.location = '<c:out value="${completeUrl}" />'"/>
          </c:when>
          <c:when test="${canStart}" >
                <c:set var="startUrl">
                <%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%=TaskEventView.EVENT_TYPE_START%>&taskId=<c:out value="${task.id}" />
                </c:set>
                <input type="button" class="button" value="<fmt:message key="taskmanager.label.start"/>" onClick="document.location = '<c:out value="${startUrl}" />'"/>
          </c:when>
        </c:choose>
            <c:if test="${widget.reassignable}">
                <c:set var="reassignUrl">
                <%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%=TaskEventView.EVENT_TYPE_REASSIGN%>
                </c:set>
                <input type="button" class="button" value="<fmt:message key="taskmanager.label.reassign"/>" onClick="document.location = '<c:out value="${reassignUrl}" />'"/>
           </c:if>
           <c:if test="${widget.editable}" >
           <c:if test="${!widget.completed && task.isproject=='No'}">      
           <input value="<fmt:message key="taskmanager.label.edit"/>" type="button" class="button" onClick="document.location = 'edittodotaskform.jsp?id=<c:out value="${task.id}" />'"/>
           </c:if>
           <c:if test="${!widget.completed && task.isproject=='Yes'}">           
           <input value="<fmt:message key="taskmanager.label.edit"/>" type="button" class="button" onClick="window.open('/ekms/worms/task.jsp?taskId=<c:out value="${task.id}" />', 'taskWindow', 'height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes')"/>
           </c:if>
           
        </c:if>
        <c:if test="${widget.deletable}" >
           <INPUT type="button" class="button" value="<fmt:message key="taskmanager.label.delete"/>" onClick="if(confirm('Click OK to confirm')){document.location = '<c:out value="${deleteUrl}" />&taskId=<c:out value="${task.id}" ></c:out>'}"/>

<%--
           | </c:if><x:event name="${widget.absoluteName}" type="delete" param="taskId=${task.id}"><fmt:message key='taskmanager.label.Delete'/></x:event >
--%>
        </c:if>

            <!-- add in on 04252005 - button for popup timesheet form -->
            <%
                String s = Application.getInstance().getProperty("com.tms.collab.timesheet");
                boolean isTimeSheet = false;
                if (s!=null && s.equals("true")) {
                    isTimeSheet = true;
                }
                if (isTimeSheet) {
            %>
            <c:forEach items="${task.attendees}" var="att">
            <c:if test="${att.userId == widget.widgetManager.user.id}">
                <input type="button" class="button" value="<fmt:message key="timesheet.label.set"/>" onClick="document.location='../timesheet/TimeSheetForm.jsp?taskid=<c:out value="${task.id}"/>';" />
                <input type="button" class="button" value="<fmt:message key="timesheet.label.timesheet"/>" onClick="document.location='../timesheet/TimeSheetViewDet.jsp?taskid=<c:out value='${ task.id }'/>&projectid=<c:out value='${ task.categoryId }'/>'"/>
            </c:if>
            </c:forEach>
            <% } %>
          </td>
    </tr>
    </c:if>




			</table>
			
		</td>
	</tr>
</table>

</td>
</tr>	
</table>




       <%-- /table --%>
     <%-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>--%>


