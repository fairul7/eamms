<%@include file="/common/header.jsp"%>

<x:config>
    <page name="printProjectMetric">
        <com.tms.collab.project.ui.ProjectPerformanceMetricReportPrint name="report"/>
    </page>
</x:config>
<c:if test="${!empty param.reportId}">
	<x:set name="printProjectMetric.report" property="reportId" value="${param.reportId}"/>
</c:if>

<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <STYLE type="text/css" media="screen"> 
    .tableBackground {background-color: #999999; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
		.contentTitleFont {background-color: #CCCCCC; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
		.contentBgColor {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
		.tableHeader {background-color: #CCCCCC; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
		.tableRow {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
	 </STYLE> 
    <style type="text/css">
		.tableBackground {background-color: #999999; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
		.contentTitleFont {background-color: #CCCCCC; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
		.contentBgColor {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
		.tableHeader {background-color: #CCCCCC; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
		.tableRow {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
		div.noPrint
		{font: 12pt Arial bold}
	</style>
<style type="text/css" media="print">
div.noPrint{
display:none;
} 
</style>
    <title>Enterprise Knowledge Portal</title>
</head>

<body>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td>
		<x:display name="printProjectMetric.report"/>
	</td>
</tr>

<tr>
	<td>
	<div class="noPrint">
	<input type="button" class="button" value="<fmt:message key="project.label.print"/>" onclick="window.print()"/>	
	</div>
	</td>
</tr>

</table>

</body>
</html>