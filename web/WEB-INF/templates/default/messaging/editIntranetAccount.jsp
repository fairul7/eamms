


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
						<x:display name="${form.childMap.fromAddress.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['2'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.indicator.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['3'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.smtpServer.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['4'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.smtpPort.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['5'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.anonymous.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['6'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.username.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['7'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.password.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['8'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.signature.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['9'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.submit.absoluteName}" />
					</td>
				</tr>
								
				<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
		</td>
	</tr>
</table>



