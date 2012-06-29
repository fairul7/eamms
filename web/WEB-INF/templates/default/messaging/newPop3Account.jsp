


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
						<x:display name="${form.childMap['1'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tfName.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['2'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.sbIndicator.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['3'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.sbDeliveryFolderId.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['4'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tfServerName.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['5'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tfServerPort.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['6'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tfUsername.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['7'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.pwPassword.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['17'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.pwConfirmPassword.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['8'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.cbLeaveMailOnServer.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['9'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap['10'].absoluteName}" />
					</td>
				</tr>
				
				
								
				<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
		</td>
	</tr>
</table>

