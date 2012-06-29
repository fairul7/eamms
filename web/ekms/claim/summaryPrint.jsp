<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_summaryPrint">
          <com.tms.hr.claim.ui.ClaimSummary name="summaryPrint"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

<c:if test="${!empty param.year}">
<x:set name="jsp_summaryPrint.summaryPrint" property="year" value="${param.year}"/>
</c:if>
<x:set name="jsp_summaryPrint.summaryPrint" property="act" value="summary"/>

<html>
<head>
<title>Summary Report</title>
<style>
.tableBackground{
	font-size: 10pt; font-family: Arial, Helvetica, sans-serif; background-color=#666666;
}
.tableHeader{
	font-size: 7pt; font-family: Arial, Helvetica, sans-serif; background-color=#CCCCCC;
}

.tableRow{
	font-size: 7pt; font-family: Arial, Helvetica, sans-serif; background-color=#FFFFFF;
}
.contentTitle {
	font-size: 12pt; font-family: Arial, Helvetica, sans-serif;
}
.contentTitleFont {
	font-size: 8pt; font-family: Arial, Helvetica, sans-serif;  background-color=#CCCCCC;
}
.contentBgColor {
	font-size: 10pt; font-family: Arial, Helvetica, sans-serif;
}


</style>
</head>
<body>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
<%--
    <tr valign="middle">
        <td height="22" bgcolor="#ffffff" class="contentTitleFont"><b><font class="contentTitle">

			Summary


			</font></b></td>
        <td align="right" bgcolor="#ffffff" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#ffffff" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
--%>
    <tr><td colspan="2" valign="TOP"  class="contentBgColor">

<x:display name="jsp_summaryPrint.summaryPrint"/>

	</td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#ffffff" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

    </x:permission>

</body>
</html>