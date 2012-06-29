<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
	<td>
		<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
   			<tr>
        		<td>
					<table width="100%" cellpadding="3" cellspacing="1">
                		<jsp:include page="../form_header.jsp" flush="true"/>
						 <tr>
						     <td class="classRowLabel" width="20%" valign="top" align="right">
						         <fmt:message key='po.menu.itemCategory'/> *
						     </td>
						     <td class="classRow" width="80%" valign="top">
						         <x:display name="${w.itemCategory.absoluteName}" />
						     </td>
						 </tr>
						 <tr>
							<td class="classRowLabel" width="20%" valign="top" align="right">
							    <fmt:message key='po.label.itemCode'/> *
							</td>
							<td class="classRow" width="80%" valign="top">
							    <x:display name="${w.itemCode.absoluteName}" />
							</td>
						 </tr>
						 <tr>
							 <td class="classRowLabel" width="15%" valign="top" align="right"><b>
							 	<fmt:message key='po.label.itemDesc'/> *
							 </b></td>
							 <td class="classRow" width="80%" valign="top">
							    <x:display name="${w.itemDesc.absoluteName}" />
							 </td>
						</tr>
						<tr>
							<td class="classRowLabel" width="20%" valign="top" align="right"><b>
							    <fmt:message key='po.label.unitOfMeasure'/> *
							</b></td>
							<td class="classRow" width="80%" valign="top">
							    <x:display name="${w.unitOfMeasure.absoluteName}" />
							</td>
						</tr>
						<tr>
							<td class="classRowLabel" width="20%" valign="top" align="right"><b>
								<fmt:message key='po.label.minQty'/> *
							</b></td>
							<td class="classRow" width="80%" valign="top">
								<x:display name="${w.minAmount.absoluteName}" />
							 </td>
						</tr>
						<tr>
							<td class="classRowLabel" width="20%" valign="top" align="right"><b>
								<fmt:message key='po.label.approved'/>
							</b></td>
							<td class="classRow" width="80%" valign="top">
								<x:display name="${w.approved.absoluteName}" />
							 </td>
						</tr>
						<tr>
							<td class="classRowLabel" width="20%" valign="top" align="right">
							</td>
							<td class="classRowLabel" width="20%" valign="top">
							    <x:display name="${w.btnPanel.absoluteName}" /> 
							</td>             
						</tr>     
                		<jsp:include page="../form_footer.jsp" flush="true"/>
					</table>
        		</td>
			</tr>
		</table>
	</td>
</tr>
</table>

