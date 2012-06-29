<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="reporttableTotal">         
            <com.tms.fms.reports.ui.TransportReqReportTotalTable name="transport" width="100%"/>
            <com.tms.fms.reports.ui.TransportReqReportTotalExport name="exportTransport" width="100%"/>        
    </page>
</x:config>

<c:choose>
	<c:when test="${forward.name == 'export'}">
		<%
	
		String mimeType = "application/vnd.ms-excel";
		response.setContentType(mimeType);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String todayDate=sdf.format(Calendar.getInstance().getTime());
		response.setHeader("content-disposition","attachment; filename=Transport_Request_With_Total_Report_"+todayDate+".xls");
		
		%>
		<x:set name="reporttableTotal.exportTransport" value="${widgets['reporttableTotal.transport'].rowsData}" property="rowsData"></x:set>
		<x:set name="reporttableTotal.exportTransport" value="${widgets['reporttableTotal.transport'].rowsCount}" property="count"></x:set>
		<table>
			<tr>
				<td><x:display name="reporttableTotal.exportTransport" /></td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<head>
			<link rel="stylesheet" href="/contentSyndicationToMiti/images/style.css">
		</head>
		<%@include file="/ekms/includes/header.jsp" %>
		<jsp:include page="includes/header.jsp" />
		
		<table border="0" cellspacing="0" cellpadding="5" bgcolor="white" width="100%">
		    <tr valign="middle">
		        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
		        	<fmt:message key="fms.report.message.label.transportrequestlistingTotalCost"/> <fmt:message key="fms.report.message.label.report"/></font></b></td>
		        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
		    </tr>
		    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
		    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
		
		        <x:display name="reporttableTotal.transport" />
		    
		    </td></tr>
		    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
		</table>
		
		
		
		<jsp:include page="includes/footer.jsp" />
		<%@include file="/ekms/includes/footer.jsp" %>
	</c:otherwise>
</c:choose>

