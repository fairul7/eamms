<%@page import="com.tms.collab.isr.permission.model.ISRGroup,
				com.tms.collab.isr.ui.NewRequestForm" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_DEPARTMENT_REPORTS %>" />
<%@include file="includes/accessControl.jsp" %>

<x:config>
	<page name="timeOfResolveFormPg">
		<com.tms.collab.isr.report.ui.TimeOfResolveReportForm name="timeOfResolveForm"/>
    </page>
</x:config>

<c:if test="${forward.name == 'print' }">
	<script>
		window.open("popupTimeOfResolveReport.jsp");
	</script>
</c:if>

<script type="text/javascript">
function generateCsv() {
	document.location = "genTimeOfResolveReportCSV.jsp";
	return false;
}
</script>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="5" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont"><fmt:message key="isr.menu.timeOfResolve"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="timeOfResolveFormPg.timeOfResolveForm" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>