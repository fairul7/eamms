<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="competencyuserslist">
        <com.tms.hr.competency.ui.CompetencyUsersList name="table"/>
    </page>
</x:config>


    <%@ include file="/ekms/includes/header.jsp" %>
    <jsp:include page="includes/header.jsp"/>


    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63"
                                                                                class="contentTitleFont"><fmt:message key='project.label.competencies'/> > <fmt:message key='com.tms.hr.competency.userCompetencies'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img
                src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"><x:display name="competencyuserslist.table" /></td></tr>


    </table>


<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>