<%@ page import="com.tms.collab.calendar.ui.CalendarView,
                 kacang.ui.Event,
                 com.tms.collab.taskmanager.ui.TaskEventView,
                 java.util.Set,
                 java.util.TreeSet,
                 org.apache.commons.lang.StringUtils,
                 com.tms.collab.taskmanager.model.Assignee"%>
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
        document.location = "<%= response.encodeURL(request.getRequestURI()) %>?cn=calendarPage.calendarView&et=view&view=daily";
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




        <table  cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
   <tr>
    <td class="calendarRowLabel" align="right" width="30%" ><fmt:message key='taskmanager.label.Title'/></td><td class="calendarRow" align="left"><c:out value="${task.title}" />
</tr>
      <tr>
    <td class="calendarRowLabel" align="right" width="30%" ><fmt:message key='taskmanager.label.StartDate'/></td><td class="calendarRow" align="left"><fmt:formatDate value="${task.startDate}" pattern="${globalDateLong}"/>
</tr>
    <tr>
         <td class="calendarRowLabel" align="right" ><fmt:message key='taskmanager.label.StartTime'/></td><td class="calendarRow" align="left"> <fmt:formatDate value="${task.startDate}" pattern="${globalTimeLong}" />
  </tr>
      <tr>
    <td class="calendarRowLabel" align="right" width="30%" ><fmt:message key='taskmanager.label.DueDate'/></td><td class="calendarRow" align="left"><fmt:formatDate value="${task.dueDate}" pattern="${globalDateLong}"/>
</tr>
    <tr>
         <td class="calendarRowLabel" align="right" ><fmt:message key='taskmanager.label.DueTime'/></td><td class="calendarRow" align="left"> <fmt:formatDate value="${task.dueDate}" pattern="${globalTimeLong}" />
  </tr>
    <tr>
         <td class="calendarRowLabel" align="right" ><fmt:message key='taskmanager.label.Classification'/></td><td class="calendarRow" align="left">
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
                    <td class="calendarRowLabel" align="right" valign="top"><fmt:message key='taskmanager.label.Description'/></td>
                    <td class="calendarRow" align="left">
                        <c:out value="${translated}" escapeXml="false"/><br>
                    </td>
                </tr>
            </c:if>

       <tr>
            <td class="calendarRowLabel" align="right" ><fmt:message key='taskmanager.label.Category'/></td>
            <td class="calendarRow" align="left">
            <c:out value="${task.category}" />
            </td>
         </tr>

       <tr>
            <td class="calendarRowLabel" align="right" ><fmt:message key='taskmanager.label.CreatedBy'/></td>
            <td class="calendarRow" align="left">
             <c:out value="${task.assigner}"/></td>
         </tr>

       <tr>
            <td class="calendarRowLabel" align="right" valign="top"><fmt:message key='taskmanager.label.Assignees'/></td>
             <%
                pageContext.setAttribute("not_started",new Integer(Assignee.TASK_STATUS_NOT_STARTED));
                pageContext.setAttribute("progress",new Integer(Assignee.TASK_STATUS_IN_PROGRESS));
                pageContext.setAttribute("completed",new Integer(Assignee.TASK_STATUS_COMPLETED));
             %>
            <td class="calendarRow" align="left">
               <table>
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
                        <c:when test="${att.taskStatus == not_started}"><fmt:message key='taskmanager.label.NotStarted'/></c:when>
                        <c:when test="${att.taskStatus == completed}"><fmt:message key='taskmanager.label.Completed'/>,<fmt:formatDate pattern="${globalDatetimeLong}" value="${att.completeDate}" />
                        </c:when>
                        <c:otherwise>
<%--
                            <table>
                            <tr>
                            <td width="<fmt:formatNumber value="${att.progress}"minFractionDigits="0" maxFractionDigits="0" />%">
                              <table bgcolor="red" border="0" cellpadding="0" cellspacing="0" width="100%" class="style{border:1px}">
                                  <TR><TD align="right" height="15"></TD>
                                  </TR>
                                </table>
                              </td><td><c:out value="${att.progress}" />%</td> <%-- <td>(from <fmt:formatDate value="${att.startDate}" pattern="d, MMM yyyy"/>)</td>
                              </tr>
                            </table>
