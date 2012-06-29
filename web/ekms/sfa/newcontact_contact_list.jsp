<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, com.tms.crm.sales.misc.*" %>

<x:config>
    <page name="jsp_contactList_commain">
<%--
     	<com.tms.crm.sales.ui.ContactTable name="table1" type="Contact_NoTieOpportunity" width="100%" />
--%>
     	<com.tms.crm.sales.ui.ContactForm name="form1" type="Add" width="100%" />
    </page>
</x:config>


<c:if test="${forward.name == 'contactAdded'}" >
    <script>
        alert("<fmt:message key='sfa.message.newcontactadded'/>!");
		location = "contact_listing.jsp";
    </script>
</c:if>



<c:choose>
	<c:when test="${not empty(param.companyID)}">
		<c:set var="companyID" value="${param.companyID}" />
	</c:when>
	<c:otherwise>
		<c:set var="companyID" value="${widgets['jsp_contactList_commain.table1'].companyID}" />
	</c:otherwise>
</c:choose>

<c:if test="${forward.name == 'cancel'}">
    <c:redirect url="/ekms/sfa/contact_listing.jsp" /> 
</c:if> <%--
<x:set name="jsp_contactList_commain.table1" property="companyID" value="${companyID}" />
<x:set name="jsp_contactList_commain.table1" property="linkUrl" value="contact_view_commain.jsp" />
--%>
<x:set name="jsp_contactList_commain.form1" property="companyID" value="${companyID}" />
<x:set name="jsp_contactList_commain.form1" property="opportunityID" value="" />


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
           <fmt:message key='sfa.message.menuCompanies'/> > <fmt:message key='sfa.message.addNewContact'/>
    </td>
    </tr>
  <%--  <tr>
    <td class="sfaRow">
	<c:set var="navSelected" value="1" scope="request"/>
	<jsp:include page="includes/navAddCompany.jsp"/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<x:display name="jsp_contactList_commain.table1"></x:display>
	</td>
    </tr>
    </table>

    <br>


<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader"><fmt:message key='sfa.message.newContact'/>    </td></tr>--%>
    <tr>
    <td class="sfaRow">

	<x:display name="jsp_contactList_commain.form1"></x:display>
	    </td></tr>
    <tr>
    <td class="sfaFooter">
                         &nbsp;
    </td>
    </tr>
    </table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
