<%@ page import="kacang.ui.Event,
                 com.tms.collab.taskmanager.ui.TaskListing,
                 com.tms.collab.calendar.ui.CalendarView,
                 kacang.Application"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c-rt:set var="cn" value="<%= Event.PARAMETER_KEY_WIDGET_NAME %>"/>
<c-rt:set var="et" value="<%= Event.PARAMETER_KEY_EVENT_TYPE %>"/>
<c-rt:set var="eid" value="<%= CalendarView.PARAMETER_KEY_EVENTID %>"/>
<c-rt:set var="iid" value="<%= CalendarView.PARAMETER_KEY_INSTANCEID %>"/>
<c:set var="view" value="${widget}"/>
<script language="javascript" src="<%= request.getContextPath() %>/common/tree/tree.js"></script>
<script>
    function viewTask(id,userid)
    {
         window.open('taskmanager/taskview.jsp?id='+id+'&<%=TaskListing.PARAMETER_EVENT_USERID%>='+userid,'viewtask','scrollbars=yes,resizable=yes,width=600,height=450');
    }
</script>
<c:set var="completeUrl">
	<%=response.encodeURL(request.getRequestURI())%>?<%=Event.PARAMETER_KEY_WIDGET_NAME%>=<c:out value="${view.absoluteName}"/>&<%=Event.PARAMETER_KEY_EVENT_TYPE%>=<%=TaskListing.PARAMETER_EVENT_COMPLETE%>
</c:set>

<!-- add for timesheet module May 2005-->
<%
    String s = Application.getInstance().getProperty("com.tms.collab.timesheet");
    boolean isTimeSheet = false;
    if (s!=null && s.equals("true")) {
        isTimeSheet = true;
    }
%>
<table cellpadding="0" cellspacing="0" width="95%" align="center" border="0">
	<jsp:include page="../form_header.jsp" flush="true"/>
	<c:choose>
		<c:when test="${empty view.map}">
			<tr><td class="contentBgColor"><fmt:message key='taskmanager.label.NoToDoTaskFound'/></td></tr>
		</c:when>
		<c:otherwise>
			<c:forEach items="${view.map}" var="item" varStatus="status">
				<tr><td class="contentBgColor"><b><a href="" onClick="treeToggle('task_layer_<c:out value="${status.count}"/>'); return false;"><c:out value="${item.key}"/></a></b></td></tr>
				<tr>
					<td class="contentBgColor">
						<span id="task_layer_<c:out value="${status.count}"/>" style="display: block">
							<script>treeLoad('task_layer_<c:out value="${status.count}"/>');</script>
							<table cellpadding="0" cellspacing="1" width="100%">
								<c:forEach items="${item.value}" var="task">
									<tr>
										<td valign="top">
											<c:if test="${!task.completed}" >
												<a href="<c:out value="${completeUrl}" />&<%=TaskListing.PARAMETER_EVENT_COMPLETE_TASKID%>=<c:out value="${task.id }"/>">
													<img src="images/checkbox.gif" width="14" height="14" align="ABSMIDDLE" alt="Mark Complete" border="0">
												</a>
												<c:if test="${task.assignerId == widgetManager.user.id}">
													<img src="images/note.gif" width="13" height="16" align="ABSMIDDLE" alt="Edit" onClick="javascript:window.open('todotaskform.jsp?id=<c:out value="${task.id}" />','addappointment','scrollbars=yes,resizable=yes,width=450,height=380')">
												</c:if>
											</c:if>
										</td>
										<td>
											<c:url var="eUrl" value="calendar/calendar.jsp">
												<c:param name="${cn}" value="calendarPage.calendarView"/>
												<c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>
												<c:param name="${eid}" value="${task.id}"/>
											</c:url>
											<a href="<c:out value="${eUrl}"  />"><c:out value="${task.title}" /></a>
											</td>
										</tr>
										<tr>
										<td>&nbsp;</td>
										<td>
											<c:choose>
												<c:when test="${task.overdue}" ><font class="taskPortletFont"><b>Overdue</b> <fmt:formatDate value="${task.dueDate}" pattern="${globalDatetimeLong}" /></font></c:when>
												<c:when test="${task.completed}" ><font class="taskPortletFont"><b>Completed</b></font></c:when>
												<c:otherwise><font class="taskPortletFont"><font color="#FF0000"> <fmt:formatDate value="${task.dueDate}" pattern="${globalDatetimeLong}" /></font></font></c:otherwise>
											</c:choose>
										</td>
									</tr>
                                    <!-- add for timesheet module-->
                                    <%
                                        if (isTimeSheet) {
                                    %>
                                    <c:forEach items="${task.attendees}" var="att">
                                    <c:if test="${att.userId == widget.widgetManager.user.id}">
                                    <tr>
                                        <td>&nbsp;</td>
                                        <td><a href="timesheet/TimeSheetForm.jsp?taskid=<c:out value="${task.id}"/>" class="taskPortletFont" ><fmt:message key="timesheet.label.set"/></a><br></td>
                                    </tr>
                                    </c:if>
                                    </c:forEach>
                                    <% } %>
								</c:forEach>
							</table>
						</span>
					</td>
				</tr>
				<tr><td class="contentBgColor">&nbsp;</td></tr>
			</c:forEach> <td>  <tr>
            <td BGCOLOR="#003366" CLASS="portletFooter">&nbsp;
            <input class="button" value="<fmt:message key='calendar.label.newTask'/> " type="button" onClick="javascript:window.open('<c:url value="/ekms/calendar/" />todotaskformpop.jsp?init=1','addtask','scrollbars=yes,resizable=yes,width=600,height=380')"/>
            </td></tr>

        </td>
		</c:otherwise>
	</c:choose>
    <jsp:include page="../form_footer.jsp" flush="true"/>
</table>
<br>

