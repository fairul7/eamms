<%@include file="/common/header.jsp"%>

<x:config>
    <page name="nonprintTimesheet">
        <com.tms.collab.timesheet.ui.TimeSheetNonProjectPrintInMandaysReport name="nonproject"/>
    </page>
</x:config>

<% 
	String checkedItemsString = "";
	if(session.getAttribute("tsMandaysReportCheckedItemsString") != null) {
		checkedItemsString = session.getAttribute("tsMandaysReportCheckedItemsString").toString();
	}
	pageContext.setAttribute("checkedItemsString", checkedItemsString);
%>

<c:set var="checkedItemsString" value="${checkedItemsString }" />

<c:if test="${!empty param.projectid}">
	<x:set name="nonprintTimesheet.nonproject" property="projectId" value="${param.projectid}"/>
	<x:set name="nonprintTimesheet.nonproject" property="startDate" value="${param.startDate}" />
	<x:set name="nonprintTimesheet.nonproject" property="endDate" value="${param.endDate}" />
	<x:set name="nonprintTimesheet.nonproject" property="checkedItemsString" value="${checkedItemsString}" />
</c:if>

<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <style type="text/css">
		.tableBackground {background-color: #999999; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
		.contentTitleFont {background-color: #CCCCCC; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
		.contentBgColor {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
		.tableHeader {background-color: #CCCCCC; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
		.tableRow {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
	</style>
    <title>Enterprise Knowledge Portal</title>
</head>

<body>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td>
		<x:display name="nonprintTimesheet.nonproject"/>
	</td>
</tr>
</table>

</body>
</html>