<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.permission.model.POGroup, com.tms.sam.po.setting.ui.Item"%>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=POGroup.PERM_ACCESS_SETUP %>" />
<%@include file="includes/accessControl.jsp" %>

<c-rt:set var="forwardBack" value="<%= Item.FORWARD_BACK %>" />
<c:if test="${forward.name==forwardBack}">
     <c:redirect url="itemListing.jsp" />
</c:if>
<c:if test="${forward.name eq 'cancel_form_action'}">
     <c:redirect url="itemListing.jsp" />
</c:if>
<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="itemListing.jsp" />
</c:if>

<x:config>
	<page name="setting">
		<com.tms.sam.po.setting.ui.ItemEdit name="EditItem"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="setting.EditItem" property="code" value="${param.id}" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="po.label.editItem"/></td>
	</tr>
	<tr>
		<td><x:display name="setting.EditItem" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>