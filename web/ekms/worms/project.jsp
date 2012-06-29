<%@ page import="com.tms.collab.project.ui.ProjectTable"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.worms.project.Project.view" module="com.tms.collab.project.WormsHandler" url="/ekms/index.jsp"/>
<x:config>
    <page name="wormsProject">
        <com.tms.collab.project.ui.ProjectTable name="table"/>
    </page>
</x:config>
<c-rt:set var="forward_add" value="<%= ProjectTable.FORWARD_ADD %>"/>
<c:if test="${!(empty param.projectId)}">
    <c:redirect url="/ekms/worms/projectOpen.jsp?projectId=${param.projectId}"/>
</c:if>
<c:if test="${forward.name == forward_add}">
    <c:redirect url="/ekms/worms/projectAdd.jsp"/>
</c:if>
<c:if test="${forward.name == 'noDelete'}">
    <script>
  alert('<fmt:message key='project.label.nopermissiondelete'/>');
  </script>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.projects'/> > <fmt:message key="project.label.manageProjects"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsProject.table"/></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>
