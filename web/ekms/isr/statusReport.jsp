<%@page import="com.tms.collab.isr.permission.model.ISRGroup" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_GENERAL_REPORTS %>" />
<%@include file="includes/accessControl.jsp" %>

<x:config>
	<page name="statusReportTablePg">
		<com.tms.collab.isr.report.ui.StatusReportTable name="statusReportTable"/>
    </page>
</x:config>

<c:if test="${! empty param.reportLink}">
	<script>
		window.open("popupStatusReportDetail.jsp?id=<c:out value='${param.reportLink}'/>");
	</script>
</c:if>

<c:if test="${forward.name == 'print' }">
	<script>
		window.open("popupStatusReport.jsp");
	</script>
</c:if>

<script type="text/javascript">
function generateCsv() {
	document.location = "genStatusReportCSV.jsp";
}
</script>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="5" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont"><fmt:message key="isr.menu.statusReport"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="statusReportTablePg.statusReportTable" /></td>
	</tr>
	<tr>
		<td class="contentBgColor"></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>