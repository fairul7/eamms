


<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<c:set var="form" value="${w}" />

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr>
		<td>
                        <jsp:include page="../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">
				
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l1.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tfIncidentCode.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l2.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tfSearchText.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l3.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.queryPanel.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l4.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.resolved.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l5.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.severity.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l6.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.incidentType.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l7.absoluteName}" />(<fmt:message key="general.from" />)
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.dateCreatedFrom.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						(<fmt:message key="general.to" />)
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.dateCreatedTo.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l8.absoluteName}" />(<fmt:message key="general.from" />)
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.dateModifiedFrom.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						(<fmt:message key="general.to" />)
					</td>
					<td class="classRow" valign="top" >
						<x:display name="${form.childMap.dateModifiedTo.absoluteName}" />
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



