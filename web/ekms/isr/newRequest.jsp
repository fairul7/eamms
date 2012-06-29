<%@page import="java.util.Collection,
				com.tms.collab.isr.setting.model.ConfigDetailObject,
				com.tms.collab.isr.setting.model.ConfigModel,
				com.tms.collab.isr.permission.model.ISRGroup,
				com.tms.collab.isr.ui.NewRequestForm,
				kacang.Application" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_NEW_REQUEST %>" />
<%@include file="includes/accessControl.jsp" %>

<% 
ConfigModel model = (ConfigModel)Application.getInstance().getModule(ConfigModel.class);
Collection allowedFileExtensions = model.getConfigDetailsByType(ConfigDetailObject.ALLOWED_FILE_EXTENSION, null);
%>

<c-rt:set var="forward_add" value="<%=NewRequestForm.FORWARD_SUCCESS%>" />
<c-rt:set var="forward_error" value="<%=NewRequestForm.FORWARD_ERROR%>" />
<c-rt:set var="forward_attachment_error" value="<%=NewRequestForm.FORWARD_ATTACHMENT_ERROR%>" />
<c-rt:set var="forward_illegal_file_type" value="<%=NewRequestForm.ILLEGAL_FILE_TYPE%>" />
<c-rt:set var="forward_illegal_file_size" value="<%=NewRequestForm.ILLEGAL_FILE_SIZE%>" />

<c:choose>
<c:when test="${forward.name eq forward_add}">
	<script type="text/javascript">
		//alert('<fmt:message key='isr.message.createRecordSuccess'/>');
		document.location = "viewRequestListing.jsp";
	</script>
</c:when>
<c:when test="${forward.name eq forward_error}">
	<script type="text/javascript">
		alert('<fmt:message key='isr.message.createRecordFailure'/>');
		document.location = "newRequest.jsp";
	</script>
</c:when>
<c:when test="${forward.name eq forward_attachment_error}">
	<script type="text/javascript">
		alert('<fmt:message key='isr.message.attachmentError'/>');
		document.location = "newRequest.jsp";
	</script>
</c:when>
<c:when test="${forward.name eq forward_illegal_file_type}">
	<script type="text/javascript">
		var alertMessage = "<fmt:message key='isr.message.illegalFileType'/>";
		<c:forEach var="illegalFile" items="${illegalFiles}">
			alertMessage += "\n- " + "<c:out value='${illegalFile}'/>";
		</c:forEach>
		alert(alertMessage);
	</script>
</c:when>
<c:when test="${forward.name eq forward_illegal_file_size}">
	<script type="text/javascript">
		alert('<fmt:message key='isr.message.illegalFileSize'/>');
	</script>
</c:when>
<c:when test="${forward.name=='cancel_form_action'}">
    <c:redirect url="viewRequestListing.jsp"/>
</c:when>
</c:choose>

<x:config>
	<page name="isr">
		<com.tms.collab.isr.ui.NewRequestForm name="newRequestForm"/>
    </page>
</x:config>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<script type="text/javascript">
String.prototype.endsWith = function (s) {
    if ('string' != typeof s) {
        throw('IllegalArgumentException: Must pass a ' +
            ' string to String.prototype.endsWith()');
    }
    var start = this.length - s.length;
    return this.substring(start) == s;
};

function checkDuplicatedFilenames(thisObj) {
	var correctFileType = false;
	<c-rt:forEach var="allowedFileExtension" items="<%=allowedFileExtensions%>">
		if(!correctFileType && thisObj.value.endsWith("<c:out value='${allowedFileExtension.configDetailName}'/>")) {
			correctFileType = true;
		}
	</c-rt:forEach>
	
	if(correctFileType) {
		var formObj = document.forms['isr.newRequestForm'];
		var attachment0 = formObj.elements['isr.newRequestForm.attachmentPanel.attachments0'];
		var attachment1 = formObj.elements['isr.newRequestForm.attachmentPanel.attachments1'];
		var attachment2 = formObj.elements['isr.newRequestForm.attachmentPanel.attachments2'];
		var duplicatedEntryTriggered = false;
		
		if(thisObj == attachment0) {
			if(thisObj.value == attachment1.value ||
				thisObj.value == attachment2.value) {
				duplicatedEntryTriggered = true;
			}
		}
		else if(thisObj == attachment1) {
			if(thisObj.value == attachment0.value ||
				thisObj.value == attachment2.value) {
				duplicatedEntryTriggered = true;
			}
		}
		else if(thisObj == attachment2) {
			if(thisObj.value == attachment0.value ||
				thisObj.value == attachment1.value) {
				duplicatedEntryTriggered = true;
			}
		}
		
		if(duplicatedEntryTriggered) {
			alert("<fmt:message key='isr.message.duplicatedAttachmentEntryNotAllowed'/>:\n" + thisObj.value);
			thisObj.value = "";
		}
	}
	else {
		alert("<fmt:message key='isr.message.selectedFileIllegalFileType' />");
		thisObj.value = "";
	}
}
</script>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.label.newRequest"/></td>
	</tr>
	<tr>
		<td><x:display name="isr.newRequestForm" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>