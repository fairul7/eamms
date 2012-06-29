



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
					<td class="classRowLabel" valign="top" align="right" width="150px">
						<x:display name="${form.childMap.labelSubject.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.subject.absoluteName}" />
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
						<x:display name="${form.childMap.labelSeverity.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.severity.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelContactedBy.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.contactedBy.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelIncidentType.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.incidentType.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelProduct.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.product.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelFeature.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.features.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelproperty1.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property1.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelproperty2.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property2.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelproperty3.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property3.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelproperty4.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.database.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelproperty5.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property5.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelproperty6.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property6.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelAttachment.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.attachment.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelResolution.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.resolution.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelButton.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.buttonPanel.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelEscalation.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.usersPanel.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelButton2.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.resolvePanel.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.labelLogging.absoluteName}" />
					</td>
					<td class="classRow">
						
						<x:display name="${form.childMap.labelLogs.absoluteName}" />
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

