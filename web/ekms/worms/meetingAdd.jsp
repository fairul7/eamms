<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
				 com.tms.collab.project.ui.ProjectMeetingFormAdd"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.worms.project.Project.edit" module="com.tms.collab.project.WormsHandler" var="hasPermission"/>
<c:if test="${!hasPermission}">
    <script>window.close();</script>
</c:if>
<x:config>
	<page name="wormsMeetingAdd">
		<com.tms.collab.project.ui.ProjectMeetingFormAdd name="form"/>
	</page>
</x:config>
<c-rt:set var="forward_success" value="<%= ProjectMeetingFormAdd.FORWARD_SUCCESSFUL %>"/>  
<c:if test="${!empty param.projectId}">
	<x:set name="wormsMeetingAdd.form" property="projectId" value="${param.projectId}"/> 
</c:if>
<c:if test="${forward_success == forward.name}">
	<script>
		alert("<fmt:message key='project.label.meetingScheduled'/>");
		window.opener.location="<c:url value="/ekms/worms/projectSchedule.jsp?projectId=${widgets['wormsMeetingAdd.form'].projectId}"/>";
		window.close();
	</script>
</c:if>
<html>
<head>
    <title><fmt:message key='project.label.schedulingMeeting'/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.schedulingMeeting'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsMeetingAdd.form"/></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
    </table>
</body>
</html>