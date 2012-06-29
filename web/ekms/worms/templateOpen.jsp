<%@ page import="com.tms.collab.project.ui.TemplateForm,
                 com.tms.collab.project.ui.RoleTable,
                 com.tms.collab.project.ui.ProjectSchedule"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.worms.project.Project.administer" module="com.tms.collab.project.WormsHandler" url="/ekms/index.jsp"/>
<x:config>
    <page name="wormsTemplateOpen">
        <com.tms.collab.project.ui.TemplateOpen name="form"/>
        <com.tms.collab.project.ui.RoleTable name="roleTable"/>
        <com.tms.collab.project.ui.ProjectSchedule name="schedule"/>
    </page>
</x:config>
<c-rt:set var="forward_success" value="<%= TemplateForm.FORWARD_SUCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= TemplateForm.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_add" value="<%= RoleTable.FORWARD_ADD %>"/>
<c-rt:set var="forward_add_milestone" value="<%= ProjectSchedule.FORWARD_ADD_MILESTONE %>"/>
<c-rt:set var="forward_add_task" value="<%= ProjectSchedule.FORWARD_ADD_TASK %>"/>
<c-rt:set var="forward_select" value="<%= ProjectSchedule.FORWARD_SELECTION %>"/>
<c:if test="${!empty param.templateId}">
    <x:set name="wormsTemplateOpen.form" property="templateId" value="${param.templateId}"/>
    <x:set name="wormsTemplateOpen.roleTable" property="templateId" value="${param.templateId}"/>
    <x:set name="wormsTemplateOpen.schedule" property="templateId" value="${param.templateId}"/>
</c:if>
<c:if test="${forward_success == forward.name}">
    <script>
        alert("Template Updated");
        document.location="<c:url value="/ekms/worms/template.jsp"/>";
    </script>
</c:if>
<c:if test="${!empty param.roleId}">
    <script>
        window.open("<c:url value="/ekms/worms/roleOpen.jsp"/>?roleId=<c:out value="${param.roleId}"/>&currentlyTemplate=true", "roleWindow", "height=400,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>
<c:if test="${!empty param.descId}">
	<script>window.open("<c:url value="/ekms/worms/descriptorOpen.jsp"/>?descId=<c:out value="${param.descId}"/>", "descriptorWindow", "height=450,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
</c:if>
<%-- Event Handling --%>
<c:if test="${forward_cancel == forward.name}">
    <c:redirect url="/ekms/worms/template.jsp"/>
</c:if>
<c:if test="${forward_add == forward.name}">
    <script>
        window.open("<c:url value="/ekms/worms/roleAdd.jsp"/>?projectId=<c:out value="${widgets['wormsTemplateOpen.roleTable'].templateId}"/>&currentlyTemplate=true", "roleWindow", "height=400,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>
<c:if test="${forward_add_milestone == forward.name}">
    <script>window.open("<c:url value="/ekms/worms/milestoneAdd.jsp"/>?projectId=<c:out value="${widgets['wormsTemplateOpen.form'].templateId}"/>&currentTemplate=true", "milestoneWindow", "height=350,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
</c:if>
<c:if test="${forward_add_task == forward.name}">
    <script>window.open("<c:url value="/ekms/worms/descriptorAdd.jsp"/>?projectId=<c:out value="${widgets['wormsTemplateOpen.form'].templateId}"/>", "descriptorWindow", "height=450,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
</c:if>
<c:if test="${forward_select == forward.name}">
    <c:if test="${!empty param.milestoneId}">
        <script>window.open("<c:url value="/ekms/worms/milestoneOpen.jsp"/>?milestoneId=<c:out value="${param.milestoneId}"/>&currentTemplate=true", "milestoneWindow", "height=350,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
    </c:if>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.projectTemplates'/> > <fmt:message key='project.label.updateProjectTemplate'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsTemplateOpen.form"/></td></tr>
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.roles'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            <table cellpadding="2" cellspacing="0" width="95%" align="center">
                <tr><td><x:display name="wormsTemplateOpen.roleTable"/></td></tr>
            </table>
        </td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.milestone'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            <table cellpadding="2" cellspacing="0" width="95%" align="center">
                <tr><td><x:display name="wormsTemplateOpen.schedule"/></td></tr>
            </table>
        </td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>