<%@page import="com.tms.collab.isr.permission.model.ISRGroup,
				com.tms.collab.isr.ui.NewRequestForm" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_DEPARTMENT_REPORTS %>" />
<%@include file="includes/accessControl.jsp" %>

<x:config>
	<page name="staffReportTablePg">
		<com.tms.collab.isr.report.ui.StaffReportTable name="staffReportTable"/>
    </page>
</x:config>


<c:if test="${! empty param.reportLinkNew}">
	<script>
		window.open("popupStaffReportDetail.jsp?id=<c:out value='${param.reportLinkNew}'/>");
	</script>
</c:if>
<c:if test="${! empty param.reportLinkInProgress}">
	<script>
		window.open("popupStaffReportDetail.jsp?id=<c:out value='${param.reportLinkInProgress}'/>");
	</script>
</c:if>
<c:if test="${! empty param.reportLinkCompleted}">
	<script>
		window.open("popupStaffReportDetail.jsp?id=<c:out value='${param.reportLinkCompleted}'/>");
	</script>
</c:if>
<c:if test="${! empty param.reportLinkClose}">
	<script>
		window.open("popupStaffReportDetail.jsp?id=<c:out value='${param.reportLinkClose}'/>");
	</script>
</c:if>
<c:if test="${! empty param.reportLinkReopen}">
	<script>
		window.open("popupStaffReportDetail.jsp?id=<c:out value='${param.reportLinkReopen}'/>");
	</script>
</c:if>
<c:if test="${! empty param.reportLink}">
	<script>
		window.open("popupStaffReportDetail.jsp?id=<c:out value='${param.reportLink}'/>");
	</script>
</c:if>


<c:if test="${forward.name == 'print' }">
	<script>
		window.open("popupStaffReport.jsp");
	</script>
</c:if>

<script type="text/javascript">
function generateCsv() {
	document.location = "genStaffReportCSV.jsp";
}
</script>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="5" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont"><fmt:message key="isr.menu.staffReport"/></td>
	</tr>
		<tr>
		<td class="contentBgColor"><x:display name="staffReportTablePg.staffReportTable" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>