<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_summaryReportPrint">
          <com.tms.hr.claim.ui.ClaimSummaryReport name="summaryReportPrint"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

<c:if test="${!empty param.month}">
<x:set name="jsp_summaryReportPrint.summaryReportPrint" property="month" value="${param.month}"/>
</c:if>
<c:if test="${!empty param.employee}">
<x:set name="jsp_summaryReportPrint.summaryReportPrint" property="selectedEmployee" value="${param.employee}"/>
</c:if>
<c:if test="${empty param.employee}">
<x:set name="jsp_summaryReportPrint.summaryReportPrint" property="selectedEmployee" value=""/>
</c:if>


<html>
<head>
<title>Summary Report</title>
<style>
.tableBackground{
	font-size: 10pt; font-family: Arial, Helvetica, sans-serif; background-color=#666666;
}
.tableHeader{
	font-size: 8pt; font-family: Arial, Helvetica, sans-serif; background-color=#CCCCCC;
}

.tableRow{
	font-size: 7pt; font-family: Arial, Helvetica, sans-serif; background-color=#FFFFFF;
}
.contentTitleFont {
	font-size: 12pt; font-family: Arial, Helvetica, sans-serif;
}
.contentBgColor {
	font-size: 10pt; font-family: Arial, Helvetica, sans-serif;
}


</style>
</head>
<body>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#FFFFFF" class="contentTitleFont"><b>




			</b></td>
        <td align="right" bgcolor="#FFFFFF" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#FFFFFF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP"  class="contentBgColor" bgcolor="#FFFFFF" >

<x:display name="jsp_summaryReportPrint.summaryReportPrint"/>

	</td></tr>


    <tr><td colspan="2" valign="TOP" bgcolor="#FFFFFF" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

    </x:permission>

</body>
</html>
