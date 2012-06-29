<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.crm.sales.misc.*, kacang.services.security.User,
                 com.tms.crm.sales.ui.ContactSimpleTable" %>

<x:config>
    <page name="jsp_companyviewCommain">
     	<com.tms.crm.sales.ui.OpportunityTable name="table1" type="Company_Filter2" width="100%" />
        <com.tms.crm.sales.ui.ContactSimpleTable name="contactTable" type="Company_Contacts" width="100%"/>
    </page>
</x:config>




<%
	User user = (User) session.getAttribute("currentUser");
	String userID = user.getId();
	boolean isSalesManager = AccessUtil.isSalesManager(userID);
	boolean isSalesPerson  = AccessUtil.isSalesPerson(userID);
%>

<c:if test="${!empty param.contactID}">
    <c:redirect url="/ekms/sfa/contact_view_commain.jsp?contactID=${param.contactID}"/>
</c:if>


<c:if test="${! empty param.opportunityID}" >
    <c:redirect url="/ekms/sfa/opportunity_details.jsp?opportunityID=${param.opportunityID}" />

</c:if>

<c-rt:set var="movecontact" value="<%=ContactSimpleTable.FORWARD_MOVE_CONTACT%>"/>

<c:if test="${forward.name == 'movecontact'}" >
    <c:redirect url="/ekms/sfa/check_move_contacts.jsp" />
</c:if>


<c:choose>
	<c:when test="${not empty (param.companyID)}">
		<c:set var="companyID" value="${param.companyID}"/>
	</c:when>
	<c:otherwise>
		<c:set var="companyID" value="${widgets['jsp_companyviewCommain.table1'].companyID}"/>
	</c:otherwise>
</c:choose>
<x:set name="jsp_companyviewCommain.table1" property="companyID" value="${companyID}" />
<x:set name="jsp_companyviewCommain.table1" property="linkUrl"   value="opportunity_details.jsp" />
<x:set name="jsp_companyviewCommain.contactTable" property="companyID" value="${companyID}" />

<c:set var="companyID" value="${companyID}" scope="request"/>
<c:set var="editURL" value="company_edit_commain.jsp" scope="request"/>
<c:choose>
<c:when test="${! empty param.backURL}">
<c:set var="backURL" value="${param.backURL}" scope="request"/>
</c:when>

<c:otherwise >
<c:set var="backURL" value="companies_listing.jsp" scope="request"/>
</c:otherwise>

</c:choose>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
    <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
        <tr valign="top">
            <td align="left" valign="top" class="sfaHeader">
        <fmt:message key='sfa.message.viewCompany'/>
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
        <fmt:message key='sfa.message.viewContacts'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
        <x:display name="jsp_companyviewCommain.contactTable" />
        <br>
        <input type="button" class="button" value="<fmt:message key='sfa.message.addNewContact'/>" onClick="location = 'newcontact_contact_list.jsp?companyID=<c:out value="${companyID}"/>'"/>


   </td>


<%--
	<% if (isSalesManager || isSalesPerson) { %>
--%>
    </table>
    <br>
    <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
    <fmt:message key='sfa.message.viewOpportunitiesOrSales'/>


            <tr>
            <td class="sfaRow">

		<x:display name="jsp_companyviewCommain.table1"></x:display>
        <br>
        <input type="button" class="button" value="<fmt:message key='sfa.message.addNewOpportunity'/>" onClick="location = 'newopportunity_contact_list.jsp?companyId=<c:out value="${companyID}"/>'"/>
         </td>
    </tr>

<%--
	<% } %>
--%>
    <tr>
    <td class="sfaFooter">
                         &nbsp;
    </td>
    </tr>
    </table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
