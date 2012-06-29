<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.Application,
				 com.tms.collab.project.WormsHandler,
				 kacang.services.security.User,
				 kacang.services.security.SecurityService"%>
<x:config>
    <page name="viewReport">
        <com.tms.collab.project.ui.ProjectReportTable name="ProjectReportTable"/>
    </page>
</x:config>
<%
SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
User user = (User) request.getSession().getAttribute(SecurityService.SESSION_KEY_USER);
String projectid=request.getParameter("projectId");
if(!(service.hasPermission(user.getId(), "com.tms.worms.project.Project.report", WormsHandler.class.getName(), null)||service.hasPermission(user.getId(), "com.tms.worms.project.Project.administer", WormsHandler.class.getName(), null)))
	response.sendRedirect("/ekms/worms/index.jsp");
if(!service.hasPermission(user.getId(), "com.tms.worms.project.Project.administer", WormsHandler.class.getName(), null)&&service.hasPermission(user.getId(), "com.tms.worms.project.Project.report", WormsHandler.class.getName(), null)&&!worms.hasProjectInvolvement(projectid,user.getId()))
{
	response.sendRedirect("/ekms/worms/index.jsp");
}
%>
<c:if test="${!(empty param.projectId)}">
    <c:redirect url="/ekms/worms/projectPerformaceMetricReport.jsp?projectId=${param.projectId}"/>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr valign="middle">
			<td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key="project.label.projects"/> > <fmt:message key='project.label.metricReport'/></font></b></td>
			<td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="viewReport.ProjectReportTable"/></td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>

