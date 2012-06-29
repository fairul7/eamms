


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
						<x:display name="${form.childMap.lb23.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.company.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb10.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.email.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb5.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.address.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb6.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.city.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb7.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.state.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb8.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.postcode.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb9.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.country.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb11.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.phone.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb13.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.fax.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.lb16.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.comments.absoluteName}" />
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


