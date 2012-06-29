<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>

<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
	
	<tr>
		<td width="25%" style="font-size:10pt;" nowrap class="profileRow" align="left">
			<b><fmt:message key="fms.facility.table.storeLocation"/></b>
		</td>
		<td width="75%" style="font-size:10pt;" class="profileRow">:&nbsp;<c:out value="${widget.assignment.storeLocation}" />
		</td>
	</tr>
    <tr>
        <td width="25%" style="font-size:10pt;" nowrap class="profileRow" align="left"><b><fmt:message key="fms.facility.form.date"/></b>&nbsp;</td>
        <td width="75%" style="font-size:10pt;" class="profileRow">:&nbsp;<fmt:formatDate value="${widget.checkedOutDate}" pattern="${globalDatetimeLong}"/></td>
    </tr>
	<tr>
        <td width="25%" style="font-size:10pt;" nowrap class="profileRow" align="left"><b><fmt:message key="fms.request.label.requestTitle"/></b>&nbsp;</td>
        <td width="75%" style="font-size:10pt;" class="profileRow">:&nbsp;<c:out value="${widget.request.title}"/></td>
    </tr>
	<tr>
        <td width="25%" style="font-size:10pt;" nowrap class="profileRow" align="left"><b><fmt:message key="fms.request.label.requestId"/></b>&nbsp;</td>
        <td width="75%" style="font-size:10pt;" class="profileRow">:&nbsp;<c:out value="${widget.requestId}"/></td>
    </tr>
    <tr>
        <td width="25%" style="font-size:10pt;" nowrap class="profileRow" align="left"><b><fmt:message key="fms.facility.form.requestor"/></b>&nbsp;</td>
        <td width="75%" style="font-size:10pt;" class="profileRow">:&nbsp;<c:out value="${widget.request.createdUserName}"/></td>
    </tr>
    <!--tr>
        <td width="25%" nowrap class="profileRow" align="left"><b><fmt:message key="fms.request.label.requestType"/></b>&nbsp;</td>
        <td width="75%" class="profileRow">:&nbsp;<%--c:out value="${widget.assignment.serviceTypeLabel}"/--%></td>
    </tr>
    <tr>
        <td width="25%" nowrap class="profileRow" align="left" valign="top"><b><fmt:message key="fms.label.groupAssignmentCode"/></b>&nbsp;</td>
        <td width="75%" class="profileRow" valign="top">:&nbsp;<%--c:out value="${widget.assignment.groupId}"/--%></td>
    </tr-->
    <tr>
        <td width="25%" style="font-size:10pt;" nowrap class="profileRow" align="left" height="40" valign="top"><b><fmt:message key="fms.facility.form.assignmentLocation"/></b>&nbsp;</td>
        <td width="75%" style="font-size:10pt;" class="profileRow" valign="top">:&nbsp;<c:out value="${widget.assignment.assignmentLocation}"/></td>
    </tr>
    <tr>
        <td width="25%" style="font-size:10pt;" nowrap class="profileRow" valign="top" align="left"><b><fmt:message key="fms.request.label.itemListCheckOut"/></b>&nbsp;</td>
        <td width="75%" style="font-size:10pt;" class="profileRow" align="right">
		</td>
	</tr>
