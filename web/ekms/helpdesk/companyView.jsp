<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.crm.sales.misc.*, kacang.services.security.User,
                 com.tms.crm.sales.ui.ContactSimpleTable" %>

<%@ include file="includes/checkHelpdeskPermission.jsp"%>

<x:config>
    <page name="jsp_companyviewCommain">
        <com.tms.crm.sales.ui.ContactSimpleTable name="contactTable" type="Company_Contacts" width="100%"/>
		<com.tms.crm.helpdesk.ui.IncidentTable name="incidentTable" sort="resolved"/>
    </page>
</x:config>


<c:if test="${!empty param.contactID}">
    <c:redirect url="/ekms/helpdesk/companyContactView.jsp?contactID=${param.contactID}"/>
</c:if>
<c:if test="${!empty param.incidentId}">
    <c:redirect url="/ekms/helpdesk/incidentOpen.jsp?incidentId=${param.incidentId}"/>
</c:if>

<c-rt:set var="movecontact" value="<%=ContactSimpleTable.FORWARD_MOVE_CONTACT%>"/>

<c:if test="${forward.name == 'movecontact'}" >
    <c:redirect url="/ekms/helpdesk/companyContactMoveCheck.jsp" />
</c:if>

<c:choose>
	<c:when test="${not empty (param.companyID)}">
		<c:set var="companyID" value="${param.companyID}" scope="request"/>
	</c:when>
	<c:otherwise>
		<c:set var="companyID" value="${widgets['jsp_companyviewCommain.contactTable'].companyID}" scope="request"/>
	</c:otherwise>
</c:choose>
<x:set name="jsp_companyviewCommain.contactTable" property="companyID" value="${companyID}" />
<x:set name="jsp_companyviewCommain.incidentTable" property="companyId" value="${companyID}" />

<c:set var="editURL" value="companyEdit.jsp" scope="request"/>
<c:choose>
<c:when test="${! empty param.backURL}">
<c:set var="backURL" value="${param.backURL}" scope="request"/>
</c:when>
<c:otherwise >
<c:set var="backURL" value="" scope="request"/>
</c:otherwise>
</c:choose>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
    <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
        <tr valign="top">
            <td align="left" valign="top" class="sfaHeader">
       <fmt:message key="helpdesk.label.customer"/> > <fmt:message key='sfa.message.viewCompany'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<jsp:include page="includes/form_companyView.jsp"/>
    </td>
    </tr>
    </table>

    <br>
   <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
   <tr valign="top">
       <td align="left" valign="top" class="sfaHeader">
       <fmt:message key="helpdesk.label.customer"/> > <fmt:message key='sfa.message.viewContacts'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
        <x:display name="jsp_companyviewCommain.contactTable" />
        <br>
        <input type="button" class="button" value="<fmt:message key='sfa.message.addNewContact'/>" onClick="location = 'companyContactNew.jsp?companyID=<c:out value="${companyID}"/>'"/>
    </td>

    </table>
    <br>
    <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
           <fmt:message key="helpdesk.label.customer"/> > <fmt:message key='helpdesk.label.viewIncidents'/>
        <tr>
        <td class="sfaRow">
            <x:display name="jsp_companyviewCommain.incidentTable" />
            <br>
            <input type="button" class="button" value="<fmt:message key='helpdesk.message.newIncident'/>" onClick="location='incidentContact.jsp?companyId=<c:out value="${companyID}"/>'"/>
         </td>
    </tr>
    
    </table>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>