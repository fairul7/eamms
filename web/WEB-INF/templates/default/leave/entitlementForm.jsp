


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
						<fmt:message key="general.leave.leaveType" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.leaveType.absoluteName}" />
					</td>
				</tr>
				
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<fmt:message key="general.leave.serviceClassification" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.serviceClass.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<fmt:message key="general.leave.serviceYears" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.serviceYears.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<fmt:message key="general.leave.entitlement" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.entitlement.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
					&nbsp;
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.addButton.absoluteName}" />
					</td>
				</tr>
								
				<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
			
		</td>
	</tr>
	<tr>
		<td colspan="2">
		<hr/>
		</td>
	</tr>
</table>

</td>
</tr>	
</table>





