




<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<c:set var="form" value="${w}" />
<jsp:include page="../form_header.jsp" flush="true"></jsp:include>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td>
                        
			<table width="100%" cellpadding="3" cellspacing="1">
				
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelName.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.productName.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelDescription.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.description.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelFeatures.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.panel.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelOwners.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.owners.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
					&nbsp;
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.button.absoluteName}" />
					</td>
				</tr>
					
			</table>
		</td>
	</tr>
</table>
</td>
</tr>	
</table>
<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>	




