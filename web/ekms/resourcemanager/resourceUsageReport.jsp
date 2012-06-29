<%@ include file="/common/header.jsp" %>

<script type="text/javascript">
function generateCsv() {
	document.location = "genResourceUsageReportCSV.jsp";
	return false;
}
</script>

<x:config>
    <page name="resourceUsageReportPg">
        <com.tms.report.ui.ResourceUsageReport name="resourceUsageReport"/>
    </page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="com.tms.report"/> - Resource Usage Report</c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<x:display name="resourceUsageReportPg.resourceUsageReport"/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>