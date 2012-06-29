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
	                <tr valign="middle">
						<td height="22" class="contentTitleFont" colspan="2">
							<fmt:message key="po.label.prePurchase"/> > <fmt:message key="supplier.label.view"/>
						</td>
					</tr>
	                <tr>
	                    <td  width="20%" class="classRowLabel" valign="top" align="right">
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
	                    	<fmt:message key='supplier.label.quotation'/>
	                    </td>
	                    <td class="classRow">
	                    	<c:out value= "${w.strQuotation }"/>  
	                    </td>
                	</tr>
                	<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='supplier.label.minBudget'/>*
	                    </td>
	                    <td class="classRow">
	                    	 RM <c:out value= "${w.strMinBudget}"/>  
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
			   							<li><a href="/ekms/po/downloadFile?attachID=<c:out value="${a.value}&type=supplier" />"><c:out value="${a.key}" /></a></li>
								</c:forEach>
							</div>
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

<table width="100%" border="0" cellspacing="1" cellpadding="3">
