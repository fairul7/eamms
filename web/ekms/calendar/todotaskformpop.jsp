<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.taskmanager.ui.TaskForm,
                 kacang.ui.WidgetManager"%>
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
        }
    %>
    <script>
        alert("<fmt:message key='calendar.message.newTaskAddedSuccessfully'/>");
        window.opener.location.reload();
        document.location = "<c:url value="/ekms/calendar/taskresourcepop.jsp?taskId=${taskId}" ></c:url>"
    </script>
</c:if>

<c:if test="${forward.name=='cancel'}" >
    <script>
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='task added'}" >
    <script>
        alert("<fmt:message key='calendar.message.newTaskAddedSuccessfully'/>");
        window.opener.location.reload();
        window.close();
    </script>
</c:if>

<%  String id = request.getParameter("id");
    boolean edit = false;
    if(id!=null&&id.trim().length()>0){
        edit = true;

%>
<x:set name="taskformpage.taskform" property="taskId" value="<%=id%>" />
<%}%>

<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <title><fmt:message key='calendar.label.addNewToDoTask'/></title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">

</head>
<body class="calendarRow">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
    <Tr>
        <td align="center" height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
            <fmt:message key='calendar.label.addNewToDoTask'/>
        </font></b></td>

    </tr>     <Tr><td>
    <x:display name="taskformpage.taskform" >
</td></tr></table></x:display>
</body>
</html>

