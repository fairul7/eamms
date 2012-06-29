<%@include file="/common/header.jsp" %>

<%
response.setHeader("Content-Disposition"," attachment; filename=attendantViewRequest_" + request.getParameter("requestId") + ".html"); 
%>

<x:config>
	<page name="isr">
		<com.tms.collab.isr.ui.AttendantViewRequest name="attendantViewRequestPrintable" template="isr/attendantViewRequestPrintable"/>
    </page>
</x:config>

<c:choose>
<c:when test="${!empty param.requestId }">
	<x:set name="isr.attendantViewRequestPrintable" property="requestId" value="${param.requestId}" />
	<x:set name="isr.attendantViewRequestPrintable" property="showDownloadLink" value="${false}" />
</c:when>
</c:choose>
<c:if test="${param.clarificationDesc eq 'true'}">
	<x:set name="isr.attendantViewRequestPrintable" property="clarificationDesc" value="${true}" />
</c:if>
<c:if test="${empty param.clarificationDesc || param.clarificationDesc eq 'false'}">
	<x:set name="isr.attendantViewRequestPrintable" property="clarificationDesc" value="${false}" />
</c:if>
<c:if test="${param.assignmentRemarksDesc eq 'true'}">
	<x:set name="isr.attendantViewRequestPrintable" property="assignmentRemarksDesc" value="${true}" />
</c:if>
<c:if test="${empty param.assignmentRemarksDesc || param.assignmentRemarksDesc eq 'false'}">
	<x:set name="isr.attendantViewRequestPrintable" property="assignmentRemarksDesc" value="${false}" />
</c:if>
<c:if test="${param.suggestedResolutionDesc eq 'true'}">
	<x:set name="isr.attendantViewRequestPrintable" property="suggestedResolutionDesc" value="${true}" />
</c:if>
<c:if test="${empty param.suggestedResolutionDesc || param.suggestedResolutionDesc eq 'false'}">
	<x:set name="isr.attendantViewRequestPrintable" property="suggestedResolutionDesc" value="${false}" />
</c:if>

<html>
<head>
	<title><fmt:message key="general.label.ekp"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<style type="text/css">
		td {font-size: 8pt}
		#altItem{
			background-color:#E0E0E0;
			width:100%;
		}
		.fieldTitle{
			text-align:right;
			font-weight:bold;
		}
		.contentBgColor {background-color:#E1EEF7; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt}
		.contentFont {font-family: Arial, Helvetica, sans-serif; font-size: 8.5pt}
		.contentTitleFont {background-color:#DD0000; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; color: #ffffff; font-weight:bold}
	</style>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.permission.viewProcessRequest"/></td>
	</tr>
	<tr>
		<td><x:display name="isr.attendantViewRequestPrintable" /></td>
	</tr>
</table>

</body>