<%@include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.misc.*, com.tms.crm.sales.ui.*" %>

<%@ include file="includes/checkHelpdeskPermission.jsp"%>

<x:config>
    <page name="jsp_contactView_commain">
     	<com.tms.crm.sales.ui.ContactForm name="form1" type="View" width="100%" />
		<com.tms.crm.helpdesk.ui.IncidentTable name="incidentTable" sort="resolved"/>
    </page>
</x:config>

<c:if test="${!empty param.incidentId}">
    <c:redirect url="/ekms/helpdesk/incidentOpen.jsp?incidentId=${param.incidentId}"/>
</c:if>

<c:choose>
	<c:when test="${not empty (param.contactID)}">
		<c:set var="contactID" value="${param.contactID}"/>
	</c:when>
	<c:otherwise>
		<c:set var="contactID" value="${widgets['jsp_contactView_commain.form1'].contactID}"/>
	</c:otherwise>
</c:choose>

<%
	NaviUtil naviUtil = new NaviUtil(pageContext);
	naviUtil.getCompanyID4Contact("contactID", "companyID");
%>
<x:set name="jsp_contactView_commain.form1" property="contactID" value="${contactID}" />
<x:set name="jsp_contactView_commain.incidentTable" property="companyId" value="${companyID}" />
<x:set name="jsp_contactView_commain.incidentTable" property="contactId" value="${contactID}" />

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.viewContact'/>
                </td>
                 </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_contactView_commain.form1"></x:display>
    <input type="button" class="button" value="<fmt:message key='sfa.message.edit'/>" onClick="location = 'companyContactEdit.jsp?contactID=<c:out value="${contactID}" />'"/>
    <input type="button" class="button" value="<fmt:message key='sfa.message.back'/>" onClick="location = 'companyView.jsp'"/>
    </td></tr>
    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

    <br>
    <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='helpdesk.label.viewIncidents'/>
        <tr>
        <td class="sfaRow">
            <x:display name="jsp_contactView_commain.incidentTable" />
            <br>
            <input type="button" class="button" value="<fmt:message key='helpdesk.message.newIncident'/>" onClick="location='incidentAdd.jsp?companyId=<c:out value="${companyID}"/>&contactId=<c:out value="${contactID}"/>'"/>
         </td>
    </tr>
    <tr>
    <td class="sfaFooter">
         &nbsp;
    </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
