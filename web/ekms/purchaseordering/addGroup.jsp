<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.permission.ui.PermissionForm"%>

<x:permission var="isAuthorized" module="com.tms.sam.po.model.PrePurchaseModule" permission="com.tms.sam.po.ManagePermission"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>

<c-rt:set var="forwardSuccess" value="<%= PermissionForm.FORWARD_SUCCESS %>" />

<c:if test="${forward.name eq forwardSuccess}">
	<script type="text/javascript">
		var alertMessage = "<fmt:message key='po.message.successfulPermission'/>";
		alert(alertMessage);
		document.location = "permission.jsp";
	</script>
    
</c:if>

<c:if test="${forward.name=='cancel_form_action'}">
     <c:redirect url="permission.jsp" />
</c:if>

<x:config>
	<page name="permission">
		<com.tms.sam.po.permission.ui.PermissionForm name="AddPermission"/>
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="po.label.setupGroupPermission"/></td>
	</tr>
	<tr>
		<td><x:display name="permission.AddPermission" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>