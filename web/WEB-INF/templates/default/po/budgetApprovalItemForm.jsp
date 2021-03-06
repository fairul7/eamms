<%@include file="/common/header.jsp" %>
<x:config>
	<page name="budgetApprovalTable">
		<com.tms.sam.po.ui.BudgetApprovalSupplierListing name="supplierListing"/>
		<com.tms.sam.po.ui.BudgetApprovalForm name="budgetApproval"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="budgetApprovalTable.budgetApproval" property="ppID" value="${param.id}" />
</c:if>

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
							<fmt:message key="po.label.po"/> > <fmt:message key="budget.label.title"/>
						</td>
					</tr>	                
	                <tr>
	                    <td width="25%" class="classRowLabel" valign="top" align="right">
	                    	<b>
            					<fmt:message key='po.label.purchaseItem'/>
           					</b>
	                    </td>
	                    <td class="classRow">
	                    <c:set var="count" value="0"/>
	                    	<table width="100%" border="0" cellspacing="1" cellpadding="3">
				           		<tr>
				           			<td  bgcolor="#BBD5F2" valign="top" align="left">
				               			<b><fmt:message key='po.label.no'/></b>
				               		</td>
				           			<td  bgcolor="#BBD5F2" valign="top" align="left">
				               			<b><fmt:message key='po.label.itemCode'/></b>
				               		</td>
				           			<td  bgcolor="#BBD5F2" valign="top">
				               			<b><fmt:message key='po.label.itemDesc'/></b>
				           			</td>
				           			<td  bgcolor="#BBD5F2" valign="top" align="left">
				               			<b><fmt:message key='po.label.qty'/></b>
				               		</td>
				           			<td  bgcolor="#BBD5F2" valign="top">
				               			<b><fmt:message key='po.label.unitOfMeasure'/></b>
				           			</td>
				           			<td  bgcolor="#BBD5F2" valign="top" align="left">
				               			<b><fmt:message key='po.label.vendor'/></b>
				               		</td>
				           		</tr>
				           		<c:forEach items="${w.itemCode}" var="itemCode" varStatus = "status">
				                  <c:set var="itemDesc" value="${w.itemDesc[status.index]}"/>
				                   <c:set var="qty" value="${w.qty[status.index]}"/>
				                    <c:set var="unitOfMeasure" value="${w.unitOfMeasure[status.index]}"/>
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
											<c:out value="${qty}" />
										</td>
										<td class="contentBgColor" width="30%" valign="top">
								   			<c:out value="${unitOfMeasure}" />
								   		</td>
									 </tr>
								</c:forEach>
				       	 	</table>
	                    </td>
	                </tr>
	                <tr>
	                   <td class="classRowLabel" valign="top" align="right">
	                   		<b>
	           					<fmt:message key='po.label.requester'/>
	        				</b>
	                   </td>
	                   <td class="classRow">
	                    	  <x:display name="${w.txtRequester.absoluteName}" />
	                   </td>
	                </tr>
	                <tr>
	                   <td class="classRowLabel" valign="top" align="right">
	                     	<b>
    	    					<fmt:message key='purchaseRequest.label.purchaseCode'/>
    	    				</b>
	                   </td>
	                   <td class="classRow">
	                    	<x:display name="${w.txtDptCode.absoluteName}" />
	                   </td>
	                </tr>
	                <tr>
	                   <td class="classRowLabel" valign="top" align="right">
	                     	<b>
          						<fmt:message key='po.label.date'/>
          					</b>
	                   </td>
	                   <td class="classRow">
	                    	<x:display name="${w.txtDate.absoluteName}" />
	                   </td>
	                </tr>
	                <tr>
	                   <td class="classRowLabel" valign="top" align="right">
	                     	<b>
          						<fmt:message key='po.label.needed'/>
          					</b>
	                   </td>
	                   <td class="classRow">
	                    	<x:display name="${w.txtNeededBy.absoluteName}" />
	                   </td>
	                </tr>
	                <tr>
	                  <td class="classRowLabel" valign="top" align="right">
	                       	<b>
          						<fmt:message key='po.label.reason'/>
          					</b>
	                   </td>
	                   <td class="classRow">
	                    	<x:display name="${w.txtReason.absoluteName}" />
	                   </td>
	                </tr>
	                <tr>
	                   <td class="classRowLabel" valign="top" align="right">
          					<x:display name="${w.attachment.absoluteName}" />
          			   </td>
	                   <td class="classRow">
	                    	<c:set var="map" value="${w.attachmentMap}" />
								<div id="attachmentItems">
									<c:forEach var="a" items="${map}">
				                   		<c:set var="key" value="${a.key}" />
				    						<li>
				    							<a href="/ekms/po/downloadFile?attachID=<c:out value="${a.value}&type=purchase" />"><c:out value="${a.key}" /></a>
				    						</li>
									</c:forEach>
				                 </div>
	                   </td>
	                </tr>
	               
				</table>
	        </td>
		</tr>
		</table>
	</td>
