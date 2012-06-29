<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>

<x:permission var="isAuthorized" module="com.tms.sam.po.model.PrePurchaseModule" permission="com.tms.sam.po.ManagePermission"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>

<x:config>
	<page name="permission">
		<com.tms.sam.po.permission.ui.PrintPermission name="print"/>
	</page>
</x:config>

<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body onload="this.focus();window.print();">

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="po.permission.listing"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="permission.print" /></td>
	</tr>
</table>
