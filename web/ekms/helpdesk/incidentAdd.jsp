<%@ page import="com.tms.crm.helpdesk.ui.IncidentForm,
                 java.util.Collection,
                 com.tms.crm.helpdesk.Incident"%>
<%@ include file="/common/header.jsp" %>

<%@ include file="includes/checkHelpdeskPermission.jsp"%>

<x:config>
	<page name="helpdeskincidentAdd">
		<com.tms.crm.helpdesk.ui.IncidentAdd name="form"/>
	</page>
</x:config>

<c-rt:set var="forward_success" value="<%= IncidentForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_cancel" value="<%= IncidentForm.FORWARD_CANCEL %>"/>
<c:if test="${forward_success == forward.name}">
	<script>
		alert("<fmt:message key="helpdesk.message.incidentCreated"/>");
		document.location="<c:url value="/ekms/helpdesk/"/>";
	</script>
</c:if>
<c:if test="${forward_cancel == forward.name}">
	<script>document.location="<c:url value="/ekms/helpdesk/"/>";</script>
</c:if>

<c:if test="${!empty param.companyId}">
    <x:set name="helpdeskincidentAdd.form" property="companyId" value="${param.companyId}"/>
    <x:set name="helpdeskincidentAdd.form" property="contactId" value="${param.contactId}"/>
</c:if>
<c:set var="companyId" value="${widgets['helpdeskincidentAdd.form'].companyId}"/>
<c:set var="contactId" value="${widgets['helpdeskincidentAdd.form'].contactId}"/>
<%
    // retrieve selected company and contact
    String companyId = (String)pageContext.getAttribute("companyId");
    String contactId = (String)pageContext.getAttribute("contactId");
    Incident incident = new Incident();
    incident.populateCompanyId(companyId);
    incident.populateContactId(contactId);
    pageContext.setAttribute("incident", incident);
%>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" class="contentBgColor" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" class="contentTitleFont">&nbsp;&nbsp;&nbsp;<fmt:message key="helpdesk.message.newIncident"/></b></td>
        <td align="right" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="top" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="5"></td></tr>
    <tr>
    <td class="classRow">
        <table border="0" cellspacing="0" cellpadding="0">
        <tr>
        <td>
            &nbsp;<fmt:message key="helpdesk.label.customer"/>
            |
        </td>
        <td>
            <b>&nbsp;<fmt:message key="helpdesk.label.incident"/></b>
            <font color="#990000">&gt; <fmt:message key="helpdesk.label.incidentDetails"/></font>
        </td>
        </tr>
        </table>
    </td>
    </tr>
    <tr>
    <td colspan="2" valign="top">
        <table width="100%" cellpadding="5" cellspacing="1" border="0">
        <tr>
        <td class="contentBgColor" width="20%" valign="top" align="right">
            <b><fmt:message key="helpdesk.label.company"/></b>
        </td>
        <td class="contentBgColor" width="80%" valign="top">
            <c:out value="${incident.companyName}"/>
        </td>
        </tr>
        <tr>
        <td class="contentBgColor" width="20%" valign="top" align="right">
            <b><fmt:message key="helpdesk.label.contact"/></b>
        </td>
        <td class="contentBgColor" width="80%" valign="top">
            <c:out value="${incident.contactFirstName} ${incident.contactLastName}"/>
        </td>
        </tr>
        </table>
    </td>
    </tr>
    <tr>
    <td colspan="2" valign="top">
        <x:display name="helpdeskincidentAdd.form"/>
    </td>
    </tr>
<%--    <tr><td colspan="2" valign="top" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr> --%>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>