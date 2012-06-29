<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.emeeting.ui.MeetingForm,
                 com.tms.collab.taskmanager.ui.TaskForm"%>
<%@include file="/common/header.jsp"%>

<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="taskformpage">
        <com.tms.collab.taskmanager.ui.TaskForm name="taskform"/>
    </page>
</x:config>

<c-rt:set var="addresource" value="<%=TaskForm.FORWARD_ADD_RESOURCES%>" ></c-rt:set>

<c:if test="${forward.name == addresource}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        TaskForm form;
        if(wm!=null){
            form = (TaskForm)wm.getWidget("taskformpage.taskform");
            if(form!=null)
                pageContext.setAttribute("taskId",form.getTask().getId());
        }       %>
    <c:redirect url="/ekms/taskmanager/taskresource.jsp?taskId=${taskId}"/>
</c:if>

<c:if test="${forward.name=='cancel'}" >
    <script>
        history.back();
        history.back();
    </script>
</c:if>

<c:if test="${forward.name=='conflict exception'}">
    <%    session.setAttribute("edit",Boolean.FALSE);%>
    <c:redirect url="/ekms/calendar/conflicts.jsp?taskId=" ></c:redirect>
</c:if>

<c:if test="${forward.name=='task added'}" >
    <script>
        alert("<fmt:message key='calendar.message.newTaskAddedSuccessfully'/>");
        <%
               WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
               if(wm!=null){
                   TaskForm form = (TaskForm)wm.getWidget("taskformpage.taskform");
        %>
        document.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>?cn=calendarPage.calendarView&et=select&eventId=<%out.print(form.getTask().getId());}%>";
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellpadding="5" cellspacing="1">
    <tr><td class="calendarHeader"><fmt:message key='calendar.label.taskManager'/> > <fmt:message key='calendar.label.addNewToDoTask'/></td></tr>  </table>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr><td align="center"> <x:display name="taskformpage.taskform" >
    </td></tr></x:display></table>

<table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td class="calendarFooter">&nbsp;</td></tr>  </table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
