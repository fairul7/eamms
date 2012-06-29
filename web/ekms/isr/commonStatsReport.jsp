<%@page import="com.tms.collab.isr.permission.model.ISRGroup" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_GENERAL_REPORTS %>" />
<%@include file="includes/accessControl.jsp" %>

<x:config>
	<page name="commonStatsFormPg">
		<com.tms.collab.isr.report.ui.CommonStatsReportForm name="commonStatsForm"/>
    </page>
</x:config>

<c:if test="${forward.name == 'print' }">
	<script>
		window.open("popupCommonStatsReport.jsp");
	</script>
</c:if>

<script type="text/javascript">
function generateCsv() {
	document.location = "genCommonStatsReportCSV.jsp";
	return false;
}
</script>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="5" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont"><fmt:message key="isr.menu.commonStats"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="commonStatsFormPg.commonStatsForm" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>