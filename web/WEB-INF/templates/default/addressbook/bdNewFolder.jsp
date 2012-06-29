

<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<c:set var="form" value="${w}" />

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td>
			<table width="100%" cellpadding="3" cellspacing="1">
				<jsp:include page="../form_header.jsp" flush="true"></jsp:include>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb1.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.nameField.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb2.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.description.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb3.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.folderSelectBox.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb4.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.buttonPanel.absoluteName}" />
					</td>
				</tr>
								
				<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
		</td>
	</tr>
</table>

