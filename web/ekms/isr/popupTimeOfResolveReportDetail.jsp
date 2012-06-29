<%@page import="com.tms.collab.isr.permission.model.ISRGroup,
				com.tms.ekms.setup.model.SetupModule" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_DEPARTMENT_REPORTS %>" />
<%@include file="includes/accessControl.jsp" %>

<%@include file="includes/isrCommon.jsp" %>

<x:config>
	<page name="timeOfResolveReportDetailTablePg">
		<com.tms.collab.isr.report.ui.TimeOfResolveReportDetailTable name="timeOfResolveReportDetailTable"/>
    </page>
</x:config>

<c:if test="${! empty param.type }">
	<x:set name="timeOfResolveReportDetailTablePg.timeOfResolveReportDetailTable" property="condition" value="${param.type}" />
	<x:set name="timeOfResolveReportDetailTablePg.timeOfResolveReportDetailTable" property="fromDate" value="${widgets['timeOfResolveFormPg.timeOfResolveForm'].from}" />
	<x:set name="timeOfResolveReportDetailTablePg.timeOfResolveReportDetailTable" property="toDate" value="${widgets['timeOfResolveFormPg.timeOfResolveForm'].to}" />
	<x:set name="timeOfResolveReportDetailTablePg.timeOfResolveReportDetailTable" property="statusId" value="${widgets['timeOfResolveFormPg.timeOfResolveForm'].statusId}" />
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
		Time Of Resolve Report Detail
		<p/>
		<span class="contentBgColor">
		<fmt:message key="isr.label.date"/>:
		<fmt:formatDate value="${widgets['timeOfResolveFormPg.timeOfResolveForm'].from}" type="date" pattern="dd-MMM-yyyy" />
		to
		<fmt:formatDate value="${widgets['timeOfResolveFormPg.timeOfResolveForm'].to}" type="date" pattern="dd-MMM-yyyy" />
		</span>
		</p>
		</td>
		<td height="81" align="right"><img src="/ekms/<%=setup.get("siteLogo") %>"/></td>
	</tr>
	<tr>
		<td colspan="2" ><hr/></td>
	</tr>
	<tr>
		<td colspan="2" class="contentBgColor">
		<x:display name="timeOfResolveReportDetailTablePg.timeOfResolveReportDetailTable" />
		</td>
	</tr>
</table>

<script>
	print();
</script>