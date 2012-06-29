<%@ include file="/common/header.jsp" %>

<script type="text/javascript">
function generateCsv() {
	document.location = "genContentSummaryReportCSV.jsp";
	return false;
}
</script>

<x:config>
    <page name="contentSummaryReportPg">
        <com.tms.report.ui.ContentSummaryReport name="contentSummaryReport"/>
    </page>
</x:config>

<x:permission permission="com.tms.cms.AccessReports" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="com.tms.report"/> - Content Summary Report</c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<x:display name="contentSummaryReportPg.contentSummaryReport"/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>