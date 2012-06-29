<%@ page import="com.tms.crm.helpdesk.ui.IncidentTable"%>
<%@ include file="/common/header.jsp" %>

<%@ include file="includes/checkHelpdeskPermission.jsp"%>

<x:config>
	<page name="helpdeskIncidentSearch">
		<com.tms.crm.helpdesk.ui.IncidentSearchTable name="table"/>
	</page>
</x:config>

<c:set var="incidentCode" value="${param['helpdeskIncidentSearch.table.filterForm.incidentSearchForm.tfIncidentCode']}"/>
<c:if test="${!empty incidentCode && widgets['helpdeskIncidentSearch.table'].model.totalRowCount == 1}">
    <c:redirect url="incidentOpen.jsp?incidentId=${widgets['helpdeskIncidentSearch.table'].model.tableRows[0].incidentId}"/>
</c:if>


<c:if test="${!empty param.incidentId}">
	<c:redirect url="/ekms/helpdesk/incidentOpen.jsp?incidentId=${param.incidentId}"/>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" class="contentTitleFont"><b><font class="contentTitleFont">&nbsp;&nbsp;&nbsp;
            <fmt:message key="com.tms.crm.helpdesk.HelpdeskHandler"/> > <fmt:message key="helpdesk.label.findIncidents"/></font></b></td>
        <td align="right" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="top" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="top" class="contentBgColor">
        <div style="text-align:right; width:98%"><%-- fmt:message key="helpdesk.label.resultsPerPage"/ --%></div>
        <x:display name="helpdeskIncidentSearch.table"/>
    </td></tr>
    <tr><td colspan="2" valign="top" class="contentStrapColor">
    	<img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15">
    </td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>