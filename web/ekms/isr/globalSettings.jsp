<%@page import="com.tms.collab.isr.permission.model.ISRGroup,
				com.tms.collab.isr.setting.ui.GlobalSettingsForm" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_ACCESS_SETUP %>" />
<%@include file="includes/accessControl.jsp" %>

<c-rt:set var="forward_add" value="<%=GlobalSettingsForm.FORWARD_SUCCESS%>" />
<c-rt:set var="forward_error" value="<%=GlobalSettingsForm.FORWARD_ERROR%>" />
<c-rt:set var="forward_invalid_ext" value="<%=GlobalSettingsForm.FORWARD_INVALID_EXT%>" />

<c:choose>
<c:when test="${forward.name eq forward_add}">
	<script type="text/javascript">
		//alert('<fmt:message key='isr.message.updateSuccess'/>');
		document.location = "globalSettings.jsp";
	</script>
</c:when>
<c:when test="${forward.name eq forward_error}">
	<script type="text/javascript">
		alert('<fmt:message key='isr.message.updateFailure'/>');
		document.location = "globalSettings.jsp";
	</script>
</c:when>
<c:when test="${forward.name=='cancel_form_action'}">
    <c:redirect url="globalSettings.jsp"/>
</c:when>
<c:when test="${forward.name eq forward_invalid_ext}">
	<script type="text/javascript">
		var alertMessage = "<fmt:message key='isr.message.invalidFileExt'/>";
		<c:forEach var="invalidFileExt" items="${invalidFileExts}">
			alertMessage += "\n- " + "<c:out value='${invalidFileExt}'/>";
		</c:forEach>
		alert(alertMessage);
	</script>
</c:when>
</c:choose>

<x:config>
	<page name="setup">
		<com.tms.collab.isr.setting.ui.GlobalSettingsForm name="globalSettings"/>
    </page>
</x:config>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<script type="text/javascript">
function fileTypeChecking(event) {
	var attachmentFileExtObj = document.forms['setup.globalSettings'].elements['setup.globalSettings.attachmentFileExt'];
	var charCode = (event.which) ? event.which : event.keyCode;
	
	/*
	8 is backspace, 13 is enter, 46 is dot,
	48 - 57 is 0 - 9
	68 - 90 is A - Z
	97 - 122 is a - z
	*/
	if(charCode == 8 || charCode == 13 || charCode == 46 || 
		(charCode >= 48 && charCode <= 57) ||
		(charCode >= 68 && charCode <= 90) ||
		(charCode >= 97 && charCode <= 122)) {
		return true;
	}
	else {
		return false;
	}
}
</script>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.label.setupGlobalSettings"/></td>
	</tr>
	<tr>
		<td><x:display name="setup.globalSettings" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>