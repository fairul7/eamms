<%@ page import="com.tms.collab.project.ui.ProjectForm"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.worms.project.Project.add" module="com.tms.collab.project.WormsHandler" url="/ekms/index.jsp"/>
<x:config>
    <page name="wormsProjectAdd">
        <com.tms.collab.project.ui.ProjectFormAdd name="form"/>
    </page>
</x:config>
<c-rt:set var="forward_success" value="<%= ProjectForm.FORWARD_SUCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= ProjectForm.FORWARD_CANCEL %>"/>
<c:if test="${forward.name == forward_success}">
    <script>
        alert("<fmt:message key='project.message.projectAdded'/>");
        document.location="<c:url value="/ekms/worms/projectOpen.jsp?projectId=${widgets['wormsProjectAdd.form'].projectId}"/>";
    </script>
</c:if>
<c:if test="${forward.name == forward_cancel}">
    <c:redirect url="/ekms/worms/project.jsp"/>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.manageProjects'/> > <fmt:message key='project.label.addNewProject'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsProjectAdd.form"/></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>
