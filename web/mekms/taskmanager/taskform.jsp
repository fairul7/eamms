<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.taskmanager.ui.TaskForm,
                 kacang.Application"%>
<%@include file="/common/header.jsp"%>
<x:config reloadable="${param.reload}">
    <page name="taskformpage">
         <com.tms.collab.taskmanager.ui.MTaskForm name="taskform"/>
    </page>
</x:config>
<c:if test="${forward.name=='task added'}">
    <script>
         alert("<fmt:message key='calendar.message.newTaskAddedSuccessfully'/>");
         document.location = "<c:url value="/mekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=${widgets['taskformpage.taskform'].task.eventId}" />";
    </script>
</c:if>
<c:if test="${forward.name =='assignees'}">
    <c:redirect url="/mekms/taskmanager/assignee.jsp?init=taskformpage.taskform"/>
</c:if>
<c:if test="${forward.name=='conflict exception'}">
    <% session.setAttribute("edit",Boolean.FALSE);%>
    <c:redirect url="/mekms/calendar/conflicts.jsp"/>
</c:if>
<c:if test="${forward.name == 'add Resources'}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        TaskForm form;
        if(wm!=null){
            form = (TaskForm)wm.getWidget("taskformpage.taskform");
            if(form!=null)
                pageContext.setAttribute("taskId",form.getTask().getId());
        }
    %>
    <c:redirect url="/mekms/taskmanager/taskresource.jsp?taskId=${taskId}"/>
</c:if>
<c:if test="${forward.name=='cancel'}">
    <c:redirect url="/mekms/calendar/calendar2.jsp?defaultDay=today"/>
</c:if>
<%  String id = request.getParameter("id");
    boolean edit = false;
    if(id!=null&&id.trim().length()>0){
        edit = true;
%>
    <x:set name="taskformpage.taskform" property="taskId" value="<%=id%>" />
<%}
    request.setAttribute("headerCaption", ((edit) ? Application.getInstance().getMessage("taskmanager.label.editingToDoTask") : Application.getInstance().getMessage("taskmanager.label.addingANewToDoTask")));
%>
<jsp:include page="../includes/mheader.jsp"/>
<table width="100%" border=0 cellPadding=2 cellSpacing=0>
    <tr valign="top"><td><x:display name="taskformpage.taskform"/></td></tr>
</table>
<jsp:include page="../includes/mfooter.jsp" />