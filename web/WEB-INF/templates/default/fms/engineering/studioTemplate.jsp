<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<x:display name="${widget.panel.absoluteName}" body="custom">
<br>
<c:set var="colSpanValue" value="9"/>
<x:display name="${widget.validateRequestId.absoluteName}" />
<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
	<tr>
		<th><fmt:message key="fms.facility.label.sNo"/></th>
		<th><fmt:message key="fms.facility.label.studio"/></th>
		<th><fmt:message key="fms.facility.label.bookingDate"/></th>
		<th><fmt:message key="fms.facility.label.blockBooking"/></th>
		<th><fmt:message key="fms.facility.label.segment"/></th>
		<th><fmt:message key="fms.facility.label.settingTime"/></th>
		<th><fmt:message key="fms.facility.label.rehearsalTime"/></th>
		<th><fmt:message key="fms.facility.label.vtr"/></th>
		<th><fmt:message key="fms.facility.table.location"/></th>
		<c:if test="${widget.viewMode eq 'false'}">
			<th><x:display name="${widget.delete.absoluteName}"/></th>
			<c:set var="colSpanValue" value="10"/>
		</c:if>
	</tr>
	    <c:set var="onlyCanBook1Studio" value="noBookedYet"/>
    	<c:forEach items="${widget.services}" var="obj" varStatus="sNo">
    	<tr>
    	    <td class="profileRow" align="right"><c:out value="${(sNo.index+1)}"/></td>
            <td class="profileRow"><c:out value="${obj.facility}" escapeXml="false"/></td>
            <td class="profileRow" align="center">
            	<fmt:formatDate value="${obj.bookingDate}" pattern="${globalDateLong}"/> - 
				<fmt:formatDate value="${obj.bookingDateTo}" pattern="${globalDateLong}"/>
			</td>
			<td class="profileRow" align="center"><c:out value="${obj.blockBooking}"/></td>
            <td class="profileRow"><c:out value="${obj.segment}"/></td>
            <td class="profileRow" align="center"><c:out value="${obj.settingFrom}"/> - <c:out value="${obj.settingTo}"/></td>
            <td class="profileRow" align="center"><c:out value="${obj.rehearsalFrom}"/> - <c:out value="${obj.rehearsalTo}"/></td>
            <td class="profileRow" align="center"><c:out value="${obj.vtrFrom}"/> - <c:out value="${obj.vtrTo}"/></td>
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
		<c:set var="onlyCanBook1Studio" value="booked"/>
        </c:forEach>
    
    <tr>
        <td class="profileFooter" colspan='<c:out value="${colSpanValue}"/>'>
           <c:if test="${onlyCanBook1Studio eq 'noBookedYet'}">
            <x:display name="${widget.add.absoluteName}"/>
           </c:if>
        </td>
    </tr>
</table><br>
</x:display>
<jsp:include page="../../form_footer.jsp" flush="true"/>

