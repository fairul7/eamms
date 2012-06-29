

<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<c:set var="form" value="${w}" />
<jsp:include page="../form_header.jsp" flush="true"></jsp:include>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr>
		<td>
                        
			<table width="100%" cellpadding="3" cellspacing="1">
				
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l1.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.severityOptions.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l2.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.resolutionStateOptions.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l3.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.contactedByOptions.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l4.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.incidentTypeOptions.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l5.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property1Label.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l11.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property1Options.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l6.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property2Label.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l12.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property2Options.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l7.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property3Label.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l13.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property3Options.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l8.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property4Label.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l14.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property4Options.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l9.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property5Label.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l15.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property5Options.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l10.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property6Label.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l16.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.property6Options.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l17.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.p1.absoluteName}" />
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


