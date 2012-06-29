<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
				 com.tms.collab.project.ui.DescriptorForm"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.worms.project.Project.edit" module="com.tms.collab.project.WormsHandler" var="hasPermission"/>
<c:if test="${!hasPermission}">
    <script>window.close();</script>
</c:if>
<x:config>
    <page name="wormsDescriptorOpen">
        <com.tms.collab.project.ui.DescriptorFormOpen name="form"/>
    </page>
</x:config>
<%-- Initializing constants --%>
<c-rt:set var="forward_cancel" value="<%= DescriptorForm.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_success" value="<%= DescriptorForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_failed" value="<%= DescriptorForm.FORWARD_FAILED %>"/>
<c-rt:set var="forward_delete" value="<%= DescriptorForm.FORWARD_DELETE %>"/>
<c:if test="${!empty param.descId}">
	<x:set name="wormsDescriptorOpen.form" property="descId" value="${param.descId}"/>
</c:if>
<%-- Event Handling --%>
<c:if test="${forward_cancel == forward.name}">
	<script>window.close();</script>
</c:if>
<c:if test="${forward_success == forward.name}">
	<script>
		alert("Task Descriptor Updated");
		window.opener.location="<c:url value="/ekms/worms/templateOpen.jsp?projectId=${widgets['wormsDescriptorOpen.form'].projectId}"/>";
		window.close();
	</script>
</c:if>
<c:if test="${forward_delete == forward.name}">
	<script>
		alert("Task Descriptor Deleted");
		window.opener.location="<c:url value="/ekms/worms/templateOpen.jsp?projectId=${widgets['wormsDescriptorOpen.form'].projectId}"/>";
		window.close();
	</script>
</c:if>
<html>
<head>
    <title>Updating Task Descriptor</title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;Updating Task Descriptor</font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsDescriptorOpen.form"/></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
    </table>
</body>
</html>