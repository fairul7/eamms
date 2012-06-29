<%@include file="/common/header.jsp"%>
<x:permission permission="com.tms.assetmanagement.manageAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">

<x:config>
    <page name="printMonthlyDepReport">
     	<com.tms.assetmanagement.ui.MonthlyDepreciationReport name="form" width="100%" />
    </page>
</x:config>

<c:set var="year" value="${widgets['monthlyDepReport.form'].year}" />
<c:set var="category" value="${widgets['monthlyDepReport.form'].category}" />
	
<c:if test="${!empty year}">
	<x:set name="printMonthlyDepReport.form" property="year" value="${year}"/>
</c:if>

<c:if test="${!empty category}">
	<x:set name="printMonthlyDepReport.form" property="category" value="${category}"/>
</c:if>

<html>
<head>
<title><fmt:message key='asset.label.monthlyDepReport'/></title>
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
    <jsp:include page="includes/header.jsp"/>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">

    <tr><td colspan="2" valign="TOP"  class="contentBgColor" bgcolor="#FFFFFF" >
         
            <x:display name="printMonthlyDepReport.form" />
        
      </td></tr>
        
    <tr><td colspan="2" valign="TOP" bgcolor="#FFFFFF" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
      </table>    
    <jsp:include page="includes/footer.jsp"/>
</body>
</html>

<script>
	window.print();
</script>

</x:permission>