<%@include file="/common/header.jsp"%>

<x:config>
	<page name="report">
		<com.tms.sam.po.report.ui.StatusReportListing name="status"/>
	</page>
</x:config>

<script type="text/javascript">
function generateCsv() {
	document.location = "genStatusReportCSV.jsp";
}
</script>

<%@include file="/ekms/includes/header.jsp" %>
<table border="0" cellpadding="5" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont"><fmt:message key="po.menu.reports"/> > <fmt:message key="report.label.statusReport"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="report.status" /></td>
	</tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>
