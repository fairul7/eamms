<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<x:display name="${widget.panel.absoluteName}" body="custom">
<br>
<c:set var="colSpanValue" value="9"/>
<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
	<tr>
		<th width="5%"><fmt:message key="fms.facility.label.sNo"/></th>
		<th><fmt:message key="fms.facility.label.assignmentId"/></th>
		<th><fmt:message key="fms.facility.table.manpower"/></th>
		<th width="15%"><fmt:message key="fms.facility.label.requiredDate"/></th>
		<th width="10%"><fmt:message key="fms.facility.label.requiredTime"/></th>
		<th><fmt:message key="fms.facility.label.manpower"/></th>  
		<th><fmt:message key="fms.request.label.completionDate"/></th>
		<th width="7%"><fmt:message key="fms.facility.table.status"/></th>
		<c:if test="${widget.isFC eq 'true'}"><th width="7%"><fmt:message key="fms.facility.label.action"/></th></c:if>
		
	</tr>
    	<c:forEach items="${widget.assignments}" var="obj" varStatus="sNo">
    	<tr>
    	    <td class="profileRow" align="right"><c:out value="${(sNo.index+1)}"/></td>
    	    <td class="profileRow">
				<c:choose>
					<c:when test="${obj.unitApprover == '1'}" >
    	    			<c:out value="${obj.codeWithLink}" escapeXml="false"/>
    	    		</c:when>
					<c:otherwise>
						<c:out value="${obj.code}"/>
					</c:otherwise>
				</c:choose>
			</td>
            <td class="profileRow"><c:out value="${obj.rateCardCategoryName}"/><c:out value="${obj.competencyName}"/></td>
            <td class="profileRow" align="center"><fmt:formatDate value="${obj.requiredFrom}" pattern="${globalDateLong}"/> - <fmt:formatDate value="${obj.requiredTo}" pattern="${globalDateLong}"/></td>
            <td class="profileRow" align="center"><c:out value="${obj.fromTime}"/> - <c:out value="${obj.toTime}"/></td>
            <td class="profileRow"><c:out value="${obj.assignedFacility}"/></td>
			<td class="profileRow"><fmt:formatDate value="${obj.completionDate}" pattern="d MMM yyyy HH:mm:ss"/></td>
            <td class="profileRow"><c:out value="${obj.statusLabel}"/></td>
			
				<c:if test="${widget.isFC eq 'true'}">
				<td class="profileRow" align="center">
					<c:if test="${obj.statusLabel eq 'New' or obj.statusLabel eq 'Assigned' or obj.statusLabel eq 'Cancelled'}">
							<c:choose>
							<c:when test="${obj.cancelBy eq null}" >
    	    					<input type="hidden" id="serviceId_<c:out value="${obj.assignmentId}"/>" name="serviceId_<c:out value="${obj.assignmentId}"/>" value="<c:out value="${widget.serviceId}"/>" />
								<input type="hidden" id="statusLab_<c:out value="${obj.assignmentId}"/>" name="statusLab_<c:out value="${obj.assignmentId}"/>" value="<c:out value="${obj.statusLabel}"/>" />
								<input type="hidden" id="facilityId_<c:out value="${obj.assignmentId}"/>" name="facilityId_<c:out value="${obj.assignmentId}"/>" value="<c:out value="${obj.competencyId}"/>" />								
								<a id="<c:out value="${obj.assignmentId}"/>"class="test" style="cursor:pointer">
									<fmt:message key="fms.facility.cancel"/>
								</a>
								
    	    				</c:when>
							<c:otherwise>
								<input type="hidden" id="serviceId_<c:out value="${obj.assignmentId}"/>" name="serviceId_<c:out value="${obj.assignmentId}"/>" value="<c:out value="${widget.serviceId}"/>" />
								<c:out value="${obj.cancelBy}"/>								
							</c:otherwise>
						</c:choose>
					</c:if>
				</td>
			</c:if>
		</tr>
        </c:forEach>
		<input type="hidden" id="reqId" name="reqId" value="<c:out value="${widget.requestId}"/>" />
</table><br>
</x:display>
<jsp:include page="../../form_footer.jsp" flush="true"/>

