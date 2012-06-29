<%@ page import="com.tms.collab.project.ui.TemplateTable"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.worms.project.Project.administer" module="com.tms.collab.project.WormsHandler" url="/ekms/index.jsp"/>
<x:config>
    <page name="wormsTemplate">
        <com.tms.collab.project.ui.TemplateTable name="table"/>
    </page>
</x:config>
<c-rt:set var="forward_add" value="<%= TemplateTable.FORWARD_ADD %>"/>
<c-rt:set var="forward_delete" value="<%= TemplateTable.FORWARD_DELETE %>"/>
<c:if test="${!empty param.templateId}">
    <c:redirect url="/ekms/worms/templateOpen.jsp?templateId=${param.templateId}"/>
</c:if>
<c:if test="${forward_add == forward.name}">
    <c:redirect url="/ekms/worms/templateAdd.jsp"/>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.projects'/> > <fmt:message key='project.label.projectTemplates'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsTemplate.table"/></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>
