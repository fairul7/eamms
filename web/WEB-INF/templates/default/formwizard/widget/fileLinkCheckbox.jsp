<%@ include file="/common/header.jsp" %>
<table cellpadding="2" cellspacing="0">
	<tr>
		<td nowrap><fmt:message key="formWizard.label.currentFile"/> : </td>
		<td><x:display name="${widget.link.absoluteName}"/></td>
	</tr>
	<tr><td colspan="2"><x:display name="${widget.checkBox.absoluteName}"/></td></tr>
	<tr>
		<td nowrap><fmt:message key="formWizard.label.newFile"/> : </td>
		<td><x:display name="${widget.file.absoluteName}"/></td>
	</tr>
</table>
