<%@page import="com.tms.collab.isr.permission.model.ISRGroup,
				com.tms.collab.isr.permission.ui.PermissionForm" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_ACCESS_GROUP_PERMISSION %>" />
<c:set var="aclIsPermissionPage" value="${true}" />
<%@include file="includes/accessControl.jsp" %>

<c-rt:set var="forward_add" value="<%=PermissionForm.FORWARD_SUCCESS%>" />
<c-rt:set var="forward_error" value="<%=PermissionForm.FORWARD_ERROR%>" />

<c:choose>
<c:when test="${forward.name eq forward_add}">
	<script type="text/javascript">
		//alert('<fmt:message key='isr.message.createRecordSuccess'/>');
		document.location = "permission.jsp";
	</script>
</c:when>
<c:when test="${forward.name eq forward_error}">
	<script type="text/javascript">
		alert('<fmt:message key='isr.message.createRecordFailure'/>');
		document.location = "permission.jsp";
	</script>
</c:when>
<c:when test="${forward.name=='cancel_form_action'}">
    <c:redirect url="permission.jsp"/>
</c:when>
</c:choose>

<x:config>
	<page name="permission">
		<com.tms.collab.isr.permission.ui.PermissionForm name="permissionForm"/>
    </page>
</x:config>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.label.setupGroupPermission"/></td>
	</tr>
	<tr>
		<td><x:display name="permission.permissionForm" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>