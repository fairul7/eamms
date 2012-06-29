<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.project.ui.RoleForm"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.worms.project.Project.edit" module="com.tms.collab.project.WormsHandler" var="hasPermission"/>
<c:if test="${!hasPermission}">
    <script>window.close();</script>
</c:if>
<x:config>
    <page name="wormsRoleOpen">
        <com.tms.collab.project.ui.RoleFormOpen name="form"/>
    </page>
</x:config>
<c-rt:set var="forward_success" value="<%= RoleForm.FORWARD_SUCCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= RoleForm.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_failed" value="<%= RoleForm.FORWARD_FAILED %>"/>
<c:if test="${!empty param.currentlyTemplate}">
    <x:set name="wormsRoleOpen.form" property="currentTemplate" value="${param.currentlyTemplate}"/>
</c:if>
<c:if test="${!empty param.roleId}">
    <x:set name="wormsRoleOpen.form" property="roleId" value="${param.roleId}"/>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>
        alert("<fmt:message key='project.message.roleUpdated'/>");
        <c:choose>
            <c:when test="${widgets['wormsRoleOpen.form'].currentlyTemplate}">
                window.opener.location="<c:url value="/ekms/worms/templateOpen.jsp"/>?templateId=<c:out value="${widgets['wormsRoleOpen.form'].projectId}"/>";
            </c:when>
            <c:otherwise>
                window.opener.location="<c:url value="/ekms/worms/projectOpen.jsp"/>?projectId=<c:out value="${widgets['wormsRoleOpen.form'].projectId}"/>";
            </c:otherwise>
        </c:choose>
        window.close();
    </script>
</c:if>
<c:if test="${forward.name == forward_cancel}">
    <script>window.close();</script>
</c:if>
<html>
<head>
    <title><fmt:message key='project.label.updatingARole'/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.updatingARole'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsRoleOpen.form"/></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
    </table>
</body>
</html>
