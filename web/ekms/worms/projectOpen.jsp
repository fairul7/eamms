<%@ page import="com.tms.collab.project.ui.ProjectForm,
                 com.tms.collab.project.WormsHandler,
                 com.tms.collab.project.ui.RoleTable"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.worms.project.Project.edit" module="com.tms.collab.project.WormsHandler" url="/ekms/index.jsp"/>
<x:config>
    <page name="wormsProjectOpen">
        <com.tms.collab.project.ui.ProjectFormOpen name="form"/>
        <com.tms.collab.project.ui.ProjectScheduleView name="view"/>
        <com.tms.collab.project.ui.RoleTable name="roleTable"/>
    </page>
</x:config>
<c:if test="${!empty param.projectId}">
    <%
        User user = (User) request.getSession().getAttribute(SecurityService.SESSION_KEY_USER);
        WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
        if(!worms.hasProjectPermission(request.getParameter("projectId"), user.getId()))
            response.sendRedirect("/ekms/worms/project.jsp");
    %>
    <x:set name="wormsProjectOpen.form" property="projectId" value="${param.projectId}" ></x:set>
    <x:set name="wormsProjectOpen.roleTable" property="projectId" value="${param.projectId}" ></x:set>
</c:if>
<c-rt:set var="forward_success" value="<%= ProjectForm.FORWARD_SUCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= ProjectForm.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_add" value="<%= RoleTable.FORWARD_ADD %>"/>
<c:if test="${forward.name == forward_success}">
    <script>
        alert("<fmt:message key='project.message.projectUpdated'/>");
        document.location="<c:url value="/ekms/worms/project.jsp"/>";
    </script>
</c:if>
<%--<c:if test="${forward.name == forward_add}">
    <script>
        window.open("<c:url value="/ekms/worms/roleAdd.jsp"/>?projectId=<c:out value="${widgets['wormsProjectOpen.roleTable'].projectId}"/>&currentlyTemplate=false", "roleWindow", "height=500,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>
<c:if test="${!empty param.roleId}">
    <script>
        window.open("<c:url value="/ekms/worms/roleOpen.jsp"/>?roleId=<c:out value="${param.roleId}"/>&currentlyTemplate=false", "roleWindow", "height=500,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>--%>
<c:if test="${forward.name == forward_cancel}">
    <c:redirect url="/ekms/worms/project.jsp"/>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.projectSchedule'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            <br>
            <table cellpadding="2" cellspacing="0" width="95%" align="center">
                <c:choose>
                    <c:when test="${!empty param.projectId}">
                        <c:set var="propertyValue" value="projectId=${param.projectId}&type=${widgets['wormsProjectOpen.view'].type}&viewType=all"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="propertyValue" value="projectId=${widgets['wormsProjectOpen.roleTable'].projectId}&type=${widgets['wormsProjectOpen.view'].type}&viewType=all"/>
                    </c:otherwise>
                </c:choose>
                <tr><td align="left">
                <x:display name="wormsProjectOpen.view"/>
                    </td></tr>                
                <tr><td align="left">
                    <input type="button" value="<fmt:message key='project.label.editProjectSchedule'/>" onClick="document.location='<c:url value="/ekms/worms/projectSchedule.jsp"/>?projectId=<c:out value="${widgets['wormsProjectOpen.form'].projectId}"/>';" class="button">
                    <input type="button" value="<fmt:message key='project.label.detachProjectSchedule'/>" class="button" onClick="window.open('<c:url value="/ekms/worms/projectPrint.jsp?projectId=${widgets['wormsProjectOpen.form'].projectId}&print=false"/>');"/>
                </td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td><form><x:template type="com.tms.collab.project.ui.ProjectChart" properties="${propertyValue}"/></form></td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td align="left">
                    <input type="button" value="<fmt:message key='project.label.editProjectSchedule'/>" onClick="document.location='<c:url value="/ekms/worms/projectSchedule.jsp"/>?projectId=<c:out value="${widgets['wormsProjectOpen.form'].projectId}"/>';" class="button">
                    <input type="button" value="<fmt:message key='project.label.detachProjectSchedule'/>" class="button" onClick="window.open('<c:url value="/ekms/worms/projectPrint.jsp?projectId=${widgets['wormsProjectOpen.form'].projectId}&print=false"/>');"/>
                </td></tr>
            </table>
        </td>
    </tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
	<tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.projectRoles'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            <table cellpadding="4" cellspacing="0" nowrap>
                <tr><td><x:display name="wormsProjectOpen.roleTable"/></td></tr>
            </table>
        </td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.updatingAProject'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            <table cellpadding="0" cellspacing="0" width="600" nowrap>
                <tr>
                    <td align="center">
                        <x:display name="wormsProjectOpen.form"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>