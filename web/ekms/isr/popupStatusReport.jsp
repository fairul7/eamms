<%@page import="com.tms.collab.isr.permission.model.ISRGroup,
				com.tms.ekms.setup.model.SetupModule" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_GENERAL_REPORTS %>" />
<%@include file="includes/accessControl.jsp" %>

<%@include file="includes/isrCommon.jsp" %>

<% 
SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
%>

<style>
.reportHeader {
	font-size: 18px;
	font-weight: bold;
	margin-left: 100px;
	text-align: left;
	font-family: arial;
	vertical-align: bottom;	
}
.tableHeader{
	font-size: 12px;
	font-family: arial;
	font-weight: bold;
	color: white;
	background-color: #736F6E;
}
.tableRow{
	font-family: arial;
	font-size: 12px;
}
.contentBgColor{
	font-family: arial;
	font-size: 12px;
}
</style>

<table border="0" cellpadding="2" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="reportHeader">
		Status Report
		<p/>
		<span class="contentBgColor">
		<fmt:message key="isr.label.date"/>:
		<fmt:formatDate value="${widgets['statusReportTablePg.statusReportTable'].fromDate}" type="date" pattern="dd-MMM-yyyy" />
		to
		<fmt:formatDate value="${widgets['statusReportTablePg.statusReportTable'].toDate}" type="date" pattern="dd-MMM-yyyy" />
		</span>
		</p>
		</td>
		<td height="81" align="right"><img src="/ekms/<%=setup.get("siteLogo") %>"/></td>
	</tr>
	<tr>
		<td colspan="2"><hr/></td>
	</tr>
	<tr>
		<td colspan="2" class="contentBgColor">
		<x:display name="statusReportTablePg.statusReportTable" body="/WEB-INF/templates/default/isr/popupStatusReport.jsp"/>
		</td>
	</tr>
</table>

<script>
	print();
</script>