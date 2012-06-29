<%@ page import="com.tms.collab.taskmanager.ui.TaskResourceForm,
                 kacang.ui.WidgetManager,
                 com.tms.collab.calendar.ui.ConflictForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>


<x:config >
    <page name="taskresourcepage">
        <com.tms.collab.taskmanager.ui.TaskResourceForm name="taskresource"/>
    </page>
</x:config>

<c:if test="${!empty param.taskId}" >

<x:set name="taskresourcepage.taskresource" property="taskId" value="${param.taskId}" ></x:set>
</c:if>

<c-rt:set var="cancel" value="<%=TaskResourceForm.FORWARD_CANCEL%>" />
<c:if test="${forward.name==cancel}" >
    <script>
     window.close();
     window.opener.location.reload();
    </script>
</c:if>

<c-rt:set var="viewconflict" value="<%=TaskResourceForm.FORWARD_VIEW_CONFLICTS%>" ></c-rt:set>
<c:if test="${forward.name == viewconflict}" >
    <script>
       window.open("conflictview.jsp","","resizable=yes,width=450,height=400,scrollbars=yes");
   </script>
</c:if>

<c-rt:set var="added" value="<%=TaskResourceForm.FORWARD_RESOURCES_BOOKED%>"></c-rt:set>
<c:if test="${forward.name == added}">
    <script>
<%      WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    TaskResourceForm form = (TaskResourceForm)wm.getWidget("taskresourcepage.taskresource");
          %>
        alert("<fmt:message key='resourcemanager.message.resourcesBookedSuccessfully'/>");
        window.close();
    </script>
</c:if>


<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <title><fmt:message key='calendar.label.taskResources'/></title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">

</head> 

<body>
<x:display name="taskresourcepage" />

</body>
</html>
