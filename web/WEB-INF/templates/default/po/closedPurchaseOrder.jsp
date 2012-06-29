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
							<fmt:message key="po.label.paymentDetails"/>
						</td>
				   </tr>
				  
                   <tr>
				       <td width="30%" class="classRowLabel" valign="top" align="right">
				       		<fmt:message key='po.label.paid'/> 
				       		<c:if test="${w.po eq null }">
					       		*
				       		</c:if>
				       	</td>
				       <td class="classRow">
				       		<x:display name="${w.paid.absoluteName}" />
				       		
				       </td>
				   </tr>
				   <tr>
				       <td width="30%" class="classRowLabel" valign="top" align="right">
				       		<fmt:message key='po.label.refNo'/> <c:if test="${w.po eq null }">*</c:if>
				       	</td>
				       <td class="classRow">
				       		<c:out value="${w.referenceN}" />
				      	    <c:if test="${w.po eq null }">
				       		<x:display name="${w.referenceNo.absoluteName}" /></c:if>
				       </td>
				   </tr>
				   <tr>
				       <td class="classRowLabel" valign="top" align="right">
				       		<fmt:message key='po.label.beneficiary'/><c:if test="${w.po eq null }"> *</c:if>
				       	</td>
				       <td class="classRow">
				        	<c:out value="${w.bficiary}" />
				      	    <c:if test="${w.po eq null }">
				       		<x:display name="${w.beneficiary.absoluteName}" /></c:if>
				       </td>
				   </tr>
				   <tr>
				       <td class="classRowLabel" valign="top" align="right">
				       		<fmt:message key='po.label.typeOfPayment'/><c:if test="${w.po eq null }"> *</c:if>
				       	</td>
				       <td class="classRow">
				        	<c:out value="${w.type}" />
				      	    <c:if test="${w.po eq null }">
				       		<x:display name="${w.radioGroup.absoluteName}" /></c:if>
				       </td>
				   </tr>
				   <tr>
				       <td class="classRowLabel" valign="top" align="right">
				       		<fmt:message key='po.label.datePaid'/><c:if test="${w.po eq null }"> *</c:if>
				       	</td>
				       <td class="classRow">
				        	<c:out value="${w.dateP}" />
				      	    <c:if test="${w.po eq null }">
				       		<x:display name="${w.datePaid.absoluteName}" /></c:if>
				       </td>
				   </tr>
				   <tr>
				       <td class="classRowLabel" valign="top" align="right">
				       		<fmt:message key='po.label.amountPaid'/><c:if test="${w.po eq null }"> *</c:if>
				       	</td>
				       <td class="classRow">
				      	    <c:out value="${w.amountP}" />
				      	    <c:if test="${w.po eq null }">
				       		<x:display name="${w.amount.absoluteName}" /></c:if>
				       </td>
				   </tr>
				   
				   <tr>
				       <td class="classRowLabel" valign="top" align="right">
				       		
				       	</td>
				       <td class="classRow">
				       		<c:if test="${w.po eq null }">
				       			<x:display name="${w.close.absoluteName}" />
				       		</c:if> 
				     			<x:display name="${w.cancel.absoluteName}" />
				       </td>
				   </tr>
				   
				</table>
        		</td>
			</tr>
		</table>
	</td>
</tr>
</table>
 <jsp:include page="../form_footer.jsp" flush="true"/>
<p></p>

<c:if test="${w.po ne null }">
	<script>
		document.forms['purchaseOrder.cPO'].elements['purchaseOrder.cPO.paid'].disabled=true;
	</script>
</c:if>