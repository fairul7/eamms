<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>

<!-- JSON -->
<script type='text/javascript' src="<c:out value='${pageContext.request.contextPath}/common/ajaxtoolkit/jsonrpc.js'/>"></script>
<!-- End of JSON -->


<%-- validation hidden field --%>
<x:display name="${form.childMap['validateRequestId'].absoluteName}" />

<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
	<tr valign="top">
		<td valign="top">
			<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
						
			<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
				<fmt:message key='fms.request.label.requestTitle'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.requestTitle.absoluteName}" /></td>
			</tr>
			
			<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
				<fmt:message key="fms.facility.table.checkOutBy"/>*</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.psusbCheckOutBy.absoluteName}"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
				<fmt:message key="fms.facility.table.takenBy"/>*</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.takenBy.absoluteName}"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
				<fmt:message key="fms.facility.table.preparedBy"/>*</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.preparedBy.absoluteName}"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
				<fmt:message key="fms.facility.table.assignmentLocation"/>*</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.assignmentLocation.absoluteName}"/></td>
			</tr>
			<c:forEach var="i" begin="0" end="${form.count-1}">
				<tr><td class="classRowLabel" valign="top" align="right" width="15%">
					<fmt:message key='fms.facility.form.checkOutItem'/>&nbsp;<c:out value="${i+1}"/></td>
				<td class="classRow" valign="top" width="40%">
					<x:display name="${form.absoluteName}.tfItem${i}" />
					<span id="labelItemName${i+1}">&nbsp;</span>
				</td>
				</tr>
			</c:forEach>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				&nbsp;</td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.pnButton.absoluteName}"/></td>
			</tr>
		</table>
		</td>
		<td valign="top">
			<table>
				<tr><td><STRONG><fmt:message key="fms.facility.requestedItems"/>:</STRONG></td></tr>
				<tr><td>&nbsp;</td></tr>
				<tr valign="top">
			        <td class="profileRow" align="right" colspan="2">
			        	<table width="100%" class="borderTable">
			        		<tr>
								<th><fmt:message key="fms.facility.label.assignmentId"/></th>
			        			<th><fmt:message key="fms.request.label.leaveType"/></th>
			        			<th><fmt:message key="fms.facility.form.status"/></th>
			        			
			        		</tr>
			        		<c:forEach items="${widget.requestedItemsList}" var="assignment" varStatus="stat">
				        		<tr>
									
									<td class="<c:out value="${className}" />">
										<c:choose>
											<c:when test="${empty assignment.code}">
												<fmt:message key="fms.label.extraCheckOut"/>
											</c:when>
											<c:otherwise>
												<c:out value="${assignment.code}" />
											</c:otherwise>
										</c:choose>
									</td>	        			
				        			<td class="<c:out value="${className}" />"><c:out value="${assignment.rateCardCategoryName}" /></td>
				        			<td class="<c:out value="${className}" />">
				        				<c:choose>
				        					<c:when test="${assignment.status eq 'N'}">
				        						<fmt:message key="fms.engineering.request.status.new"/>
				        					</c:when>
				        					<c:when test="${assignment.status eq 'O'}">
				        						<fmt:message key="fms.engineering.request.status.checkedOut"/>
				        					</c:when>
				        					<c:when test="${assignment.status eq 'P'}">
				        						<fmt:message key="fms.facility.prepareCheckout"/>
				        					</c:when>
				        					<c:when test="${assignment.status eq 'I'}">
				        						<fmt:message key="fms.engineering.request.status.checkedIn"/>
				        					</c:when>
				        				</c:choose>
				        				
				        			</td>
				        		</tr>
			        		</c:forEach>
			        	</table>
			        	
			        </td>
			    </tr>
			    <c:if test="${!empty widget.requestedPreparedItemsList}">
			    <tr><td>&nbsp;</td></tr>
			    <tr><td><STRONG>Prepared Check Out Item(s):</STRONG></td></tr>
			    		    
			    	
			    <c:forEach items="${widget.requestedPreparedItemsList}" var="prepareAssignment" varStatus="stat" begin="0" end="0">
			    	<tr>
			    		<td>Prepared By:</td>
			    		<td><c:out value="${prepareAssignment.prepareCheckOutBy}"/></td>
			    	</tr>
			    	<tr>
			    		<td>Date Prepared:</td>
			    		<td><fmt:formatDate pattern="dd MMM yyyy" value="${prepareAssignment.prepareCheckOutDate}"/></td>
			    	</tr>
			    	<tr valign="top">
			    		<td valign="top">Item(s):</td>
			    		
			    		<td>
			    			<c:forEach items="${widget.requestedPreparedItemsList}" var="item" varStatus="stat">
			    			<table>
			    				<tr> 
			    					<td><c:out value="${item.rateCardCategoryName}"/></td>
			    				</tr>
			    			</table>
			    			</c:forEach>	
			    		</td>
			    	</tr>	
			    </c:forEach>
			    	
				</c:if>
			    
			</table>
		</td>
	</tr>
</table>
		
<script type='text/javascript'>

function displayItem(fldobj,nbox, label) {
	
	var itemCode=document.getElementsByName(nbox)[0].value;
	jsonrpc = new JSONRpcClient("/JSON-RPC");
	var result = jsonrpc.facilityModule.itemNameByBarcode(itemCode);
	var el = document.getElementById(label);
	el.firstChild.data=result.item;

}

</script>

<jsp:include page="../../form_footer.jsp" flush="true"/>