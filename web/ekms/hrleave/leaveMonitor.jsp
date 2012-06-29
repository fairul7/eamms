<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="leaveMonitor">
        <com.tms.collab.project.ui.usersListTableUI name="report"/>
    </page>
</x:config>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="../includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;
 <fmt:message key='leave.label.LeaveApp'/> > <fmt:message key='leave.label.leaveMonitor'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="leaveMonitor.report"/></td></tr>
    </tr>
</table>
<jsp:include page="../includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>