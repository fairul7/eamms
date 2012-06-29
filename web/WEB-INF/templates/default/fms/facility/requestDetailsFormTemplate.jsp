
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../../form_header.jsp" flush="true"/>
    <tr>
        <td width="25%" nowrap class="profileRow" align="right" height="25"><b><fmt:message key="fms.request.label.requestTitle"/></b>&nbsp;</td>
        <td width="75%" class="profileRow"><c:out value="${widget.request.title}"/></td>
    </tr>
    <tr>
        <td nowrap class="profileRow" align="right" height="25"><b><fmt:message key="fms.facility.form.requestor"/></b>&nbsp;</td>
        <td class="profileRow"><c:out value="${widget.request.createdUserName}"/></td>
    </tr>
	<tr>
        <td nowrap class="profileRow" align="right" height="25"><b><fmt:message key="fms.facility.label.remarks"/></b>&nbsp;</td>
        <td class="profileRow"><c:out value="${widget.request.description}" escapeXml="false"/></td>
    </tr>	
    <tr>
        <td nowrap class="profileRow" valign="top" align="right" height="30"><b><fmt:message key="fms.request.label.requestedItem"/></b>&nbsp;</td>
		<td></td>
	</tr>
	<tr>
        <td class="profileRow" align="left" colspan="2">
			<c:set var="isAnyExtraCheckout" value="false" />
        	<table width="100%" class="borderTable">
        		<tr>
        			<th>S.No</th>
        			<!-- <th><fmt:message key="fms.label.groupAssignmentCode"/></th> -->
        			<th><fmt:message key="fms.facility.label.requiredFrom"/></th>
        			<th><fmt:message key="fms.facility.label.requiredTo"/></th>
					<th><fmt:message key="fms.request.label.requestType"/></th>
					<th><fmt:message key="fms.facility.label.assignmentId"/></th>
        			<th><fmt:message key="fms.request.label.leaveType"/></th>
        			<th><fmt:message key="fms.facility.form.itemBarcode"/></th>
        			<th><fmt:message key="fms.facility.table.checkOutDetails"/></th>
        			<th><fmt:message key="fms.facility.table.checkInDetails"/></th>
        			<th><fmt:message key="fms.ratecard.table.label.status"/></th>
        			<th><fmt:message key="fms.facility.label.action"/></th>
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
								<td class="<c:out value="${className}" />" align="right"><c:out value="${stat.index+1}" /></td>
								<!-- <td class="<c:out value="${className}" />" align="right"><c:out value="${itemsList.groupId}" /></td> -->
								<td class="<c:out value="${className}" />" align="right"><fmt:formatDate pattern="dd MMM yyyy" value="${itemsList.requiredFrom}"/></td>
								<td class="<c:out value="${className}" />" align="right"><fmt:formatDate pattern="dd MMM yyyy" value="${itemsList.requiredTo}"/></td>						
								<td class="<c:out value="${className}" />" align="left">
									<c:choose>
										<c:when test="${empty itemsList.serviceTypeLabel}">
											<fmt:message key="fms.label.extraCheckOut"/>
										</c:when>
										<c:otherwise>
											<c:out value="${itemsList.serviceTypeLabel}" />
										</c:otherwise>
									</c:choose>
								</td>
								<td class="<c:out value="${className}" />">
									<c:choose>
										<c:when test="${empty itemsList.code}">
											<fmt:message key="fms.label.extraCheckOut"/>
										</c:when>
										<c:otherwise>
											<c:out value="${itemsList.code}" />
										</c:otherwise>
									</c:choose>
								</td>	        			
			        			<td class="<c:out value="${className}" />"><c:out value="${itemsList.rateCardCategoryName}" /></td>
			        			<td class="<c:out value="${className}" />"><c:out value="${itemsList.barcode}" /></td>
			        			<td class="<c:out value="${className}" />"><c:if test="${not empty itemsList.checkedOutByFullName}">
			        					<c:out value="${itemsList.checkedOutByFullName}" /> [<fmt:formatDate value="${itemsList.checkedOutDate}"  pattern="${globalDateLong}"/>]
			        				</c:if>
			        			</td>
			        			<td class="<c:out value="${className}" />">
			        				<c:if test="${not empty itemsList.checkedInByFullName }">
			        					<c:out value="${itemsList.checkedInByFullName}" /> [<fmt:formatDate value="${itemsList.checkedInDate}"  pattern="${globalDateLong}"/>]
			        				</c:if>
			        			</td>
			        			<td class="<c:out value="${className}" />"><c:out value="${itemsList.statusLabel}" /></td>
			        			<td class="<c:out value="${className}" />">
			        				<c:if test="${itemsList.statusLabel eq 'Prepare Check Out'}">
			        					<a href="requestDetails.jsp?page=${param.page}&requestId=${param.requestId}&assignmentId=${itemsList.assignmentId}&action=remove">Remove</a>
			        				</c:if>
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
	        			<th>S.No</th>
	        			<!-- <th><fmt:message key="fms.label.groupAssignmentCode"/></th> -->
        				<th><fmt:message key="fms.facility.label.requiredFrom"/></th>
        				<th><fmt:message key="fms.facility.label.requiredTo"/></th>
	        			<%--<th><fmt:message key="fms.facility.table.itemName"/></th>--%>
						<th><fmt:message key="fms.request.label.leaveType"/></th>
	        			<th><fmt:message key="fms.facility.form.itemBarcode"/></th>
	        			<th><fmt:message key="fms.facility.table.checkOutDetails"/></th>
	        			<th><fmt:message key="fms.facility.table.checkInDetails"/></th>
	        			<th><fmt:message key="fms.ratecard.table.label.status"/></th>
	        			<th><fmt:message key="fms.facility.label.action"/></th>
	        		</tr>
					<c:set var="idx" value ="0"/>
					<c:forEach items="${widget.requestExtraItemsList}" var="extraItemsList" varStatus="stat2">
					
							<c:set var="idx" value="${idx + 1}"/>
			        		<tr>								
								<td class="<c:out value="${className}" />" align="right"><c:out value="${idx}" /></td>	
								<!-- <td class="<c:out value="${className}" />" align="right"><c:out value="${extraItemsList.groupId}" /></td> -->
								<td class="<c:out value="${className}" />" align="right"><fmt:formatDate pattern="dd MMM yyyy" value="${extraItemsList.requiredFrom}"/></td>
								<td class="<c:out value="${className}" />" align="right"><fmt:formatDate pattern="dd MMM yyyy" value="${extraItemsList.requiredTo}"/></td>					
			        			<%--<td class="<c:out value="${className}" />"><c:out value="${extraItemsList.facilityName}" /></td>--%>
								<td class="<c:out value="${className}" />">
									<c:choose>
										<c:when test="${not empty extraItemsList.rateCardCategoryName}">
											<c:out value="${extraItemsList.rateCardCategoryName}" />
										</c:when>
										<c:otherwise>
											<fmt:message key="fms.facility.label.notInRateCard"/>
										</c:otherwise>
									</c:choose>
								</td>
			        			<td class="<c:out value="${className}" />"><c:out value="${extraItemsList.barcode}" /></td>
			        			<td class="<c:out value="${className}" />"><c:if test="${not empty extraItemsList.checkedOutByFullName}">
			        					<c:out value="${extraItemsList.checkedOutByFullName}" /> [<fmt:formatDate value="${extraItemsList.checkedOutDate}"  pattern="${globalDateLong}"/>]
			        				</c:if>
			        			</td>
			        			<td class="<c:out value="${className}" />">
			        				<c:if test="${not empty extraItemsList.checkedInByFullName }">
			        					<c:out value="${extraItemsList.checkedInByFullName}" /> [<fmt:formatDate value="${extraItemsList.checkedInDate}"  pattern="${globalDateLong}"/>]
			        				</c:if>
			        			</td>
			        			<td class="<c:out value="${className}" />"><c:out value="${extraItemsList.statusLabel}" /></td>
			        			<td class="<c:out value="${className}" />">
			        				<c:if test="${extraItemsList.statusLabel eq 'Prepare Check Out'}">
			        					<a href="requestDetails.jsp?page=${param.page}&requestId=${param.requestId}&assignmentEquipmentId=${extraItemsList.assignmentEquipmentId}&action=remove">Remove</a>
			        				</c:if>
			        			</td>
			        		</tr>
						
        		</c:forEach>
				</table>
			
        </td>
    </tr>
    
    <tr>
    	<td colspan="2" align="center"></td>
    </tr>
    <tr>
    	<td></td>
    	<td class="profileFooter" align="left">
    		<x:display name="${widget.prepareCheckout.absoluteName}"/>
            <x:display name="${widget.checkOut.absoluteName}"/>
            <x:display name="${widget.checkIn.absoluteName}"/>
			<x:display name="${widget.printCheckOut.absoluteName}"/>
            <x:display name="${widget.cancel.absoluteName}"/>
            <x:display name="${widget.notUtilized.absoluteName}"/>
    	</td>
    </tr>
    <x:display name="${widget.childMap['validateRequestId'].absoluteName}"/>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
