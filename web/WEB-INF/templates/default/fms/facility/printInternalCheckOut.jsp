<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/> 

 <jsp:include page="../../form_header.jsp" flush="true"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
<tr>
        <td width="25%" nowrap class="profileRow" style="font-size:10pt;" align="left"><b><fmt:message key="fms.facility.table.storeLocation"/></b>&nbsp;</td>
        <td width="75%" class="profileRow" style="font-size:10pt;">:&nbsp;<c:out value="${widget.fo.storeLocation}"/></td>
    </tr>
	<tr>
        <td width="25%" nowrap class="profileRow" style="font-size:10pt;" align="left"><b><fmt:message key="fms.facility.form.date"/></b>&nbsp;</td>
        <td width="75%" class="profileRow" style="font-size:10pt;">:&nbsp;<fmt:formatDate value="${widget.checkedOutDate}" pattern="${globalDatetimeLong}"/></td>
    </tr>
	<tr>
        <td width="25%" nowrap class="profileRow" style="font-size:10pt;" align="left" valign="top">
				<b><fmt:message key="fms.facility.form.requestor"/></b>&nbsp;</td>
        	<td width="75%" class="profileRow" style="font-size:10pt;" valign="top">:&nbsp;<c:out value="${widget.fo.checkout_by}"/></td>
    	</tr>
	<tr>
        <td width="25%" nowrap class="profileRow" style="font-size:10pt;" align="left" valign="top">
				<b><fmt:message key="fms.facility.form.assignmentLocation"/></b>&nbsp;</td>
        	<td width="75%" class="profileRow" style="font-size:10pt;" valign="top">:&nbsp;<c:out value="${widget.fo.location}"/></td>
    	</tr>
		<tr>
        	<td width="25%" nowrap class="profileRow" style="font-size:10pt;" align="left" valign="top">
				<b><fmt:message key="fms.facility.form.purpose"/></b>&nbsp;</td>
        	<td width="75%" class="profileRow" style="font-size:10pt;" valign="top">:&nbsp;<c:out value="${widget.fo.purpose}"/></td>
    	</tr>
    <tr>
        <td width="25%" nowrap class="profileRow" style="font-size:10pt;" valign="top" align="left"><b><fmt:message key="fms.request.label.itemListCheckOut"/></b>&nbsp;</td>
        <td width="75%" class="profileRow"  style="font-size:10pt;" align="right">
		</td>
	</tr>
</table>

<table class="profileTable" cellpadding="3" cellspacing="1" width="100%" >
   
     
	<tr>
		<td nowrap class="profileRow" style="font-size:10pt;" valign="top" align="right" colspan="2">
        	<table width="100%" class="borderTable">
        		<tr>
        			<th style="font-size:10pt;"width="5%">No</th>
        			<th style="font-size:10pt;"width="25%"><fmt:message key="fms.facility.form.assetName"/></th>
        			<!-- <th><fmt:message key="fms.facility.form.fatCodeNo"/></th> -->
        			    <th style="font-size:10pt;"width="25%"><fmt:message key="fms.facility.form.facCodeNo"/></th>
						<th style="font-size:10pt;"><fmt:message key="fms.facility.label.remarks"/>
						<!-- <th style="font-size:10pt;"><fmt:message key="fms.facility.form.itemBarcode"/></th> -->
						<!-- <th style="font-size:10pt;"><fmt:message key="fms.facility.form.checkList"/></th>
	        			<!--  <th style="font-size:10pt;"><fmt:message key="fms.facility.table.checkOutDetails"/></th>
	        			<!--  <th style="font-size:10pt;"><fmt:message key="fms.facility.table.checkInDetails"/></th>
	        			<!--  <th style="font-size:10pt;"><fmt:message key="fms.ratecard.table.label.status"/></th>
	        			<!--  <th style="font-size:10pt;"><fmt:message key="fms.facility.label.action"/></th> -->
        		</tr>
        		<c:forEach items="${widget.childItems}" var="assignment" varStatus="stat">
	        		<tr>
	        			<td style="font-size:10pt;" class="<c:out value="${className}" />" align="right"><c:out value="${stat.index+1}" /></td>
	        			<td style="font-size:10pt;" class="<c:out value="${className}" />"><c:out value="${assignment.name}" /></td>
	        			<td style="font-size:10pt;" class="<c:out value="${className}" />"><c:out value="${assignment.barcode}" /></td>
	        			<td class="<c:out value="${className}" />">&nbsp;</td>
	        			<!-- <td style="font-size:10pt;" class="<c:out value="${className}" />">
	        			<td style="font-size:10pt;" class="<c:out value="${className}" />"><c:if test="${not empty assignment.checkout_by}">
			        					<c:out value="${assignment.checkout_by}" /> [<fmt:formatDate value="${assignment.checkout_date}"  pattern="${globalDateLong}"/>]
			        				</c:if>
			        			</td>
			        			<td style="font-size:10pt;" class="<c:out value="${className}" />">
			        				<c:if test="${not empty assignment.checkin_by }">
			        					<c:out value="${assignment.checkin_by}" /> [<fmt:formatDate value="${assignment.checkin_date}"  pattern="${globalDateLong}"/>]
			        				</c:if>
			        			</td>
			        			<td style="font-size:10pt;" class="<c:out value="${className}" />"><c:choose>
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
				        				</c:choose></td>
						<td class="<c:out value="${className}" />">
	        			</td>
	        			 -->
						
	        		</tr>
        		</c:forEach>
        	</table>
        </td>
    </tr>
	 <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
		<tr>
			<td valign="top">
        		<table>
        			<tr>
			        	<td class="profileRow" style="font-size:10pt;" align="left" valign="top"><b><fmt:message key="fms.facility.table.preparedBy"/></b>&nbsp;</td>
        				<td class="profileRow" style="font-size:10pt;" valign="top">:.............................................................................</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td style="font-size:10pt;">
							<c:if test="${!empty widget.fo.preparedBy && widget.fo.preparedBy ne ''}">
								(<c:out value="${widget.fo.preparedBy}"/>)
							</c:if>
						</td>
					</tr>
        		</table>
        	</td>
        	
        	<td align="right">
        		
        		<table>
        			<tr>
			        	<td class="profileRow" style="font-size:10pt;" align="left" valign="top"><b><fmt:message key="fms.facility.table.takenBy"/></b>&nbsp;</td>
			        	<td class="profileRow" style="font-size:10pt;" valign="top">:.............................................................................</td>
			    	</tr>
			    	<tr>
						<td>&nbsp;</td>
						<td style="font-size:10pt;">
							<c:if test="${!empty widget.fo.takenBy && widget.fo.takenBy ne ''}">
								(<c:out value="${widget.fo.takenBy}"/>)
							</c:if>
						</td>
					</tr>
					
					<tr>
						<td style="font-size:10pt;" class="profileRow" align="left"><b>Staff ID</b></td>
						<td style="font-size:10pt;" class="profileRow">:.............................................................................</td>
					</tr>
					
					<tr><td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td></tr>
					<tr>
						<td class="profileRow" style="font-size:10pt;"align="left"><b><fmt:message key="fms.facility.table.issuedBy"/></b></td>
						<td class="profileRow" style="font-size:10pt;" >:.............................................................................</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td style="font-size:10pt;">
							<c:if test="${!empty widget.userLogin && widget.userLogin ne ''}">
								(<c:out value="${widget.userLogin}"/>)
							</c:if>
						</td>
					</tr>
        		</table>
        	</td>
    	</tr>
    
    <tr>
    	<td colspan="2" align="center"></td>
    </tr>

    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>

