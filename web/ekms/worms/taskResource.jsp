<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.taskmanager.ui.TaskResourceForm"%>
<%@ include file="/common/header.jsp" %>
<x:config >
    <page name="wormsResource">
        <com.tms.collab.taskmanager.ui.TaskResourceForm name="form"/>
    </page>
</x:config>
<c-rt:set var="forward_added" value="<%= TaskResourceForm.FORWARD_RESOURCES_BOOKED %>"/>
<c-rt:set var="forward_cancel" value="<%= TaskResourceForm.FORWARD_CANCEL %>" />
<c-rt:set var="viewconflict" value="<%= TaskResourceForm.FORWARD_VIEW_CONFLICTS %>"/>
<c:if test="${!empty param.taskId}" >
    <x:set name="wormsResource.form" property="taskId" value="${param.taskId}"/>
</c:if>
<c:if test="${forward_cancel == forward.name}" >
    <c:redirect url="/ekms/worms/task.jsp?taskId=${widgets['wormsResource.form'].taskId}" ></c:redirect>
</c:if>
<c:if test="${forward.name == viewconflict}" >
    <script>
       window.open("<c:url value="/ekms/taskmanager/conflictview.jsp"/>","","resizable=yes,width=450,height=400,scrollbars=yes");
   </script>
</c:if>
<c:if test="${forward_added == forward.name}">
    <script>
        alert("<fmt:message key='project.message.resourcesBooked'/>");
        window.opener.location="<c:url value="/ekms/worms/projectSchedule.jsp"/>";
        window.close();
    </script>
</c:if>

<html>
<head>
    <title><fmt:message key='project.label.bookingResources'/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.bookingResources'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsResource.form"/></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
    </table>
</body>
</html>
