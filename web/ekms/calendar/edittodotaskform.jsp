<%@ page import="com.tms.collab.taskmanager.ui.TaskForm,
                 kacang.ui.WidgetManager"%>
<%@include file="/common/header.jsp"%>

<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="edittaskformpage">
        <com.tms.collab.taskmanager.ui.TaskForm name="taskform"/>
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
        }       %>
    <c:redirect url="/ekms/taskmanager/taskresource.jsp?taskId=${taskId}"/>
</c:if>

<c:if test="${forward.name=='conflict exception'}">
    <%    session.setAttribute("edit",Boolean.TRUE);%>
    <c:redirect url="/ekms/calendar/conflicts.jsp" ></c:redirect>
</c:if>

<c:if test="${!empty param.id}" >
    <x:set name="edittaskformpage.taskform" property="taskId" value="${param.id}" ></x:set>
</c:if>

<c:if test="${forward.name=='cancel'}" >
    <c:redirect url="/ekms/calendar/calendar.jsp" ></c:redirect>
</c:if>

<c:if test="${forward.name=='task updated'}" >
    <c:set var="taskId" value="${widgets['edittaskformpage.taskform'].taskId}"/>
    <script>
        alert("<fmt:message key='calendar.message.taskUpdatedSuccessfully'/>");
        document.location = "<c:url value="/ekms/calendar/calendar.jsp" />?cn=calendarPage.calendarView&et=select&eventId=<c:out value="${taskId}"/>&instanceId=0";
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />

<c:set var="mainBody"><x:display name="edittaskformpage.taskform"/></c:set>

<table width="100%" border="0" cellpadding="5" cellspacing="1">
    <tr><td class="calendarHeader"><fmt:message key='calendar.label.editToDoTask'/> : <c:out value="${widgets['edittaskformpage.taskform'].title.value}"/></td></tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr><td align="center"><c:out value="${mainBody}" escapeXml="false"/></td></tr>
</table>

<table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td class="calendarFooter">&nbsp;</td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>

<%--

<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <title><fmt:message key='calendar.label.editAppointment'/></title>
    <link rel="stylesheet" href="<c:url value="/ekms/"/>images/style.css">
</head>
<body class="calendarRow">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
    <Tr>
   <td align="center" height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
       <fmt:message key='calendar.label.editAppointment'/>
      </font></b></td>

</tr>     <Tr><td>
 <x:display name="appointmentformpage.AppointmentForm" >
    </td></tr></table></x:display>
</body>
</html>
--%>

