<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="auditTrail">
    	<com.tms.hr.recruit.ui.AuditTrailList name="list"/>
    </page>
</x:config>

<!-- Handle Events -->
<c:choose>
	<c:when test="${!empty param.vacancyCode}">
		<c:redirect url="vacancyReportListView.jsp?vacancyCode=${param.vacancyCode}"/>
	</c:when>
	<c:when test="${forward.name == 'genCSV'}" >	
		<c:redirect url="genCommonStatsPriorityReportCSV.jsp"/>
	</c:when>
</c:choose>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.viewAuditTrail"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="auditTrail.list"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>