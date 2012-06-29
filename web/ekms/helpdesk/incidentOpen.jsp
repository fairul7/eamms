<%@ page import="com.tms.crm.helpdesk.ui.IncidentForm"%>
<%@ include file="/common/header.jsp" %>
<%@ include file="includes/checkHelpdeskPermission.jsp"%>


<script>
	function popup(url) {
    	var myWin = window.open(url, 'windowName', 'scrollbars=yes,status=0,resizable=1,width=650,height=380,left=250,top=150');
        if (myWin != null) {
          	myWin.focus();
        }
   }
</script>


<x:config>
	<page name="helpdeskincidentOpen">
		<com.tms.crm.helpdesk.ui.IncidentOpen name="form"/>
	</page>
</x:config>

<c:if test="${!empty param.incidentId}">
	<x:set name="helpdeskincidentOpen.form" property="incidentId" value="${param.incidentId}"/>
</c:if>
<c-rt:set var="forward_success" value="<%= IncidentForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_escalated" value="<%= IncidentForm.FORWARD_ESCALATED %>"/>
<c-rt:set var="forward_resolved" value="<%= IncidentForm.FORWARD_RESOLVED %>"/>
<c-rt:set var="forward_reopen" value="<%= IncidentForm.FORWARD_REOPEN %>"/>
<c-rt:set var="forward_cancel" value="<%= IncidentForm.FORWARD_CANCEL %>"/>
<c:if test="${!empty forward.name}">
	<script>
		<c:choose>
			<c:when test="${forward_success == forward.name}">
				alert("<fmt:message key='helpdesk.message.incidentUpdated'/>");
			</c:when>
			<c:when test="${forward_escalated == forward.name}">
			alert("<fmt:message key='helpdesk.message.incidentEscalated'/>");
			</c:when>

			<c:when test="${forward_resolved == forward.name}">
				alert("<fmt:message key='helpdesk.message.incidentResolved'/>");
			</c:when>
			<c:when test="${forward_reopen == forward.name}">
				alert("<fmt:message key='helpdesk.message.incidentReopened'/>");
			</c:when>
		</c:choose>
		document.location="<c:url value="/ekms/helpdesk/"/>";

	</script>
</c:if>

<c:set var="incidentOpenForm"><x:display name="helpdeskincidentOpen.form"/></c:set>
<c:set var="incident" value="${widgets['helpdeskincidentOpen.form'].incident}"/>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" class="contentBgColor" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" class="contentTitleFont">&nbsp;&nbsp;&nbsp;<fmt:message key='helpdesk.message.updatingIncident'/></b></td>
        <td align="right" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="top" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
    <td colspan="2" valign="top">
        <table width="100%" cellpadding="5" cellspacing="1" border="0">
        <tr>
        <td class="contentBgColor" width="20%" valign="top" align="right">
            <b><fmt:message key="helpdesk.label.incidentNo"/></b>
        </td>
        <td class="contentBgColor" width="80%" valign="top">
            #<fmt:formatNumber pattern="000" value="${incident.incidentCode}"/>
        </td>
        </tr>
        <tr>
        <td class="contentBgColor" width="20%" valign="top" align="right">
            <b><fmt:message key="helpdesk.label.company"/></b>
        </td>
        <td class="contentBgColor" width="80%" valign="top">
            <a href="<c:url value='/ekms/helpdesk/companyView.jsp?cn=jsp_companyList_commain.table1&et=sel&companyID=${incident.companyId}'/>">
            <c:out value="${incident.companyName}"/></a>
        </td>
        </tr>
        <tr>
        <td class="contentBgColor" width="20%" valign="top" align="right">
            <b><fmt:message key="helpdesk.label.contact"/></b>
        </td>
        <td class="contentBgColor" width="80%" valign="top">
            <a href="<c:url value='/ekms/helpdesk/contactDetails.jsp?contactID=${incident.contactId}'/>">
            <c:out value="${incident.contactFirstName} ${incident.contactLastName}"/></a>
        </td>
        </tr>
        </table>
    </td>
    <tr><td colspan="2" valign="top"><c:out value="${incidentOpenForm}" escapeXml="false" /></td></tr>
<%--     <tr><td colspan="2" valign="top" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr> --%>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>