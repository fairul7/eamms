<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../../form_header.jsp" flush="true"/>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.requestTitle"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.request.title}"/> &nbsp; 
        	<c:if test="${widget.showEditRequestLink}">
			<a href="javascript:pops('edit','editRequestDetail.jsp?requestId=${widget.requestId}',700,900)">
				<c:out value="${widget.editDetails}"/>
			</a>
			</c:if>
		</td>
    </tr>
	<tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.facility.label.requiredDate"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><fmt:formatDate value="${widget.request.requiredFrom}"  pattern="${globalDateLong}"/> - <fmt:formatDate value="${widget.request.requiredTo}"  pattern="${globalDateLong}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.requestType"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.requestTypeLabel}"/></td>
    </tr>
	<c:if test="${widget.request.requestType eq 'I'}">
	    <tr>
	        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.program"/></b>&nbsp;</td>
	        <td width="70%" class="profileRow"><c:out value="${widget.request.programName}"/></td>
	    </tr>
	</c:if>
	<c:if test="${widget.request.requestType eq 'E'}">
	    <tr>
	        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.clientName"/></b>&nbsp;</td>
	        <td width="70%" class="profileRow"><c:out value="${widget.request.clientName}"/></td>
	    </tr>
	</c:if>
    <tr>
        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.request.label.remarks"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.request.description}" escapeXml="false"/></td>
    </tr>
    <c:if test="${widget.viewMode eq 'true' or widget.fcEditMode eq 'true'}" >
    <tr>
        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.facility.label.submittedBy"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.request.createdUserName}"/> [<fmt:formatDate value="${widget.request.submittedDate}"  pattern="${globalDatetimeLong}"/>]</td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.facility.table.status"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.request.statusLabel}"/></td>
    </tr>
   	 	<c:if test="${ not empty(widget.request.approvedBy)}" >
			<tr>
		        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.facility.label.remarksDepartmentApprover"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow">
							<c:out value="${widget.request.approverRemarks}"/>
				</td>
		    </tr>
			<tr>
		        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.facility.label.approvedBy"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow">
							<c:out value="${widget.request.approvedBy}"/> [<fmt:formatDate value="${widget.request.approvedDate}"  pattern="${globalDatetimeLong}"/>]
				</td>
		    </tr>
		</c:if>
    
		<!-- c:if test="${(widget.isHod eq 'true') || (widget.isFC eq 'true') || (widget.isFCHead eq 'true')|| (widget.isHOU eq 'true')}" -->
			<tr>
		        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.facility.table.totalAmountRate"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow">
		        	<fmt:message key="fms.request.label.currency"/>&nbsp;
					<c:choose>
						<c:when test="${widget.request.requestType eq 'E'}">
							<fmt:formatNumber value="${widget.totalExternalRate}" maxFractionDigits="2" pattern="#,##0.00"/>
						</c:when>
						<c:otherwise>
							<fmt:formatNumber value="${widget.totalInternalRate}" maxFractionDigits="2" pattern="#,##0.00"/>
						</c:otherwise>						
					</c:choose>
				</td>
		    </tr>
		<!-- /c:if-->

		<c:if test="${ not empty(widget.request.cancellationCharges) }">
			<tr>
		        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.label.cancellationCharges"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow">
		        	<fmt:message key="fms.request.label.currency"/>&nbsp;
					<fmt:formatNumber value="${widget.request.cancellationCharges}" maxFractionDigits="2" pattern="#,##0.00"/>					
				</td>
		    </tr>
		</c:if>

		<c:if test="${ not empty(widget.request.lateCharges) }">
			<tr>
		        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.label.lateCharges"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow">
		        	<fmt:message key="fms.request.label.currency"/>&nbsp;
					<fmt:formatNumber value="${widget.request.lateCharges}" maxFractionDigits="2" pattern="#,##0.00"/>					
				</td>
		    </tr>
		</c:if>
		
		<c:if test="${ not empty(widget.systemCalculatedCharges) }">
			<tr>
		        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.label.systemCalculatedCharges"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow">
		        	<fmt:message key="fms.request.label.currency"/>&nbsp;
					<fmt:formatNumber value="${widget.systemCalculatedCharges}" maxFractionDigits="2" pattern="#,##0.00"/>					
				</td>
		    </tr>
		</c:if>
    </c:if>
    <tr><td class="profileFooter" colspan="2">
    	<c:forEach items="${widget.serviceForms}" var="profiler">
            <x:display name="${profiler.absoluteName}"/>
        </c:forEach>
    	</td>
    </tr>
    <c:if test="${widget.isHod eq 'true'}" >
	    <c:if test="${widget.request.status eq 'H'}" >
		    <tr>
		    	<td class="contentTitleFont" colspan="2" align="center"></td>
		    </tr>
		    <tr>
		        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.facility.label.remarksDepartmentApprover"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow"><x:display name="${widget.remarks.absoluteName}"/></td>
		    </tr>
	    </c:if>
    </c:if>
    <tr>
    	<td class="contentTitleFont" colspan="2" align="center"></td>
    </tr>
    <tr>
    	<td class="profileFooter" colspan="2" align="center">
            <x:display name="${widget.submit.absoluteName}"/>
            <x:display name="${widget.hodApprove.absoluteName}"/>
            <x:display name="${widget.hodReject.absoluteName}"/>
            <x:display name="${widget.assignFC.absoluteName}"/>
            <x:display name="${widget.fcEdit.absoluteName}"/>
			<x:display name="${widget.fcSubmit.absoluteName}"/>
 			<x:display name="${widget.viewOutsource.absoluteName}"/>
            <x:display name="${widget.outSource.absoluteName}"/>
            <x:display name="${widget.modifyRequest.absoluteName}"/>
            <x:display name="${widget.checkAvailability.absoluteName}"/>
            <x:display name="${widget.fcAccept.absoluteName}"/>
            <x:display name="${widget.fcReject.absoluteName}"/>
			<x:display name="${widget.lateCompletion.absoluteName}"/>
			<x:display name="${widget.cancellationCharges.absoluteName}"/>
			<x:display name="${widget.prepareAssignment.absoluteName}"/>
			<x:display name="${widget.viewAssignment.absoluteName}"/>
			<x:display name="${widget.houReject.absoluteName}"/>
            <x:display name="${widget.cancelRequest.absoluteName}"/>
            <x:display name="${widget.cancel.absoluteName}"/>
			<x:display name="${widget.copyRequest.absoluteName}"/>
    	</td>
    </tr>
    <tr>
    	<td align="center" colspan="2"><h2><font color="red"><x:display name="${widget.childMap['errorLbl'].absoluteName}" /></font></h2></td>
    </tr>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
