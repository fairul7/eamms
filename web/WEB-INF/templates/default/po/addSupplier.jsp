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
							<td class="contentTitleFont" colspan="2">
								<b><fmt:message key="quotation.label.request"/> > <fmt:message key="supplier.label.addSupp"/></b>
							</td>
						 </tr>
						 <tr>
						     <td class="classRowLabel" width="20%" valign="top" align="right">
						         <fmt:message key='supplier.label.supp'/>
						     </td>
						     <td class="classRow" width="80%" valign="top">
						         <x:display name="${w.txtSupplier.absoluteName}" />
						     </td>
						 </tr>
						 <tr>
							<td class="classRowLabel" width="20%" valign="top" align="right"><b>
							    <fmt:message key='supplier.label.company'/></b>
							</td>
							<td class="classRow" width="80%" valign="top">
							    <x:display name="${w.txtCompany.absoluteName}" />
							</td>
						 </tr>
						 <tr>
							 <td class="classRowLabel" width="15%" valign="top" align="right"><b>
							 	<fmt:message key='supplier.label.telephone'/>
							 </b></td>
							 <td class="classRow" width="80%" valign="top">
							    <x:display name="${w.txtTelephone.absoluteName}" />
							 </td>
						</tr>
						
						 <tr>
		                    <td class="classRowLabel" valign="top" align="right">
		                    	 <fmt:message key='po.label.deliveryDateRequested'/> *
		                    </td>
		                    <td class="classRow">
		                    	<x:display name="${w.txtDeliveryDate.absoluteName}" />
		                    </td>
		                </tr>
						<tr>
							<td class="classRowLabel" width="20%" valign="top" align="right"><b>
								<fmt:message key='supplier.label.quotation'/>*
							</b></td>
							<td class="classRow" width="80%" valign="top">
								<x:display name="${w.txtQuotation.absoluteName}" />
							 </td>
						</tr>
						<tr>
							<td class="classRowLabel" width="20%" valign="top" align="right"><b>
							</b></td>
							<td class="classRowLabel" width="20%" valign="top">
							    <x:display name="${w.buttonPanel.absoluteName}" /> 
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

