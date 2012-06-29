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
							<fmt:message key="po.label.po"/> > <fmt:message key="po.label.requestDetail"/>
						</td>
				   </tr>
				     <tr>
				       <td width="30%" class="classRowLabel" valign="top" align="right">
				       		<fmt:message key='purchaseRequest.label.purchaseCode'/>
				       	</td>
				       <td class="classRow">
				       		<c:out value="${w.purchaseCode}" />
				       </td>
				   </tr>
				     <tr>
				       <td width="30%" class="classRowLabel" valign="top" align="right">
				       		<fmt:message key='myRequest.label.dateRequested'/>
				       	</td>
				       <td class="classRow">
				       		<c:out value="${w.dateRequested}" />
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
				           		</tr>
				           			<c:forEach items="${w.itemCode}" var="itemCode" varStatus = "status">
					                  <c:set var="itemDesc" value="${w.itemDesc[status.index]}"/>
					                   <c:set var="qty" value="${w.qty[status.index]}"/>
					                    <c:set var="hide" value="${w.hide[status.index]}"/>
					                    <c:set var="unitOfMeasure" value="${w.unitOfMeasure[status.index]}"/>
					                    <c:set var="unitPrice" value="${w.unitPrice[status.index]}"/>
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
												<c:out value="${qty}" />
											</td>
											<td class="contentBgColor" width="30%" valign="top">
									   			<c:out value="${unitOfMeasure}" />
									   		</td>
									   		<td class="contentBgColor" width="30%" valign="top">
									   			<c:out value="${unitPrice}" />
									   		</td>
										 </tr>
									</c:forEach>
				           	</table>
                    </td>
                </tr>
				   <tr>
				       <td width="30%" class="classRowLabel" valign="top" align="right">
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
	                    	<fmt:message key='po.label.needed'/>
	                    </td>
	                    <td class="classRow">
	                    	<c:out value="${w.neededBy}" />
                    	</td>
                	</tr>
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='myRequest.label.status'/>
	                    </td>
	                    <td class="classRow">
	                    	<c:out value="${w.status}" />
                    	</td>
                	</tr>
				   <tr>
				       <td class="classRowLabel" valign="top" align="right">
				       		<fmt:message key='supplier.label.quotation'/>
				       	</td>
				       <td class="classRow">
				       		
				       		 <x:display name="${w.txtQuotation.absoluteName}" />
				       </td>
				   </tr>
				   <tr>
				       <td class="classRowLabel" valign="top" align="right">
				       		<x:display name="${w.quotationAttachment.absoluteName}" />
				       	</td>
				       <td class="classRow">
				       	   <c:set var="map" value="${w.attachmentMap}" />
							<div id="attachmentItems">
								<c:forEach var="a" items="${map}">
			                   		<c:set var="key" value="${a.key}" />
			   							<li><a href="/ekms/po/downloadFile?attachID=<c:out value="${a.value}&type=supplier" />"><c:out value="${a.key}" /></a></li>
								</c:forEach>
							</div>
				       </td>
				   </tr>
				   <tr>
				       <td class="classRowLabel" valign="top" align="right">
				       		<fmt:message key='supplier.label.minBudget'/>
				       	</td>
				       <td class="classRow">
				       		<x:display name="${w.currency.absoluteName}" />&nbsp;
				       		<x:display name="${w.txtMinBudget.absoluteName}" />
				       </td>
				   </tr>
				</table>
        		</td>
			</tr>
		</table>
	</td>
</tr>
</table>

<p></p>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
	<td>
		<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
		    <tr>
		        <td>
				<table width="100%" cellpadding="3" cellspacing="1">
					
				    <tr>
				        <td height="22" class="contentTitleFont" colspan="2">
							<fmt:message key="po.label.poIssuance"/>
						</td>
				   </tr>
                	  <tr>
	                    <td width="30%" class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.deliveryOrderNo'/> 
	                    	<c:if test="${w.po eq null }">
	                    	*</c:if>
	                    </td>
	                    <td class="classRow">
	                    	<c:out value="${w.delivery}" />
	                    	<c:if test="${w.po eq null }">
	                    	<x:display name="${w.deliveryOrderNo.absoluteName}" /></c:if>
                    	</td>
                	</tr>
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.invoiceNo'/> 
	                    	<c:if test="${w.po eq null }">
	                    	*</c:if>
	                    </td>
	                    <td class="classRow">
	                    	<c:out value="${w.invoiceN}" />
	                    	<c:if test="${w.po eq null }">
	                    	<x:display name="${w.invoiceNo.absoluteName}" /></c:if>
                    	</td>
                	</tr>
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.invoiceDate'/> 
	                    	<c:if test="${w.po eq null }">
	                    	*</c:if>
	                    </td>
	                    <td class="classRow">
	                    	<c:out value="${w.invoiceD}" />
	                    	<c:if test="${w.po eq null }">
	                    	<x:display name="${w.invoiceDate.absoluteName}" /></c:if>
                    	</td>
                	</tr>
                	  <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.terms'/> 
	                    	<c:if test="${w.po eq null }">
	                    	*</c:if>
	                    </td>
	                    <td class="classRow">
	                    	<c:out value="${w.paymentTerm}" />
	                    	<c:if test="${w.po eq null }">
	                    	<x:display name="${w.terms.absoluteName}" /></c:if><fmt:message key='po.label.day'/>
                    	</td>
                	</tr>
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.dateDelivered'/> 
	                    	<c:if test="${w.po eq null }">
	                    	*</c:if>
	                    </td>
	                    <td class="classRow">
	                   	 	<c:out value="${w.delivered}" />
	                   	 	<c:if test="${w.po eq null }">
	                    	<x:display name="${w.dateDelivered.absoluteName}" /></c:if>
                    	</td>
                	</tr>
                	<c:if test="${w.po eq null }">
					   <tr>
					       <td class="classRowLabel" valign="top" align="right">
					       		
					       	</td>
					       <td class="classRow">
					       		<x:display name="${w.btnPanel.absoluteName}" /> 
					       </td>
					   </tr>
				    </c:if>
				   <jsp:include page="../form_footer.jsp" flush="true"/>
				</table>
        		</td>
			</tr>
		</table>
	</td>
</tr>
</table>
<p></p>
<c:if test="${w.po ne null }">
<jsp:include page="/ekms/purchaseordering/closedPurchaseOrder.jsp" flush="true"/>
</c:if>