</table>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    
	<tr>
        <td class="profileRow" align="left" colspan="2">
			<c:set var="isAnyExtraCheckout" value="false" />
        	<table width="100%" class="borderTable">
        		<tr>
        			<th style="font-size:10pt;" >S.No</th>
        			<!-- <th style="font-size:10pt;" ><fmt:message key="fms.label.groupAssignmentCode"/></th> -->
        			<th style="font-size:10pt;" ><fmt:message key="fms.facility.label.requiredFrom"/></th>
        			<th style="font-size:10pt;" ><fmt:message key="fms.facility.label.requiredTo"/></th>
					<th style="font-size:10pt;" ><fmt:message key="fms.request.label.requestType"/></th>
					<!-- <th style="font-size:10pt;" ><fmt:message key="fms.facility.label.assignmentId"/></th> -->
        			<th style="font-size:10pt;" ><fmt:message key="fms.request.label.leaveType"/></th>
        			<th style="font-size:10pt;" ><fmt:message key="fms.facility.form.itemBarcode"/></th>
        			<th style="font-size:10pt;" ><fmt:message key="fms.facility.table.checkOutDate"/></th>
        			<!-- <th style="font-size:10pt;" ><fmt:message key="fms.facility.table.checkInDetails"/></th>
        			<th style="font-size:10pt;" ><fmt:message key="fms.ratecard.table.label.status"/></th>
        			<th style="font-size:10pt;" ><fmt:message key="fms.facility.label.action"/></th> -->
        		</tr>
        		<c:forEach items="${widget.requestItemsList}" var="itemsList" varStatus="stat">
					<c:choose>
	        			<c:when test="${itemsList.assignmentId ne '-'}">
			        		<tr>
								<c:choose>
				        			<c:when test="${itemsList.assignmentId eq '-'}">
				        				<c:set var="className" value="textsmallRedPrint"/>
				        			</c:when>
									<c:otherwise>
										<c:set var="className" value=""/>
									</c:otherwise>
								</c:choose>
								<td style="font-size:10pt;" class="<c:out value="${className}" />" align="right"><c:out value="${stat.index+1}" /></td>
								<!-- <td style="font-size:10pt;" class="<c:out value="${className}" />" align="right"><c:out value="${itemsList.groupId}" /></td> -->
								<td style="font-size:10pt;" class="<c:out value="${className}" />" align="right"><fmt:formatDate pattern="dd MMM yyyy" value="${itemsList.requiredFrom}"/></td>
								<td style="font-size:10pt;" class="<c:out value="${className}" />" align="right"><fmt:formatDate pattern="dd MMM yyyy" value="${itemsList.requiredTo}"/></td>						
								<td style="font-size:10pt;" class="<c:out value="${className}" />" align="left">
									<c:choose>
										<c:when test="${empty itemsList.serviceTypeLabel}">
											<fmt:message key="fms.label.extraCheckOut"/>
										</c:when>
										<c:otherwise>
											<c:out value="${itemsList.serviceTypeLabel}" />
										</c:otherwise>
									</c:choose>
								</td>
								<!-- <td class="<c:out value="${className}" />">
									<c:choose>
										<c:when test="${empty itemsList.code}">
											<fmt:message key="fms.label.extraCheckOut"/>
										</c:when>
										<c:otherwise>
											<c:out value="${itemsList.code}" />
										</c:otherwise>
									</c:choose>
								</td>	         -->			
			        			<td style="font-size:10pt;" class="<c:out value="${className}" />"><c:out value="${itemsList.rateCardCategoryName}" /></td>
			        			<td style="font-size:10pt;" class="<c:out value="${className}" />"><c:out value="${itemsList.barcode}" /></td>
			        			<td style="font-size:10pt;" class="<c:out value="${className}" />">
			        				<fmt:formatDate value="${itemsList.checkedOutDate}"  pattern="${globalDatetimeLong}"/>
			        			</td>
			        		</tr>
						</c:when>
						<c:otherwise>
							<c:set var="isAnyExtraCheckout" value="true" />
						</c:otherwise>
					</c:choose>
        		</c:forEach>
        	</table>
        	<!-- fmt:message key="fms.msg.assignment.extraItems"/--><br/>
			
			<b>Extra check out item(s)</b><br />
			    <table width="100%" class="borderTable">
	        		<tr>
	        			<th style="font-size:10pt;" >S.No</th>
	        			<!-- <th style="font-size:10pt;" ><fmt:message key="fms.label.groupAssignmentCode"/></th> -->
        				<th style="font-size:10pt;" ><fmt:message key="fms.facility.label.requiredFrom"/></th>
        				<th style="font-size:10pt;" ><fmt:message key="fms.facility.label.requiredTo"/></th>
	        			<th style="font-size:10pt;" ><fmt:message key="fms.facility.table.itemName"/></th>
						<th style="font-size:10pt;" ><fmt:message key="fms.request.label.leaveType"/></th>
	        			<th style="font-size:10pt;" ><fmt:message key="fms.facility.form.itemBarcode"/></th>
	        			<th style="font-size:10pt;" ><fmt:message key="fms.facility.table.checkOutDate"/></th>
	        			<!-- <th style="font-size:10pt;" ><fmt:message key="fms.facility.table.checkInDetails"/></th>
	        			<th style="font-size:10pt;" ><fmt:message key="fms.ratecard.table.label.status"/></th>
	        			<th style="font-size:10pt;" ><fmt:message key="fms.facility.label.action"/></th> -->
	        		</tr>
					<c:set var="idx" value ="0"/>
					<c:forEach items="${widget.requestExtraItemsList}" var="extraItemsList" varStatus="stat2">
					
							<c:set var="idx" value="${idx + 1}"/>
			        		<tr>								
								<td style="font-size:10pt;" class="<c:out value="${className}" />" align="right"><c:out value="${idx}" /></td>	
								<!-- <td style="font-size:10pt;" class="<c:out value="${className}" />" align="right"><c:out value="${extraItemsList.groupId}" /></td> -->
								<td style="font-size:10pt;" class="<c:out value="${className}" />" align="right"><fmt:formatDate pattern="dd MMM yyyy" value="${extraItemsList.requiredFrom}"/></td>
								<td style="font-size:10pt;" class="<c:out value="${className}" />" align="right"><fmt:formatDate pattern="dd MMM yyyy" value="${extraItemsList.requiredTo}"/></td>					
			        			<td style="font-size:10pt;" class="<c:out value="${className}" />"><c:out value="${extraItemsList.facilityName}" /></td>
								<td style="font-size:10pt;" class="<c:out value="${className}" />">
									<c:choose>
										<c:when test="${not empty extraItemsList.rateCardCategoryName}">
											<c:out value="${extraItemsList.rateCardCategoryName}" />
										</c:when>
										<c:otherwise>
											<fmt:message key="fms.facility.label.notInRateCard"/>
										</c:otherwise>
									</c:choose>
								</td>
			        			<td style="font-size:10pt;" class="<c:out value="${className}" />"><c:out value="${extraItemsList.barcode}" /></td>
			        			<td style="font-size:10pt;" class="<c:out value="${className}" />">
			        				<fmt:formatDate value="${extraItemsList.checkedOutDate}"  pattern="${globalDatetimeLong}"/>
			        			</td>
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
			        	<td style="font-size:10pt;" class="profileRow" align="left" valign="top"><b><fmt:message key="fms.facility.table.preparedBy"/></b>&nbsp;</td>
        				<td style="font-size:10pt;"  class="profileRow" valign="top">:.............................................................................</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td style="font-size:10pt;" >
							<c:if test="${!empty widget.assignment.preparedBy && widget.assignment.preparedBy ne ''}">
								(<c:out value="${widget.assignment.preparedBy}"/>)
							</c:if>
						</td>
					</tr>
        		</table>
        	</td>
        	
        	<td align="right">
        		
        		<table>
        			<tr>
			        	<td style="font-size:10pt;" class="profileRow" align="left" valign="top"><b><fmt:message key="fms.facility.table.takenBy"/></b>&nbsp;</td>
			        	<td style="font-size:10pt;" class="profileRow" valign="top">:.............................................................................</td>
			    	</tr>
			    	<tr>
						<td>&nbsp;</td>
						<td style="font-size:10pt;">
							<c:if test="${!empty widget.assignment.takenBy && widget.assignment.takenBy ne ''}">
								(<c:out value="${widget.assignment.takenBy}"/>)
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
						<td style="font-size:10pt;" class="profileRow" align="left"><b><fmt:message key="fms.facility.table.issuedBy"/></b></td>
						<td style="font-size:10pt;" class="profileRow">:.............................................................................</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td style="font-size:10pt;" >
							<c:if test="${!empty widget.userLogin && widget.userLogin ne ''}">
								(<c:out value="${widget.userLogin}"/>)
							</c:if>
						</td>
					</tr>
        		</table>
        	</td>
    	</tr>

		
    </tr>
    <tr>
    	<td colspan="2" align="center"></td>
    </tr>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
