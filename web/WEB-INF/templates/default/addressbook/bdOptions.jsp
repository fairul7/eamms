
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
						<x:display name="${form.childMap.l1.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.csvFile.absoluteName}" />
					</td>
				</tr>
				
			 <tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.selectEncodeText.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.encodingSelect.absoluteName}" />
					</td>
				</tr>
				
				
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l2.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.folder.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l3.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.tmpPanel.absoluteName}" />
					</td>
				</tr>
								
				<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
		</td>
	</tr>
</table>


