<%@include file="/common/header.jsp"%>

<x:config>
	<page name="report">
		<com.tms.sam.po.report.ui.FinancialReportListing name="financial"/>
	</page>
</x:config>


<%@include file="/ekms/includes/header.jsp" %>
<table border="0" cellpadding="5" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont"><fmt:message key="report.label.financialReport"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="report.financial" /></td>
	</tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>
