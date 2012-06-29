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
	response.setHeader("content-disposition","attachment; filename=Request_Report_"+todayDate+".xls");
	
%>


<x:config>
    <page name="engRequestReport">         
            <com.tms.fms.reports.ui.ExportEngineeringReqReportTable name="excel"/>        
    </page>
</x:config>
<%--
<c:set var="widget" value="${widgets['reqRepform.form']}"/>
<c:if test="${widget != null}">
	<x:set name="engRequestReport.excel" property="requestStartDate" value="${widget.requestStartDate}"/>
	<x:set name="engRequestReport.excel" property="requestEndDate" value="${widget.requestEndDate}"/>
	<x:set name="engRequestReport.excel" property="requestProgramId" value="${widget.requestProgramId}"/>
	<x:set name="engRequestReport.excel" property="requestTypeId" value="${widget.requestTypeId}"/>
	<x:set name="engRequestReport.excel" property="requestStatus" value="${widget.requestStatus}"/>
</c:if>

<c:set var="requestStatus" value="${widget.requestStatus}"/>
--%>

<table>
	<%--<tr>
		<td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			<table width="50%" cellpadding="3" cellspacing="1" align="left">
				<c:if test="${!empty widget.requestStartDate}">
				<tr>
					<td class="classRowLabel" align="right">Report Date <fmt:message key="general.from" />:</td>
					<td class="classRow"><fmt:formatDate value="${widget.requestStartDate}" type="date" pattern="dd MMM yyyy"/></td>
				</tr>
				</c:if>
				<c:if test="${!empty widget.requestEndDate}">
				<tr>
					<td class="classRowLabel" align="right">Report Date <fmt:message key="general.to" />:</td>
					<td class="classRow"><fmt:formatDate value="${widget.requestEndDate}" type="date" pattern="dd MMM yyyy"/></td>
				</tr>
				</c:if>
				<c:if test="${!empty widget.requestProgramId}">
				<tr>
					<td class="classRowLabel" align="right">Program:</td>
					
					<c:set var="programId" value="${widget.requestProgramId}"></c:set>
					<%
					SetupModule modulset = (SetupModule)Application.getInstance().getModule(SetupModule.class);
					ProgramObject pObj=modulset.getProgram(pageContext.getAttribute("programId").toString());
					%>
					<td class="classRow"><%=pObj.getProgramName() %></td>
				</tr>
				</c:if>
				<c:if test="${!empty widget.requestTypeId}">
				<tr>
					<td class="classRowLabel" align="right">Request Type:</td>
					
					<td class="classRow">
						<c:choose>
							<c:when test="${widget.requestTypeId eq 'I'"}">Internal</c:when>
							<c:when test="${widget.requestTypeId eq 'E'"}">External</c:when>
							<c:when test="${widget.requestTypeId eq 'N'"}">Non Program(daily tasks)</c:when>
						</c:choose>
						
					</td>
				</tr>
				</c:if>
				<c:if test="${!empty widget.requestStatus}">
				<tr>
					<td class="classRowLabel" align="right">Status:</td>
					
					<td class="classRow">
					<%=(String)EngineeringModule.STATUS_MAP.get(pageContext.getAttribute("requestStatus")) %>
					</td>
				</tr>
				</c:if>
			</table>
		</td>
	</tr>--%>
   	<tr>
   		<td>
			<x:display name="engRequestReport.excel" ></x:display>
   		</td>
   	</tr>
</table>





