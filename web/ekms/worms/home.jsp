<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.Application,
				 kacang.services.security.User,
				 kacang.services.security.SecurityService"%>
<x:config>
    <page name="wormsProject">
        <com.tms.collab.project.ui.PersonalProject name="personalProject"/>
    </page>
</x:config>
<x:set name="wormsProject.personalProject" property="documentLink" value="/ekms/content/content.jsp"/>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<c:set var="mainBody"><x:display name="wormsProject.personalProject"/></c:set>
<%
SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
String userId = service.getCurrentUser(request).getId();
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<c:choose>
    <c:when test="${!empty widgets['wormsProject.personalProject'].project}">
		<tr valign="middle">
			<td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key="project.label.myProjects"/>
			> <c:out value="${widgets['wormsProject.personalProject'].projectName}"/>
			</font></b></td>
			<td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><c:out value="${mainBody}" escapeXml="false" /></td></tr>

				<tr>
					<td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
						<table width="95%" align="center">
							<tr><td>
							<input type="button" value="<fmt:message key='project.label.printProjectSchedule'/>" class="button" onClick="window.open('<c:url value="/ekms/worms/projectPrint.jsp?projectId=${widgets['wormsProject.personalProject'].project.projectId}"/>');"/>
							<%if(service.hasPermission(userId, "com.tms.worms.project.Project.administer", "com.tms.worms.WormsHandler", null)||service.hasPermission(userId, "com.tms.worms.project.Project.report", "com.tms.worms.WormsHandler", null)){ %>
							<input type="button" value="<fmt:message key='project.label.projectPerformanceMetricReport'/>" class="button" onClick="window.location='<c:url value="/ekms/worms/projectPerformaceMetricReport.jsp?projectId=${widgets['wormsProject.personalProject'].project.projectId}"/>';"/>														
							<%} %>
							</td>
							</tr>
						</table>
					</td>
				</tr>

			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
		
	</c:when>
	<c:otherwise>
		<tr valign="middle">
			<td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key="project.label.projects"/> > <fmt:message key='project.label.myProjects'/></font></b></td>
			<td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsProject.personalProject"/></td></tr>
			<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
		
	</c:otherwise>
	</c:choose>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>

