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
        			<th width="5%">S.No</th>
        			<th><fmt:message key="fms.label.groupAssignmentCode"/></th>
        			<th><fmt:message key="fms.facility.label.requiredFrom"/></th>
        			<th><fmt:message key="fms.facility.label.requiredTo"/></th>
					<th><fmt:message key="fms.request.label.requestType"/></th>
					<th><fmt:message key="fms.facility.label.assignmentId"/></th>
        			<th><fmt:message key="fms.request.label.leaveType"/></th>
        			<th><fmt:message key="fms.facility.form.itemBarcode"/></th>
        			<th><fmt:message key="fms.facility.table.checkOutDetails"/></th>
        			<th><fmt:message key="fms.facility.table.checkInDetails"/></th>
        			<th><fmt:message key="fms.ratecard.table.label.status"/></th>
        		</tr>
        		<c:forEach items="${widget.childItems}" var="assignment" varStatus="stat">
					<c:choose>
	        			<c:when test="${assignment.assignmentId ne '-'}">
			        		<tr>
								<c:choose>
				        			<c:when test="${assignment.assignmentId eq '-'}">
				        				<c:set var="className" value="textsmallRedPrint"/>
				        			</c:when>
									<c:otherwise>
										<c:set var="className" value=""/>
									</c:otherwise>
								</c:choose>
								<td class="<c:out value="${className}" />" align="right"><c:out value="${stat.index+1}" /></td>
								<td class="<c:out value="${className}" />" align="right"><c:out value="${assignment.groupId}" /></td>
								<td class="<c:out value="${className}" />" align="right"><fmt:formatDate pattern="dd MMM yyyy" value="${assignment.requiredFrom}"/></td>
								<td class="<c:out value="${className}" />" align="right"><fmt:formatDate pattern="dd MMM yyyy" value="${assignment.requiredTo}"/></td>						
								<td class="<c:out value="${className}" />" align="left">
									<c:choose>
										<c:when test="${empty assignment.serviceTypeLabel}">
											<fmt:message key="fms.label.extraCheckOut"/>
										</c:when>
										<c:otherwise>
											<c:out value="${assignment.serviceTypeLabel}" />
										</c:otherwise>
									</c:choose>
								</td>
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
			        			<td class="<c:out value="${className}" />"><c:out value="${assignment.barcode}" /></td>
			        			<td class="<c:out value="${className}" />"><c:if test="${not empty assignment.checkedOutByFullName}">
			        					<c:out value="${assignment.checkedOutByFullName}" /> [<fmt:formatDate value="${assignment.checkedOutDate}"  pattern="${globalDateLong}"/>]
			        				</c:if>
			        			</td>
			        			<td class="<c:out value="${className}" />">
			        				<c:if test="${not empty assignment.checkedInByFullName }">
			        					<c:out value="${assignment.checkedInByFullName}" /> [<fmt:formatDate value="${assignment.checkedInDate}"  pattern="${globalDateLong}"/>]
			        				</c:if>
			        			</td>
			        			<td class="<c:out value="${className}" />"><c:out value="${assignment.statusLabel}" /></td>
			        		</tr>
						</c:when>
						<c:otherwise>
							<c:set var="isAnyExtraCheckout" value="true" />
						</c:otherwise>
					</c:choose>
        		</c:forEach>
        	</table>
        	<!-- fmt:message key="fms.msg.assignment.extraItems"/--><br/>
			<c:if test="${isAnyExtraCheckout eq 'true' }">
			<b>Extra check out item(s)</b><br />
			    <table width="100%" class="borderTable">
	        		<tr>
	        			<th width="5%">S.No</th>
	        			<th><fmt:message key="fms.label.groupAssignmentCode"/></th>
        				<th><fmt:message key="fms.facility.label.requiredFrom"/></th>
        				<th><fmt:message key="fms.facility.label.requiredTo"/></th>
	        			<th><fmt:message key="fms.facility.table.itemName"/></th>
						<th><fmt:message key="fms.request.label.leaveType"/></th>
	        			<th><fmt:message key="fms.facility.form.itemBarcode"/></th>
	        			<th><fmt:message key="fms.facility.table.checkOutDetails"/></th>
	        			<th><fmt:message key="fms.facility.table.checkInDetails"/></th>
	        			<th><fmt:message key="fms.ratecard.table.label.status"/></th>
	        		</tr>
					<c:set var="idx" value ="0"/>
					<c:forEach items="${widget.childItems}" var="assignment2" varStatus="stat2">
					<c:choose>
	        			<c:when test="${assignment2.assignmentId eq '-'}">
							<c:set var="idx" value="${idx + 1}"/>
			        		<tr>								
								<td class="<c:out value="${className}" />" align="right"><c:out value="${idx}" /></td>	
								<td class="<c:out value="${className}" />" align="right"><c:out value="${assignment2.groupId}" /></td>
								<td class="<c:out value="${className}" />" align="right"><fmt:formatDate pattern="dd MMM yyyy" value="${assignment2.requiredFrom}"/></td>
								<td class="<c:out value="${className}" />" align="right"><fmt:formatDate pattern="dd MMM yyyy" value="${assignment2.requiredTo}"/></td>					
			        			<td class="<c:out value="${className}" />"><c:out value="${assignment2.facilityName}" /></td>
								<td class="<c:out value="${className}" />">
									<c:choose>
										<c:when test="${not empty assignment2.rateCardCategoryName}">
											<c:out value="${assignment2.rateCardCategoryName}" />
										</c:when>
										<c:otherwise>
											<fmt:message key="fms.facility.label.notInRateCard"/>
										</c:otherwise>
									</c:choose>
								</td>
			        			<td class="<c:out value="${className}" />"><c:out value="${assignment2.barcode}" /></td>
			        			<td class="<c:out value="${className}" />"><c:if test="${not empty assignment2.checkedOutByFullName}">
			        					<c:out value="${assignment2.checkedOutByFullName}" /> [<fmt:formatDate value="${assignment2.checkedOutDate}"  pattern="${globalDateLong}"/>]
			        				</c:if>
			        			</td>
			        			<td class="<c:out value="${className}" />">
			        				<c:if test="${not empty assignment2.checkedInByFullName }">
			        					<c:out value="${assignment2.checkedInByFullName}" /> [<fmt:formatDate value="${assignment2.checkedInDate}"  pattern="${globalDateLong}"/>]
			        				</c:if>
			        			</td>
			        			<td class="<c:out value="${className}" />"><c:out value="${assignment2.statusLabel}" /></td>
			        		</tr>
						</c:when>
					</c:choose>
        		</c:forEach>
				</table>
			</c:if>
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
            <x:display name="${widget.cancel.absoluteName}"/>
    	</td>
    </tr>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
