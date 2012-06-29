<%@ include file="/common/header.jsp" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>

<%
	
	String mimeType = "application/vnd.ms-excel";
	response.setContentType(mimeType);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String todayDate=sdf.format(Calendar.getInstance().getTime());
	response.setHeader("content-disposition","attachment; filename=Resource_Utilization_Report_"+todayDate+".xls");
	
%>

<x:config>
    <page name="reportresource">         
            <com.tms.fms.reports.ui.ResourceUtilizationExportTable name="util"/>        
    </page>
</x:config>

<%--<c:set var="widget" value="${widgets['resource.table']}"/>
<c:if test="${widget != null}">
	<x:set name="reportresource.util" property="resourceStartDate" value="${widget.resourceStartDate}"/>
	<x:set name="reportresource.util" property="resourceEndDate" value="${widget.resourceEndDate}"/>
	<x:set name="reportresource.util" property="resourceProgramId" value="${widget.resourceProgramId}"/>
	<x:set name="reportresource.util" property="resourceServiceTypeId" value="${widget.resourceServiceTypeId}"/>
	<x:set name="reportresource.util" property="resourceStatus" value="${widget.resourceStatus}"/>
</c:if>--%>

	


<table border="0" cellspacing="0" cellpadding="5" bgcolor="white">
    
	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="reportresource.util" />
    
    </td></tr>
   
</table>

