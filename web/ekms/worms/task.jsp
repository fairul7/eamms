<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.project.ui.ProjectTaskForm"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.worms.project.Project.edit" module="com.tms.collab.project.WormsHandler" var="hasPermission"/>
<c:if test="${!hasPermission}">
    <script>window.close();</script>
</c:if>
<x:config>
    <page name="wormsTaskAssign">
        <com.tms.collab.project.ui.ProjectTaskForm name="form"/>
    </page>
</x:config>
<%-- Initializing widget --%>
<c:if test="${!empty param.reset}">
    <x:set name="wormsTaskAssign.form" property="reset" value="${param.reset}"/>
</c:if>
<c:if test="${!empty param.projectId}">
    <x:set name="wormsTaskAssign.form" property="projectId" value="${param.projectId}"/>
</c:if>
<c:if test="${!empty param.taskId}">
    <x:set name="wormsTaskAssign.form" property="taskId" value="${param.taskId}"/>
</c:if>
<%-- Event handling --%>
<c-rt:set var="forward_add" value="<%= ProjectTaskForm.FORWARD_TASK_ADD %>"/>
<c-rt:set var="forward_update" value="<%= ProjectTaskForm.FORWARD_TASK_UPDATED %>"/>
<c-rt:set var="forward_delete" value="<%= ProjectTaskForm.FORWARD_TASK_DELETE %>"/>
<c-rt:set var="forward_cancel" value="<%= ProjectTaskForm.FORWARD_TASK_CANCEL %>"/>
<c-rt:set var="forward_add_resources" value="<%= ProjectTaskForm.FORWARD_ADD_RESOURCES %>"/>
<c:if test="${forward_cancel == forward.name}">
    <script>window.close();</script>
</c:if>
<c:if test="${forward_add == forward.name}">
    <script>
        alert("<fmt:message key='project.message.taskAdded'/>");
        window.opener.location="<c:url value="/ekms/worms/projectSchedule.jsp"/>?projectId=<c:out value="${widgets['wormsTaskAssign.form'].projectId}"/>";
    </script>
</c:if>
<c:if test="${forward_update == forward.name}">
    <script>
        alert("<fmt:message key='project.message.taskUpdated'/>");
        window.opener.location="<c:url value="/ekms/worms/projectSchedule.jsp"/>?projectId=<c:out value="${widgets['wormsTaskAssign.form'].projectId}"/>";
        window.close();
    </script>
</c:if>
<c:if test="${forward_delete == forward.name}">
    <script>
        alert("<fmt:message key='project.message.taskDeleted'/>");
        window.opener.location="<c:url value="/ekms/worms/projectSchedule.jsp"/>?projectId=<c:out value="${widgets['wormsTaskAssign.form'].projectId}"/>";
        window.close();
    </script>
</c:if>
<c:if test="${forward_add_resources == forward.name}">
    <c:redirect url="/ekms/worms/taskResource.jsp?taskId=${widgets['wormsTaskAssign.form'].milestoneTaskId}"/>
</c:if>
<html>
<head>
    <title><fmt:message key='project.label.assigningTask'/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.assigningTask'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsTaskAssign.form"/></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
    </table>
</body>
</html>
