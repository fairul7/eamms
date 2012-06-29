<%@ page import="com.tms.collab.project.ui.TemplateForm"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.worms.project.Project.administer" module="com.tms.collab.project.WormsHandler" url="/ekms/index.jsp"/>
<x:config>
    <page name="wormsTemplateAdd">
        <com.tms.collab.project.ui.TemplateAdd name="form"/>
    </page>
</x:config>
<c-rt:set var="forward_success" value="<%= TemplateForm.FORWARD_SUCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= TemplateForm.FORWARD_CANCEL %>"/>
<c:if test="${forward_success == forward.name}">
    <script>
        alert("<fmt:message key='project.message.templateCreated'/>");
        document.location="<c:url value="/ekms/worms/templateOpen.jsp?templateId=${widgets['wormsTemplateAdd.form'].templateId}"/>";
    </script>
</c:if>
<c:if test="${forward_cancel == forward.name}">
    <c:redirect url="/ekms/worms/template.jsp"/>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.projectTemplates'/> > <fmt:message key='project.label.addNewProjectTemplate'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsTemplateAdd.form"/></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>
