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
<c:set var="deleteUrl"><%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%=TaskEventView.EVENT_TYPE_DELETE%></c:set>
<c:if test="${forward.name == 'delete successful'}" >
    <script>
        alert("Task deleted!");
        <c:choose>
            <c:when test="${from == 'task' }" >document.location = '<c:url value="/ekms/taskmanager/taskmanager.jsp" />';</c:when>
            <c:otherwise>document.location = "<%= response.encodeURL(request.getRequestURI()) %>?cn=calendarPage.calendarView&et=view&view=daily";</c:otherwise>
        </c:choose>
    </script>
</c:if>
<c-rt:set var="reassign" value="<%=TaskEventView.FORWARD_REASSIGN%>" />
<c:if test="${forward.name == reassign}">
    <script>window.open("<c:url value="/mekms/taskmanager/taskreassignform.jsp?taskId=${id}" />","reassignForm","height=400,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
</c:if>
<c-rt:set var="set" value="<%=TaskEventView.FORWARD_SET_PROGRESS%>"></c-rt:set>
<c:if test="${forward.name == set}" >
    <script>window.open("<c:url value="/mekms/taskmanager/progressform.jsp?taskId=${taskId}&userId=${userId}" />","reassignForm","height=200,width=300,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
</c:if>
<br>
<table cellpadding="0" cellspacing="1" width="100%">
    <tr><td class="title" align="left"><fmt:message key="taskmanager.label.Title"/> : </td></tr>
    <tr><td class="data" align="left"><c:out value="${task.title}" /></td></tr>
    <tr><td class="title" align="left"><fmt:message key="taskmanager.label.Date"/> : </td></tr>
    <tr><td class="data" align="left"><fmt:formatDate value="${task.startDate}" pattern="${globalDatetimeShort}"/> - <fmt:formatDate value="${task.dueDate}" pattern="${globalDatetimeShort}"/></td></tr>
    <tr><td class="title" align="left"><fmt:message key="taskmanager.label.Classification"/> : </td></tr>
    <tr>
        <td class="data" align="left">
            <c:choose>
                <c:when test="${task.classification == 'pub'}">Public</c:when>
                <c:when test="${task.classification == 'pri'}">Private</c:when>
                <c:otherwise>Confidential</c:otherwise>
            </c:choose>
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
        <tr><td class="title" align="left"><fmt:message key="taskmanager.label.description"/> : </td></tr>
        <tr><td class="data" align="left"><c:out value="${translated}" escapeXml="false"/></td></tr>
    </c:if>
    <tr><td class="title" align="left"><fmt:message key="taskmanager.label.CreatedBy"/> : </td></tr>
    <tr><td class="data" align="left"><c:out value="${task.assigner}" /></td></tr>
    <tr><td class="title" align="left"><fmt:message key="taskmanager.label.Assignees"/> : </td></tr>
    <%
        pageContext.setAttribute("not_started",new Integer(Assignee.TASK_STATUS_NOT_STARTED));
        pageContext.setAttribute("progress",new Integer(Assignee.TASK_STATUS_IN_PROGRESS));
        pageContext.setAttribute("completed",new Integer(Assignee.TASK_STATUS_COMPLETED));
     %>
    <tr>
        <td class="data" align="left">
            <c:forEach items="${task.attendees}" var="att" >
                [<c:choose>
                    <c:when test="${att.taskStatus == not_started}">x</c:when>
                    <c:when test="${att.taskStatus == completed}">100%</c:when>
                    <c:otherwise><c:out value="${att.progress}" />%</c:otherwise>
                </c:choose>]
                <c:if test="${view.widgetManager.user.id == att.userId}" ><i></c:if>
                <c:out value="${att.name}" />
                <c:if test="${view.widgetManager.user.id == att.userId}" ></i></c:if>
                <br>
            </c:forEach>
            Overall: <fmt:formatNumber value="${task.overallProgress}"minFractionDigits="0" maxFractionDigits="0" />%
        </td>
    </tr>
    <c:if test="${task.completed}" >
        <tr><td class="title" align="left"><fmt:message key='taskmanager.label.Completed'/> : </td></tr>
        <tr><td class="data" align="left"><fmt:formatDate value="${task.completeDate}" pattern="${globalDatetimeLong}" /></td></tr>
    </c:if>
    <tr><td class="data" align="left">&nbsp;</td></tr>
    <tr>
        <td class="data" align="left">
            <c:if test="${widget.editable}">
               <input type="button" value="Edit" class="button" onClick="document.location = 'edittodotaskform.jsp?id=<c:out value="${task.id}" />'"/>
            </c:if>
            <c:if test="${widget.deletable}">
               <input type="button" class="button" value="Delete" onClick="document.location = '<c:out value="${deleteUrl}" />&taskId=<c:out value="${task.id}" ></c:out>';"/>
            </c:if>
            <%--<c:if test="${!task.completed}">
                <input type="button" value="Progress" class="button" onClick="document.location = 'edittodotaskform.jsp?id=<c:out value="${task.id}" />'"/>
            </c:if>--%>
        </td>
    </tr>
</table>