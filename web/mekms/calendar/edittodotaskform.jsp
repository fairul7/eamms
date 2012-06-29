<%@ page import="com.tms.collab.taskmanager.ui.TaskForm,
   kacang.ui.WidgetManager"%>
<%@include file="/common/header.jsp"%>
<x:config reloadable="${param.reload}">
    <page name="edittaskformpage">
        <com.tms.collab.taskmanager.ui.MTaskForm name="taskform"/>
    </page>
</x:config>
<c-rt:set var="addresource" value="<%=TaskForm.FORWARD_ADD_RESOURCES%>" ></c-rt:set>
<c:if test="${forward.name == addresource}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        TaskForm form;
        if(wm!=null){
            form = (TaskForm)wm.getWidget("edittaskformpage.taskform");
            if(form!=null)
                pageContext.setAttribute("taskId",form.getTask().getId());
        }
    %>
    <c:redirect url="/mekms/taskmanager/taskresource.jsp?taskId=${taskId}"/>
</c:if>
<c:if test="${forward.name =='assignees'}">
    <c:redirect url="/mekms/taskmanager/assignee.jsp?init=edittaskformpage.taskform"/>
</c:if>
<c:if test="${!empty param.reload}">
    <x:set name="edittaskformpage.taskform" property="populated" value="<%= Boolean.TRUE %>"/>
</c:if>
<c:if test="${forward.name=='conflict exception'}">
    <% session.setAttribute("edit",Boolean.TRUE); %>
    <c:redirect url="/mekms/calendar/conflicts.jsp"/>
</c:if>
<c:if test="${!empty param.id}" >
    <x:set name="edittaskformpage.taskform" property="taskId" value="${param.id}" ></x:set>
</c:if>
<c:if test="${forward.name=='cancel'}" >
    <c:redirect url="/mekms/calendar/calendar.jsp" ></c:redirect>
</c:if>
<c:if test="${forward.name=='task updated'}" >
    <c:set var="taskId" value="${widgets['edittaskformpage.taskform'].taskId}"/>
    <script>
        alert("<fmt:message key='calendar.message.taskUpdatedSuccessfully'/>");
        document.location = "<c:url value="/mekms/calendar/calendar.jsp" />?cn=calendarPage.calendarView&et=select&eventId=<c:out value="${taskId}"/>&instanceId=0";
    </script>
</c:if>
<jsp:include page="../includes/mheader.jsp"/>
<table width="100%" border=0 cellpadding=2 cellspacing=0>
    <tbody><tr valign="top"><td><x:display name="edittaskformpage.taskform"/></td></tr></tbody>
</table>
<jsp:include page="../includes/mfooter.jsp" />
