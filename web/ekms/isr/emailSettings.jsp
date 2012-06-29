<%@page import="com.tms.collab.isr.permission.model.ISRGroup,
				com.tms.collab.isr.setting.ui.EmailSettingForm" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_ACCESS_SETUP %>" />
<%@include file="includes/accessControl.jsp" %>

<c-rt:set var="forward_add" value="<%=EmailSettingForm.FORWARD_SUCCESS%>" />
<c-rt:set var="forward_error" value="<%=EmailSettingForm.FORWARD_ERROR%>" />

<c:choose>
<c:when test="${forward.name eq forward_add}">
	<script type="text/javascript">
		//alert('<fmt:message key='isr.message.updateSuccess'/>');
		document.location = "emailSettings.jsp";
	</script>
</c:when>
<c:when test="${forward.name eq forward_error}">
	<script type="text/javascript">
		alert('<fmt:message key='isr.message.updateFailure'/>');
		document.location = "emailSettings.jsp";
	</script>
</c:when>
<c:when test="${forward.name=='cancel_form_action'}">
    <c:redirect url="globalSettings.jsp"/>
</c:when>
</c:choose>

<x:config>
	<page name="setup">
		<com.tms.collab.isr.setting.ui.EmailSettingForm name="emailSettings"/>
    </page>
</x:config>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.label.setupGlobalSettings"/></td>
	</tr>
	<tr>
		<td><x:display name="setup.emailSettings" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>