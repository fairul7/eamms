<%@ page import="com.tms.hr.competency.ui.CompetencyAdd"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.hr.competency.Competency.view" module="com.tms.hr.competency.CompetencyHandler" url="/ekms/index.jsp"/>
<x:config>
    <page name="wormsCompetencyAdd">
        <com.tms.hr.competency.ui.CompetencyAdd name="form"/>
    </page>
</x:config>
<c-rt:set var="forward_success" value="<%= CompetencyAdd.FORWARD_SUCCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= CompetencyAdd.FORWARD_CANCEL %>"/>
<c:if test="${forward.name == forward_cancel}">
    <c:redirect url="competency.jsp"/>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>
        alert("<fmt:message key='project.message.competencyAdded'/>");
        document.location="<c:url value="competency.jsp"/>";
    </script>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='project.label.competencies'/> > <fmt:message key='project.label.addNewCompetency'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="wormsCompetencyAdd.form" /></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>