--%>
                        </c:otherwise>
                    </c:choose>
                    </TD>
                    <TD>
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

<%--
                           <INPUT type="button" class="button" value="Set Progress" onClick="document.location = '<c:out value="${setUrl}" ></c:out>'" />
--%>
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
        <td class="calendarRowLabel" align="right"><fmt:message key='taskmanager.label.OverallProgress'/></td>
        <td class="calendarRow">
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
       <td class="calendarRowLabel" align="right" align="top"><fmt:message key='taskmanager.label.ReassignedTo'/></td>  <td class="CalendarRow">
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
       <td class="calendarRowLabel" align="right" align="top"><fmt:message key='taskmanager.label.ReassignedBy'/></td>  <td class="calendarRow" align="left"> <c:out value="${reassigner}"/></td>
            </c:if>


    </tr>
    </c:if>

         <tr>
        <td class="calendarRowLabel"align="right"valign="top"><fmt:message key='taskmanager.label.AttachedFiles'/></td>
        <td class="calendarRow"cellspacing="0" cellpadding="0" valign="top">
        <x:display name="${view.fileListing.absoluteName}" />
        </td>
     </tr>


     <c:if test="${!empty task.resources}" >
    <tr>
        <td class="calendarRowLabel"align="right" valign="top"><fmt:message key='taskmanager.label.Resources'/></td>
        <td  class="calendarRow">
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
    <td class="calendarRowLabel"align="right"><fmt:message key='taskmanager.label.Completed'/></td>
    <td  class="calendarRow">
       <fmt:formatDate value="${task.completeDate}" pattern="${globalDatetimeLong}" />
    </td>
</tr>

</c:if>

    <c:if test="${task.lastModified!=null}" >
    <tr>
        <td class="calendarRowLabel"align="right"><fmt:message key='taskmanager.label.Lastmodified'/></td><td class="calendarRow"><fmt:formatDate pattern="${globalDatetimeLong}" value="${task.lastModified}" /> <fmt:message key='taskmanager.label.by'/> <c:out value="${task.lastModifiedName}"/>
        </td>
    </tr>
    </c:if>

    <%--task.reassign && widget.widgetManager.user.id == task.assignerId &&!task.completed--%>
<c:if test="${!widget.parent.hiddenAction}" >
    <Tr>
    <td class="calendarRow"> &nbsp</td>
        <td  class="calendarRow" align="left">
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
                <input type="button" class="button" value="Completed" onClick="document.location = '<c:out value="${completeUrl}" />'"/>
          </c:when>
          <c:when test="${canStart}" >
                <c:set var="startUrl">
                <%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%=TaskEventView.EVENT_TYPE_START%>&taskId=<c:out value="${task.id}" />
                </c:set>
                <input type="button" class="button" value="Start" onClick="document.location = '<c:out value="${startUrl}" />'"/>
          </c:when>
        </c:choose>
<%--
            <c:if test="${widget.reassignable}">
                <c:set var="reassignUrl">
                <%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%=TaskEventView.EVENT_TYPE_REASSIGN%>
                </c:set>
                <input type="button" class="button" value="Reassign" onClick="document.location = '<c:out value="${reassignUrl}" />'"/>
           </c:if>
--%>
<%--
           <c:if test="${widget.editable}" >
           <input value="Edit" type="button" class="button" onClick="document.location = 'edittodotaskform.jsp?id=<c:out value="${task.id}" />'"/>
        </c:if>
--%>
<%--
        <c:if test="${widget.deletable}" >
           <INPUT type="button" class="button" value="Delete" onClick="if(confirm('Click OK to confirm')){document.location = '<c:out value="${deleteUrl}" />&taskId=<c:out value="${task.id}" ></c:out>'}"/>
--%>

<%--
           | </c:if><x:event name="${widget.absoluteName}" type="delete" param="taskId=${task.id}"><fmt:message key='taskmanager.label.Delete'/></x:event >

        </c:if>
 --%>         </td>
    </tr>
    </c:if>



       </table>
     <%-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>--%>


