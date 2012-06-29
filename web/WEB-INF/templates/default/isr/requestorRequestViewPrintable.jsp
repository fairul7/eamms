<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<table width="100%" border="0" cellspacing="1" cellpadding="4" >
	<tr>
		<td valign="top" class="contentBgColor fieldTitle" width="20%"><fmt:message key="isr.label.requestId"/></td>
		<td class="contentBgColor">
			<c:out value="${param.requestId }" />
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle" width="20%"><fmt:message key="isr.label.requestingDept"/></td>
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
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.resolution"/></td>
		<td class="contentBgColor">
			<c:if test="${!empty w.resolution}">
				<x:display name="${w.resolution.absoluteName}"/> 
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