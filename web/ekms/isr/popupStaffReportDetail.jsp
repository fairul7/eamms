<%@page import="com.tms.collab.isr.permission.model.ISRGroup,
				com.tms.ekms.setup.model.SetupModule" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_GENERAL_REPORTS %>" />
<%@include file="includes/accessControl.jsp" %>
<%@include file="includes/isrCommon.jsp" %>

<x:config>
	<page name="staffReportDetailTablePg">
		<com.tms.collab.isr.report.ui.StaffReportDetailTable name="staffReportDetailTable"/>
    </page>
</x:config>

<c:if test="${! empty param.id }">
	<x:set name="staffReportDetailTablePg.staffReportDetailTable" property="linkId" value="${param.id}" />
	<x:set name="staffReportDetailTablePg.staffReportDetailTable" property="fromDate" value="${widgets['staffReportTablePg.staffReportTable'].fromDate}" />
	<x:set name="staffReportDetailTablePg.staffReportDetailTable" property="toDate" value="${widgets['staffReportTablePg.staffReportTable'].toDate}" />
</c:if>

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
		Staff Report Detail 
		<c:if test="${! empty  widgets['staffReportDetailTablePg.staffReportDetailTable'].staffName}">
			- <c:out value="${widgets['staffReportDetailTablePg.staffReportDetailTable'].staffName }"/>
		</c:if>
		<p/>
		<span class="contentBgColor">
		<fmt:message key="isr.label.date"/>:
		<fmt:formatDate value="${widgets['staffReportTablePg.staffReportTable'].fromDate}" type="date" pattern="dd-MMM-yyyy" />
		to
		<fmt:formatDate value="${widgets['staffReportTablePg.staffReportTable'].toDate}" type="date" pattern="dd-MMM-yyyy" />
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
		<x:display name="staffReportDetailTablePg.staffReportDetailTable"/>
		</td>
	</tr>
</table>

<script>
	print();
</script>