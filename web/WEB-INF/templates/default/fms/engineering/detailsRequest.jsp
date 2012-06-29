<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../../form_header.jsp" flush="true"/>
	<tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.requestId"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.request.requestId}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.requestTitle"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.request.title}"/></td>
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
            
            <input value="<fmt:message key="ad.label.close"/>" type="button" class="button" onClick="window.close();"/>
			
    	</td>
    </tr>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
</td>
</tr>	
</table>