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
						<x:display name="${form.childMap.lb15.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.folderSelectBox.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb2.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.firstName.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb3.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.comments.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb4.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.contactSelectBox.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb6.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.usersSelectBox.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb5.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.emails.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lbApproved.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.approved.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb17.absoluteName}" />
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

</td>
</tr>	
</table>