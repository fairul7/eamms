<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<x:display name="${widget.panel.absoluteName}" body="custom">
<br>
<c:set var="colSpanValue" value="11"/>
<x:display name="${widget.validateRequestId.absoluteName}" />
<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
	<tr>
		<th><fmt:message key="fms.facility.label.sNo"/></th>
		<th><fmt:message key="fms.facility.label.facility"/></th>
		<th><fmt:message key="fms.facility.label.particulars"/></th>
		<th><fmt:message key="fms.facility.label.requiredDate"/></th>
		<th><fmt:message key="fms.facility.label.requiredTime"/></th>
		<th><fmt:message key="fms.facility.label.blockBooking"/></th>
		<th><fmt:message key="fms.facility.table.format"/></th>
		<th><fmt:message key="fms.facility.label.conversion"/></th>
		<th><fmt:message key="fms.facility.label.duration"/></th>
		<th><fmt:message key="fms.facility.label.noOfCopies"/></th>
		<th><fmt:message key="fms.facility.table.location"/></th>
		<c:if test="${widget.viewMode eq 'false'}">
			<th><x:display name="${widget.delete.absoluteName}"/></th>
			<c:set var="colSpanValue" value="12"/>
		</c:if>
	</tr>
    	<c:forEach items="${widget.services}" var="obj" varStatus="sNo">
    	<tr>
    	    <td class="profileRow" align="right"><c:out value="${(sNo.index+1)}"/></td>
			<td class="profileRow"><c:out value="${obj.facility}" escapeXml="false"/></td>
            <td class="profileRow"><c:out value="${obj.serviceNameLink}" escapeXml="false"/></td>
            <td class="profileRow" align="center"><fmt:formatDate value="${obj.requiredDate}" pattern="${globalDateLong}"/> - 
				<fmt:formatDate value="${obj.requiredDateTo}" pattern="${globalDateLong}"/> 
			</td>
            <td class="profileRow" align="center"><c:out value="${obj.requiredFrom}"/> - <c:out value="${obj.requiredTo}"/></td>
            <td class="profileRow" align="center"><c:out value="${obj.blockBooking}"/></td>
			<td class="profileRow"><c:out value="${obj.formatFromLabel}"/> - <c:out value="${obj.formatToLabel}"/></td>
            <td class="profileRow"><c:out value="${obj.conversionFromLabel}"/> - <c:out value="${obj.conversionToLabel}"/></td>
            <td class="profileRow"><c:out value="${obj.duration}"/></td>
            <td class="profileRow" align="center"><c:out value="${obj.noOfCopies}"/></td>
			<td class="profileRow"><c:out value="${obj.location}"/></td>
            <c:if test="${widget.viewMode eq 'false'}">
				<td class="profileRow" align="center">&nbsp;
					<c:forEach items="${widget.checkBoxes}" var="check" varStatus="checkStatus">
						<c:if test="${checkStatus.index eq sNo.index}">
							<x:display name="${check.absoluteName}"/>
						</c:if>
					</c:forEach>
				</td>
			</c:if>
		</tr>
        </c:forEach>
    
    <tr><td class="profileFooter" colspan='<c:out value="${colSpanValue}"/>'><x:display name="${widget.add.absoluteName}"/></td></tr>
</table><br>
</x:display>
<jsp:include page="../../form_footer.jsp" flush="true"/>

