


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
						<x:display name="${form.childMap['1'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.name.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['2'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.parentFolder.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap['3'].absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.buttons.absoluteName}" />
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



