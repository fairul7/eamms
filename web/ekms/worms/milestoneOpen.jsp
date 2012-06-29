<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.project.ui.MilestoneForm"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.worms.project.Project.edit" module="com.tms.collab.project.WormsHandler" var="hasPermission"/>
<c:if test="${!hasPermission}">
    <script>window.close();</script>
</c:if>
<x:config>
    <page name="wormsMilestoneOpen">
        <com.tms.collab.project.ui.MilestoneFormOpen name="form"/>
    </page>
</x:config>
<%-- Event Handling --%>
<c-rt:set var="forward_success" value="<%= MilestoneForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_failed" value="<%= MilestoneForm.FORWARD_FAILED %>"/>
<c-rt:set var="forward_cancel" value="<%= MilestoneForm.FORWARD_CANCEL %>"/>
<c:if test="${!empty param.milestoneId}">
    <x:set name="wormsMilestoneOpen.form" property="milestoneId" value="${param.milestoneId}"/>
</c:if>
<c:if test="${!empty param.currentTemplate}">
    <x:set name="wormsMilestoneOpen.form" property="currentTemplate" value="${param.currentTemplate}"/>
</c:if>
<c:if test="${forward_success == forward.name}">
    <script>
        alert("<fmt:message key='project.message.milestoneUpdated'/>");
        <c:choose>
            <c:when test="${widgets['wormsMilestoneOpen.form'].currentlyTemplate}">
                window.opener.location="<c:url value="/ekms/worms/templateOpen.jsp?templateId=${widgets['wormsMilestoneOpen.form'].projectId}"/>";
            </c:when>
            <c:otherwise>
                window.opener.location="<c:url value="/ekms/worms/projectSchedule.jsp?projectId=${widgets['wormsMilestoneOpen.form'].projectId}"/>";
            </c:otherwise>
        </c:choose>
        window.close();
    </script>
</c:if>
<c:if test="${forward_cancel == forward.name}">
    <script>window.close();</script>
</c:if>
<html>
<head>
    <title><fmt:message key='project.label.updatingAMilestone'/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.updatingAMilestone'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsMilestoneOpen.form"/></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
    </table>
</body>
</html>
