<%@ page import="com.tms.collab.emeeting.ui.MeetingTaskForm,
                 com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.taskmanager.ui.TaskForm,
                 kacang.ui.WidgetManager"%>
<%@include file="/common/header.jsp"%>

<x:config>
    <page name="emeetingTask">
        <com.tms.collab.emeeting.ui.MeetingTaskForm name="taskForm"/>
    </page>
</x:config>

<c:if test="${! empty param.taskId}">
    <c:choose>
        <c:when test="${param.taskId == '-1'}">
            <x:set name="emeetingTask.taskForm" property="taskId" value=""/>
        </c:when>
        <c:otherwise>
            <x:set name="emeetingTask.taskForm" property="taskId" value="${param.taskId}"/>
        </c:otherwise>
    </c:choose>
</c:if>

<c:if test="${! empty param.itemId}">
    <x:set name="emeetingTask.taskForm" property="itemId" value="${param.itemId}"/>
</c:if>
<%-- Event Handling --%>

<c-rt:set var="addresource" value="<%=TaskForm.FORWARD_ADD_RESOURCES%>" ></c-rt:set>
<c:if test="${forward.name == addresource}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        MeetingTaskForm form = null;
        if(wm!=null){
            form = (MeetingTaskForm)wm.getWidget("emeetingTask.taskForm");
            if(form!=null)
                pageContext.setAttribute("taskId",form.getTask().getId());
        }
    %>
    <script>
        <%
            if(form.isEdit()) {
        %>
        alert("<fmt:message key='calendar.message.taskAssignedSuccessfully'/>");
        <%}else{%>
        alert("<fmt:message key='calendar.message.taskUpdatedSuccessfully'/>");

        <%}%>
        //         window.opener.location.reload();
        document.location = "<c:url value="/ekms/calendar/taskresourcepop.jsp?taskId=${taskId}" ></c:url>"
    </script>
</c:if>

<c-rt:set var="forward_add" value="<%= MeetingTaskForm.FORWARD_TASK_ADD %>"/>
<c-rt:set var="forward_update" value="<%= MeetingTaskForm.FORWARD_TASK_UPDATED %>"/>
<c:if test="${forward.name == forward_add}">
    <script>
        alert("<fmt:message key='calendar.label.taskAssigned'/>");
        window.opener.opener.location.reload();
        window.close();
    </script>
</c:if>

<c:if test="${forward.name == forward_update}">
    <script>
        alert("<fmt:message key='calendar.label.taskUpdated'/>");
        window.close();
    </script>
</c:if>

<html>
<head>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">

    <title><fmt:message key='calendar.label.assignAgendaTask'/></title>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td class="emeetingHeader">
            <table cellpadding="4" cellspacing="0" width="100%">
                <tr><td class="emeetingHeader"><fmt:message key='calendar.label.taskAssignment'/></td></tr>
            </table>
        </td>
    </tr>
    <tr><td><x:display name="emeetingTask.taskForm"/></td></tr>
    <tr><td class="emeetingFooter">&nbsp;</td></tr>
</table>
</body>