</tr>
</table>
<br>
<c:set var="counting" value="1"/>
<c:forEach items="${w.budgetSupplierListing}" var="budgetSupplierListing" varStatus = "status">

	<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
	<tr>
		<td>
			<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
		    <tr>
		        <td>
		        	    <c:set var="count" value="1"/>
						<table width="100%" cellpadding="3" cellspacing="1">
			                <jsp:include page="../form_header.jsp" flush="true"/>
			                <tr>
			                   	<td height="22" class="contentTitleFont" colspan="2" >
									 <fmt:message key='po.label.item'/>(s)
								</td>
			                </tr>
			                <tr>
	                    
	                    <td width="80%" class="classRow">
	                    	<c:set var="count" value="0"/>
	                    	<table width="100%" border="0" cellspacing="1" cellpadding="3">
				           		<tr>
				           			<td  bgcolor="#CCCCCC" valign="top" align="left">
				               			<b><fmt:message key='po.label.no'/></b>
				               		</td>
				           			<td  bgcolor="#CCCCCC" valign="top" align="left">
				               			<b><fmt:message key='po.label.itemCode'/></b>
				               		</td>
				           			<td  bgcolor="#CCCCCC" valign="top">
				               			<b><fmt:message key='po.label.itemDesc'/></b>
				           			</td>
				           			<td  bgcolor="#CCCCCC" valign="top" align="left">
				               			<b><fmt:message key='po.label.qty'/></b>
				               		</td>
				           			<td  bgcolor="#CCCCCC" valign="top">
				               			<b><fmt:message key='po.label.unitOfMeasure'/></b>
				           			</td>
				           			
				           		</tr>
				           		<c:forEach items="${w.supplierItemCode}" var="supplierItemCode" varStatus = "status">
				                  <c:set var="supplierItemDesc" value="${w.supplierItemDesc[status.index]}"/>
				                   <c:set var="supplierItemQty" value="${w.supplierItemQty[status.index]}"/>
				                    <c:set var="supplierItemMeasure" value="${w.supplierItemMeasure[status.index]}"/>
				                    <c:set var="supplierCount" value="${w.supplierCount[status.index]}"/>
				                    <c:if test="${supplierCount eq counting }">
								    	<tr>
									   		<td class="contentBgColor" valign="top" align="right">
									   			<c:set var="count" value="${count + 1}"/>
																	<c:out value="${count}" /> 
									   		</td>
											<td class="contentBgColor" valign="top" align="right">
												<c:out value="${supplierItemCode}" />
												<br>
											</td>
											<td class="contentBgColor" valign="top">
									   			<c:out value="${supplierItemDesc}" />
									   		</td>
											<td class="contentBgColor" valign="top" align="right">
												<c:out value="${supplierItemQty}" />
											</td>
											<td class="contentBgColor" width="30%" valign="top">
									   			<c:out value="${supplierItemMeasure}" />
									   		</td>
										 </tr>
								</c:if>
								</c:forEach>
								<p></p>
				       	 	</table>
                    	</td>
                	</tr>
			        <tr>
			            <td height="22" class="contentTitleFont" colspan="2" >
							<fmt:message key='supplier.label.supp'/>(s)
						</td>
			        </tr>
			        <tr>
			            <td class="contentBgColor" width="25%" valign="top" align="left" colspan="2">
						         <x:display name="${budgetSupplierListing.absoluteName}" />
						    	</td>
			                </tr>
		                	<jsp:include page="../form_footer.jsp" flush="true"/>
						</table>
	        	</td>
			</tr>
			</table>
		</td>
	</tr>
	
	 <c:set var="counting" value="${counting + 1}"/>
	
	</table>
	<br>
	
</c:forEach>

       

<br>
    <x:display name="budgetApprovalTable.budgetApproval"/>
    
