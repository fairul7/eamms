

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
					<td class="classRowLabel" valign="top" align="right" width="150px">
						<x:display name="${form.childMap.date.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.df_TimeFrom.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.category.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.sb_Category.absoluteName}" />
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
						<x:display name="${form.childMap.Remarks.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tf_Remarks.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.Amount.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tf_Amount.absoluteName}" />
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
								
				
			</table>
			
		</td>
	</tr>
</table>

</td>
</tr>	
</table>
<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>

