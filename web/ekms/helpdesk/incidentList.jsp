<%@ page import="com.tms.crm.helpdesk.ui.IncidentTable"%>
<%@ include file="/common/header.jsp" %>

<%@ include file="includes/checkHelpdeskPermission.jsp"%>

<x:config>
	<page name="helpdeskIncident">
		<com.tms.crm.helpdesk.ui.IncidentTable name="table" sort="resolved"/>
	</page>
</x:config>
<c-rt:set var="forward_add" value="<%= IncidentTable.FORWARD_ADD %>"/>
<c:if test="${forward_add == forward.name}">
	<c:redirect url="/ekms/helpdesk/incidentAdd.jsp"/>
</c:if>
<c:if test="${!empty param.incidentId}">
	<c:redirect url="/ekms/helpdesk/incidentOpen.jsp?incidentId=${param.incidentId}"/>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="003366" class="contentTitleFont"><b><font color="FFCF63" class="contentTitleFont">&nbsp;&nbsp;&nbsp;
            <fmt:message key="com.tms.crm.helpdesk.HelpdeskHandler"/> > <fmt:message key="helpdesk.label.listIncidents"/></font></b></td>
        <td align="right" bgcolor="003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="top" bgcolor="EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="top" bgcolor="CCCCCC" class="contentBgColor"><x:display name="helpdeskIncident.table"/></td></tr>
    <tr><td colspan="2" valign="top" bgcolor="CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>