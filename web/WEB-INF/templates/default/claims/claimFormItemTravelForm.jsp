


<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<c:set var="form" value="${w}" />

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td>
                        <jsp:include page="../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">
				
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.date.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.df_TimeFrom.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.Remarks.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tf_Remarks.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.project.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.sb_Project.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.travelFrom.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tf_travelFrom.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.travelTo.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tf_travelTo.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.mileage.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tf_Mileage.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.toll.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tf_Toll.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.parking.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tf_Parking.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.allowance.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tf_Allowance.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
					&nbsp;
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

</td>
</tr>	
</table>


