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
		<th><fmt:message key="fms.facility.label.sNo"/></th>
		<th><fmt:message key="fms.facility.label.assignmentId"/></th>
		<th><fmt:message key="fms.facility.label.facility"/></th>
		<th><fmt:message key="fms.facility.label.requiredDate"/></th>
		<th><fmt:message key="fms.facility.label.requiredTime"/></th>
		<th><fmt:message key="fms.facility.label.facilityAssigned"/></th>
		<th><fmt:message key="fms.facility.table.status"/></th>
	</tr>
    	<c:forEach items="${widget.assignments}" var="obj" varStatus="sNo">
    	<tr>
    	    <td class="profileRow" align="right"><c:out value="${(sNo.index+1)}"/></td>
    	    <td class="profileRow"><c:out value="${obj.code}"/></td>
            <td class="profileRow"><c:out value="${obj.rateCardCategoryName}"/><c:out value="${obj.competencyName}"/></td>
            <td class="profileRow"><fmt:formatDate value="${obj.requiredFrom}" pattern="${globalDateLong}"/> - <fmt:formatDate value="${obj.requiredTo}" pattern="${globalDateLong}"/></td>
            <td class="profileRow"><c:out value="${obj.fromTime}"/> - <c:out value="${obj.toTime}"/></td>
            <td class="profileRow"><c:out value="${obj.assignedFacility}"/></td>
            <td class="profileRow"><c:out value="${obj.statusLabel}"/></td>
		</tr>
        </c:forEach>
</table><br>
</x:display>
<jsp:include page="../../form_footer.jsp" flush="true"/>

