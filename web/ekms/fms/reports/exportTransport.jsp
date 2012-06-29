<%@ include file="/common/header.jsp" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Collection"%>
<%@page import="kacang.Application"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.tms.fms.setup.model.SetupModule"%>
<%@page import="com.tms.fms.setup.model.ProgramObject"%>

<%
	
	String mimeType = "application/vnd.ms-excel";
	response.setContentType(mimeType);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String todayDate=sdf.format(Calendar.getInstance().getTime());
	response.setHeader("content-disposition","attachment; filename=Transport_Request_Report_"+todayDate+".xls");
	
%>

<x:config>
    <page name="export">         
            <com.tms.fms.reports.ui.ExportTransportReqReportTable name="transport"/>        
    </page>
</x:config>

<x:set name="export.transport" value="${widgets['reporttable.transport'].tableRowsData}" property="tableRowsData"></x:set>
<x:set name="export.transport" value="${widgets['reporttable.transport'].totalRowCountData}" property="totalRowCountData"></x:set>

<table>
	<%--<tr>
		<td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			<table width="50%" cellpadding="3" cellspacing="1" align="left">
				<tr>
					<td class="classRowLabel" align="right">Report Date <fmt:message key="general.from" />:</td>
					<td class="classRow"><fmt:formatDate value="${widget.startDate}" type="date" pattern="dd MMM yyyy"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" align="right">Report Date <fmt:message key="general.to" />:</td>
					<td class="classRow"><fmt:formatDate value="${widget.endDate}" type="date" pattern="dd MMM yyyy"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" align="right">Program:</td>
					
					<c:set var="programId" value="${widget.programId}"></c:set>
					<%
					SetupModule modulset = (SetupModule)Application.getInstance().getModule(SetupModule.class);
					ProgramObject pObj=modulset.getProgram(pageContext.getAttribute("programId").toString());
					%>
					<td class="classRow"><%=pObj.getProgramName() %></td>
				</tr>
			</table>
		</td>
	</tr>--%>
   	<tr>
   		<td>
			<x:display name="export.transport" ></x:display>
   		</td>
   	</tr>
</table>


