


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
						<x:display name="${form.childMap.l1.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.libraryName.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l2.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.albumName.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l3.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.description.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l5.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.featuredAlbumPanel.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l4.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.eventDate.absoluteName}" />
					</td>
				</tr>
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.childMap.l6.absoluteName}" />
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.eventTime.absoluteName}" />
					</td>
				</tr>
				
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
					&nbsp;
					</td>
					<td class="classRow">
						<x:display name="${form.childMap.buttonsPanel.absoluteName}" />
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


