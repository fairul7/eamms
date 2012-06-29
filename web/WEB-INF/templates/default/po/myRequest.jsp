<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<jsp:include page="/WEB-INF/templates/ekms/form_header.jsp" />
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
							<fmt:message key="po.label.po"/> > <fmt:message key="myRequest.label.viewRequest"/>
						</td>
	                </tr>
	               
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.requester'/>
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.txtrequester.absoluteName}" />   
                    	</td>
                	</tr>
                	
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	 <fmt:message key='po.label.date'/>
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.txtDate.absoluteName}" /> 
                    	</td>
                	</tr>
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.purchaseItem'/>
	                    </td>
	                    <td width="70%" class="classRow">
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
	                    	<fmt:message key='po.global.priority'/>
	                    </td>
	                    <td class="classRow">
	                    	<c:out value="${w.priority}" /> 
                    	</td>
                	</tr>
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.reason'/>
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.txtPReason.absoluteName}" /> 
                    	</td>
                	</tr>
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.needed'/>
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.txtNeededBy.absoluteName}" />  
                    	</td>
                	</tr>
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='myRequest.label.status'/>
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.txtStatus.absoluteName}" />  
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
				   								<li><a href="/ekms/po/downloadFile?attachID=<c:out value="${a.value}&type=purchase" />"><c:out value="${a.key}" /></a></li>
									</c:forEach>
								</div>  
	                    </td>
	                </tr>
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<x:display name="${w.remarks.absoluteName}" />  
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.txtRemarks.absoluteName}" />  
                    	</td>
                	</tr>
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.resubmit.absoluteName}" />
	                    	<x:display name="${w.withdraw.absoluteName}" />
	                    	<x:display name="${w.cancel.absoluteName}" />
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
<p></p>
<c:set var="counting" value="1"/>
<c:forEach items="${w.approvedSupplier}" var="approvedSupplier" varStatus = "status">
<c:set var="rejectedSupplier" value="${w.rejectedSupplier[status.index]}"/>
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
							<fmt:message key='po.label.approved'/> <fmt:message key='supplier.label.supp'/>(s)
						</td>
			        </tr>
			        <tr>
			            <td class="contentBgColor" width="25%" valign="top" align="left" colspan="2">
						   <x:display name="${approvedSupplier.absoluteName}" />
						</td>
			        </tr>
			         <tr>
			            <td height="22" class="contentTitleFont" colspan="2" >
							<fmt:message key='po.label.rejected'/> <fmt:message key='supplier.label.supp'/>(s)
						</td>
			        </tr>
			        <tr>
			            <td class="contentBgColor" width="25%" valign="top" align="left" colspan="2">
						   <x:display name="${rejectedSupplier.absoluteName}" />
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
