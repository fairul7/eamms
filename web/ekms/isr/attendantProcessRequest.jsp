<%@page import="java.util.Collection,
				com.tms.collab.isr.setting.model.ConfigDetailObject,
				com.tms.collab.isr.setting.model.ConfigModel,
				com.tms.collab.isr.ui.AttendantProcessRequestForm,
				kacang.Application" %>
<%@include file="/common/header.jsp" %>

<% 
ConfigModel model = (ConfigModel)Application.getInstance().getModule(ConfigModel.class);
Collection allowedFileExtensions = model.getConfigDetailsByType(ConfigDetailObject.ALLOWED_FILE_EXTENSION, null);
%>

<c-rt:set var="forward_add" value="<%=AttendantProcessRequestForm.FORWARD_SUCCESS%>" />
<c-rt:set var="forward_completed" value="<%=AttendantProcessRequestForm.FORWARD_COMPLETED%>" />
<c-rt:set var="forward_assigned" value="<%=AttendantProcessRequestForm.FORWARD_ASSIGNED%>" />
<c-rt:set var="forward_error" value="<%=AttendantProcessRequestForm.FORWARD_ERROR%>" />
<c-rt:set var="forward_illegal_file_type" value="<%=AttendantProcessRequestForm.ILLEGAL_FILE_TYPE%>" />
<c-rt:set var="forward_illegal_file_size" value="<%=AttendantProcessRequestForm.ILLEGAL_FILE_SIZE%>" />

<x:config>
	<page name="isr">
		<com.tms.collab.isr.ui.AttendantProcessRequestForm name="attendantProcessRequestForm"/>
    </page>
</x:config>

<c:choose>
<c:when test="${!empty param.requestId }">
	<x:set name="isr.attendantProcessRequestForm" property="requestId" value="${param.requestId}" />
</c:when>
</c:choose>
<c:if test="${param.clarificationDesc eq 'true'}">
	<x:set name="isr.attendantProcessRequestForm" property="clarificationDesc" value="${true}" />
</c:if>
<c:if test="${empty param.clarificationDesc || param.clarificationDesc eq 'false'}">
	<x:set name="isr.attendantProcessRequestForm" property="clarificationDesc" value="${false}" />
</c:if>
<c:if test="${param.assignmentRemarksDesc eq 'true'}">
	<x:set name="isr.attendantProcessRequestForm" property="assignmentRemarksDesc" value="${true}" />
</c:if>
<c:if test="${empty param.assignmentRemarksDesc || param.assignmentRemarksDesc eq 'false'}">
	<x:set name="isr.attendantProcessRequestForm" property="assignmentRemarksDesc" value="${false}" />
</c:if>
<c:if test="${param.suggestedResolutionDesc eq 'true'}">
	<x:set name="isr.attendantProcessRequestForm" property="suggestedResolutionDesc" value="${true}" />
</c:if>
<c:if test="${empty param.suggestedResolutionDesc || param.suggestedResolutionDesc eq 'false'}">
	<x:set name="isr.attendantProcessRequestForm" property="suggestedResolutionDesc" value="${false}" />
</c:if>

<c:set var="requestId" value="${widgets['isr.attendantProcessRequestForm'].requestId}" />

<c:choose>
<c:when test="${forward.name eq forward_add}">
	<c:choose>
	<c:when test="${!empty duplicatedFiles}">
		<script type="text/javascript">
			var alertMessage = "<fmt:message key='isr.message.duplicatedAttachmentSkipped'/>:";
			<c:forEach var="duplicatedFile" items="${duplicatedFiles}">
				alertMessage += "\n- " + "<c:out value='${duplicatedFile}'/>";
			</c:forEach>
			alert(alertMessage);
			document.location = "attendantProcessRequest.jsp?requestId=<c:out value='${requestId}'/>";
		</script>
	</c:when>
	<c:otherwise>
		<c:redirect url="attendantProcessRequest.jsp?requestId=${requestId}" />
	</c:otherwise>
	</c:choose>
</c:when>
<c:when test="${forward.name eq forward_completed}">
	<c:redirect url="attendantViewRequest.jsp?requestId=${requestId}" />
</c:when>
<c:when test="${forward.name eq forward_assigned}">
	<c:redirect url="attendantProcessRequest.jsp?requestId=${requestId}&expandAssign=true" />
</c:when>
<c:when test="${forward.name eq forward_error}">
	<script type="text/javascript">
		alert('<fmt:message key='isr.message.updateFailure'/>');
		document.location = "attendantProcessRequest.jsp?requestId=<c:out value='${requestId}'/>";
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
		var formObj = document.forms['isr.attendantProcessRequestForm'];
		var attachment0 = formObj.elements['isr.attendantProcessRequestForm.resolutionAttachmentPanel.resolutionAttachments0'];
		var attachment1 = formObj.elements['isr.attendantProcessRequestForm.resolutionAttachmentPanel.resolutionAttachments1'];
		var attachment2 = formObj.elements['isr.attendantProcessRequestForm.resolutionAttachmentPanel.resolutionAttachments2'];
		var duplicatedEntryTriggered = false;
		
		if(attachment0 != null && thisObj == attachment0) {
			if((attachment1 != null && thisObj.value == attachment1.value) ||
				(attachment2 != null && thisObj.value == attachment2.value)) {
				duplicatedEntryTriggered = true;
			}
		}
		else if(attachment1 != null && thisObj == attachment1) {
			if(thisObj.value == attachment0.value ||
				(attachment2 != null && thisObj.value == attachment2.value)) {
				duplicatedEntryTriggered = true;
			}
		}
		else if(attachment2 != null && thisObj == attachment2) {
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
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.label.processRequest"/></td>
	</tr>
	<tr>
		<td><x:display name="isr.attendantProcessRequestForm" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>