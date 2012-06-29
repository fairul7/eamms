<%@ page import="com.tms.collab.project.WormsHandler,
                 kacang.services.security.User,
                 kacang.services.security.SecurityService"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ProjectPerformanceMetricReport">
        <com.tms.collab.project.ui.ProjectPerformanceMetricReport name="ProjectPerformanceMetricReport"/>
        <com.tms.collab.project.ui.ProjectPerformanceMetricReportTable name="ProjectPerformanceMetricReportTable"/>
    </page>
</x:config>
<%
SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
User user = (User) request.getSession().getAttribute(SecurityService.SESSION_KEY_USER);
String projectid=request.getParameter("projectId");
if(!(service.hasPermission(user.getId(), "com.tms.worms.project.Project.report", WormsHandler.class.getName(), null)||service.hasPermission(user.getId(), "com.tms.worms.project.Project.administer", WormsHandler.class.getName(), null)))
	response.sendRedirect("/ekms/worms/index.jsp");
if(!service.hasPermission(user.getId(), "com.tms.worms.project.Project.administer", WormsHandler.class.getName(), null)&&service.hasPermission(user.getId(), "com.tms.worms.project.Project.report", WormsHandler.class.getName(), null)&&!worms.hasProjectReportInvolvement(projectid,user.getId()))
{
	response.sendRedirect("/ekms/worms/index.jsp");
}
%>

<c:if test="${!empty param.projectId && !empty param.reportId}">
    <x:set name="ProjectPerformanceMetricReport.ProjectPerformanceMetricReport" property="projectId" value="${param.projectId}"/>
    <x:set name="ProjectPerformanceMetricReport.ProjectPerformanceMetricReportTable" property="projectId" value="${param.projectId}"/>
    <script>
    window.open("printPerformanceMetricReport.jsp?reportId=<c:out value='${param.reportId}'/>",'', 'resizable=1, scrollbars=yes, statusbar=1');
    </script>
</c:if>
<c:if test="${!empty param.projectId && empty param.reportId}">
    <x:set name="ProjectPerformanceMetricReport.ProjectPerformanceMetricReport" property="projectId" value="${param.projectId}"/>
    <x:set name="ProjectPerformanceMetricReport.ProjectPerformanceMetricReportTable" property="projectId" value="${param.projectId}"/>
    </c:if>
<c:if test="${empty param.projectId && empty param.reportId}">
<x:set name="ProjectPerformanceMetricReport.ProjectPerformanceMetricReport" property="projectId" value="${widgets['ProjectPerformanceMetricReport.ProjectPerformanceMetricReport'].projectId}"/>
    <x:set name="ProjectPerformanceMetricReport.ProjectPerformanceMetricReportTable" property="projectId" value="${widgets['ProjectPerformanceMetricReport.ProjectPerformanceMetricReport'].projectId}"/>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<c:set var="mainBody"><x:display name="ProjectPerformanceMetricReport.ProjectPerformanceMetricReport"/></c:set>

<table width="100%" border="0" cellspacing="0" cellpadding="0">

		<tr valign="middle">
			<td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key="project.label.myProjects"/>
			> <c:out value="${widgets['ProjectPerformanceMetricReport.ProjectPerformanceMetricReport'].project.projectName}"/>
			</font></b></td>
			<td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="ProjectPerformanceMetricReport.ProjectPerformanceMetricReportTable"/></td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			<font color="#ff0000"><fmt:message key="project.label.metricNotes"/></font></td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><c:out value="${mainBody}" escapeXml="false" /></td></tr>

</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>

