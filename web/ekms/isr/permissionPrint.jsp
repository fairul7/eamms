<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@page import="com.tms.collab.isr.permission.model.ISRGroup" %>
<%@ include file="/common/header.jsp" %>
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_ACCESS_GROUP_PERMISSION %>" />
<c:set var="aclIsPermissionPage" value="${true}" />
<%@include file="includes/accessControl.jsp" %>
<x:config>
    <page name="permission">
        <com.tms.collab.isr.permission.ui.PermissionTablePrint name="permissionTablePrint"/>
    </page>
</x:config>
<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body onload="this.focus();window.print();">
<table border="0" cellpadding="5" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont"><fmt:message key="isr.label.groupListing"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="permission.permissionTablePrint" /></td>
	</tr>
</table>
</body>
</html>

