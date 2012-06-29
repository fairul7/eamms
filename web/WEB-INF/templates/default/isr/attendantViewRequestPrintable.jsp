<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<script type="text/javascript">
function exportRequest() {
	//mywindow = window.open("/ekms/isr/requestorViewRequestPrintable.jsp?requestId=<c:out value='${w.requestId}' />", "requestorExportRequest", "resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=800, height=600");
    //mywindow.moveTo(0,0);
    document.location = "/ekms/isr/attendantViewRequestPrintable.jsp?requestId=<c:out value='${w.requestId}' />";
}
</script>

<table width="100%" border="0" cellspacing="1" cellpadding="4" >
	<tr>
		<td valign="top" class="contentBgColor fieldTitle" width="20%"><fmt:message key="isr.label.requestId"/></td>
		<td class="contentBgColor">
			<c:out value="${param.requestId }" />
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.requestingDept"/></td>
		<td class="contentBgColor">
			<x:display name="${w.requestingDepartment.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.attentionTo"/></td>
		<td class="contentBgColor">
			<x:display name="${w.attentionTo.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.subject"/></td>
		<td class="contentBgColor">
			<x:display name="${w.subject.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.description"/></td>
		<td class="contentBgColor">
			<x:display name="${w.description.absoluteName}"/> 
			<c:if test="${!empty w.remarksPanel}">
			<br />
			<x:display name="${w.remarksPanel.absoluteName}"/>
			</c:if>
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.status"/></td>
		<td class="contentBgColor">
			<x:display name="${w.status.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.priorityByRequester"/></td>
		<td class="contentBgColor">
			<x:display name="${w.priority.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.priorityByAdmin"/></td>
		<td class="contentBgColor">
			<c:if test="${!empty w.priorityByAdmin}">
				<x:display name="${w.priorityByAdmin.absoluteName}"/>
			</c:if>
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.requestType"/></td>
		<td class="contentBgColor">
			<x:display name="${w.requestType.absoluteName}"/>
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.attachmentByRequestingDept"/></td>
		<td class="contentBgColor">
			<x:display name="${w.attachmentPanel.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.requestedDateTime"/></td>
		<td class="contentBgColor">
			<x:display name="${w.dateCreated.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.consolidatedResolution"/></td>
		<td class="contentBgColor">
			<c:if test="${!empty w.consolidatedResolution}">
				<x:display name="${w.consolidatedResolution.absoluteName}"/> 
			</c:if>
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.resolutionAttachment"/></td>
		<td class="contentBgColor">
			<c:if test="${!empty w.resolutionAttachmentPanel}">
				<x:display name="${w.resolutionAttachmentPanel.absoluteName}"/> 
			</c:if>
		</td>
	</tr>
</table>

<!-- Resolutions -->
<c:if test="${!empty w.suggestedResolutionPanel }">
<table width="100%" border="0" cellspacing="0" cellpadding="4" >
	<tr>
		<td class="contentTitleFont" style="padding:5px; background-color:#1863B0; border-bottom:1px solid #FFFFFF;" colspan="2">
			<fmt:message key="isr.label.resolutions"/>
		</td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="4" >
	<tr>
		<td width="20%" valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.resolutions"/></td>
		<td class="contentBgColor">
			<x:display name="${w.suggestedResolutionPanel.absoluteName}"/>
		</td>
	</tr>
</table>
</c:if>

<!-- Clarification Messages -->
<c:if test="${!empty w.clarificationMessagesPanel }">
<table width="100%" border="0" cellspacing="0" cellpadding="4" >
	<tr>
		<td class="contentTitleFont" style="padding:5px; background-color:#1863B0; border-bottom:1px solid #FFFFFF;" colspan="2">
			<fmt:message key="isr.label.clarificationMessages"/>
		</td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="4" >
	<tr>
		<td width="20%" valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.clarificationMessages"/></td>
		<td class="contentBgColor">
			<x:display name="${w.clarificationMessagesPanel.absoluteName}"/>
		</td>
	</tr>
</table>
</c:if>