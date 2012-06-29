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
                    <td height="22" class="contentTitleFont" colspan="2">
						<fmt:message key="po.label.po"/> > <fmt:message key="supplier.label.view"/>
					</td>
                </tr>
                <tr>
                    <td width="20%" class="classRowLabel" valign="top" align="right">
                    	<fmt:message key='po.menu.currency'/>* 
                    </td>
                    <td class="classRow">
                    	<x:display name="${w.currency.absoluteName}" />
                    </td>
                </tr>
                <tr>
                    <td width="20%" class="classRowLabel" valign="top" align="right">
                    	<fmt:message key='po.label.purchaseItem'/>
                    </td>
                    <td class="classRow">
                    	<c:set var="count" value="0"/>
	                    	<table width="100%" border="0" cellspacing="1" cellpadding="3">
				           		<tr>
				           			<td  bgcolor="#BBD5F2" valign="top" align="left">
				               			<b><fmt:message key='po.label.no'/></b>
				               		</td>
				           			<td  bgcolor="#BBD5F2" valign="top" align="left" width="15%">
				               			<b><fmt:message key='po.label.itemCode'/></b>
				               		</td>
				           			<td  bgcolor="#BBD5F2" valign="top" width="20%">
				               			<b><fmt:message key='po.label.itemDesc'/></b>
				           			</td>
				           			<td  bgcolor="#BBD5F2" valign="top" align="left" width="10%">
				               			<b><fmt:message key='po.label.qty'/></b>
				               		</td>
				           			<td  bgcolor="#BBD5F2" valign="top">
				               			<b><fmt:message key='po.label.unitOfMeasure'/></b>
				           			</td>
				           			<td  bgcolor="#BBD5F2" valign="top">
				               			<b><fmt:message key='po.label.unitPrice'/></b>
				           			</td>
				           			<td  bgcolor="#BBD5F2" valign="top" width="20%">
				               			<b><fmt:message key='po.label.amount'/></b>
				           			</td>
				           		</tr>
				           			<c:forEach items="${w.itemCode}" var="itemCode" varStatus = "status">
					                  <c:set var="itemDesc" value="${w.itemDesc[status.index]}"/>
					                   <c:set var="qty" value="${w.qty[status.index]}"/>
					                    <c:set var="hide" value="${w.hide[status.index]}"/>
					                    <c:set var="unitOfMeasure" value="${w.unitOfMeasure[status.index]}"/>
					                    <c:set var="unitPrice" value="${w.unitPrice[status.index]}"/>
					                    <c:set var="amount" value="${w.amount[status.index]}"/>
								    	<tr>
									   		<td class="contentBgColor" valign="top" align="right">
									   			<c:set var="count" value="${count + 1}"/>
																	<c:out value="${count}" /> 
									   		</td>
											<td class="contentBgColor" valign="top" align="right">
												<c:out value="${itemCode}" />
												<br>
											</td>
											<td class="contentBgColor" valign="top">
									   			<c:out value="${itemDesc}" />
									   		</td>
											<td class="contentBgColor" valign="top" align="right">
												<x:display name="${hide.absoluteName}" />
												<x:display name="${qty.absoluteName}" />
											</td>
											<td class="contentBgColor" width="30%" valign="top">
									   			<c:out value="${unitOfMeasure}" />
									   		</td>
									   		<td class="contentBgColor" width="30%" valign="top">
									   			<x:display name="${unitPrice.absoluteName}" />
									   		</td>
									   		<td class="contentBgColor" width="30%" valign="top">
									   		<x:display name="${amount.absoluteName}" />
									   	
									   		</td>
										 </tr>
									</c:forEach>
				           	</table>
                    </td>
                </tr>
	   			<tr>
                    <td class="classRowLabel" valign="top" align="right">
                    	<fmt:message key='supplier.label.supp'/>
                    </td>
                    <td class="classRow">
                    	<x:display name="${w.txtSupplier.absoluteName}" />
                    </td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">
                    	<fmt:message key='supplier.label.company'/>
                    </td>
                    <td class="classRow">
                    	<x:display name="${w.txtCompany.absoluteName}" />
                    </td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">
                    	<fmt:message key='supplier.label.telephone'/>
                    </td>
                    <td class="classRow">
                    	 <x:display name="${w.txtTelephone.absoluteName}" />
                    </td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">
                    	 <fmt:message key='supplier.label.dateSent'/>
                    </td>
                    <td class="classRow">
                    	<x:display name="${w.txtDateSent.absoluteName}" />
                    </td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">
                    	<fmt:message key='supplier.label.dateReceived'/>
                    </td>
                    <td class="classRow">
                    	<x:display name="${w.txtDateReceived.absoluteName}" />
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
                    <td class="classRowLabel" valign="top" align="right">
                    	<fmt:message key='supplier.label.quotation'/>
                    </td>
                    <td class="classRow">
                    	<x:display name= "${w.txtQuotation.absoluteName }"/>
                    </td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">
                    	 <x:display name="${w.attached.absoluteName}" />
                    </td>
                    <td class="classRow">
                    	<c:set var="map" value="${w.attachmentMap}" />
							<div id="attachmentItems">
								<c:forEach var="a" items="${map}">
			                   		<c:set var="key" value="${a.key}" />
			    						<li>
			    							<a href="/ekms/po/downloadFile?attachID=<c:out value="${a.value}&type=supplier" />"><c:out value="${a.key}" /></a>
			    						</li>
								</c:forEach>
							</div>	
                    </td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">
                    	<fmt:message key='supplier.label.attachment'/>
                    </td>
                    <td class="classRow">
                    	<x:display name="${w.attachment.absoluteName}" />
                    </td>
                </tr>
                 <tr>
                    <td class="classRowLabel" valign="top" align="right">
                    	<fmt:message key='supplier.label.minBudget'/>*
                    </td>
                    <td class="classRow">
                    	<x:display name="${w.totalQuotation.absoluteName}" />
                    </td>
                </tr>
                 <tr>
                    <td class="classRowLabel" valign="top" align="right">
                    	<fmt:message key='supplier.label.responded'/>
                    </td>
                    <td class="classRow">
                    	<x:display name="${w.responded.absoluteName}" />
                    </td>
                </tr>
                 <tr>
                    <td class="classRowLabel" valign="top" align="right">
                    	<fmt:message key='supplier.label.recommend'/>
                    </td>
                    <td class="classRow">
                    	<x:display name="${w.recommended.absoluteName}" />
                    </td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">
                    	
                    </td>
                    <td class="classRow">
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

<script>
var size = <c:out value="${w.size}"/>;
for(var i=0; i<size; i++){
	document.forms['viewSupplierPg.ViewSupplier'].elements['viewSupplierPg.ViewSupplier.amount'+i].readOnly= true;
}

function calculateAmount(count){
	var quantity = document.forms['viewSupplierPg.ViewSupplier'].elements['viewSupplierPg.ViewSupplier.hide'+ count].value;
	document.forms['viewSupplierPg.ViewSupplier'].elements['viewSupplierPg.ViewSupplier.amount'+count].value=
		document.forms['viewSupplierPg.ViewSupplier'].elements['viewSupplierPg.ViewSupplier.unitPrice'+count].value * quantity;

}
</script>
