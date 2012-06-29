<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, com.tms.crm.sales.misc.*" %>

<x:config>
    <page name="jsp_contactList">
     	<com.tms.crm.sales.ui.ContactTable name="table1" type="Company_Contacts" width="100%" />
     	<com.tms.crm.sales.ui.ContactForm name="form1" type="Add" width="100%" />
    </page>
</x:config>


<%-- // TODO: Remove the extra 'lulu' in the url --%>

<c:if test="${not empty param.clear}" >
    <x:set name="jsp_contactList.table1" property="clear" value="1" />
</c:if>

<c:if test="${not empty param.companyId}" >
    <x:set name="jsp_contactList.table1" property="companyID" value="${param.companyId}" />
    <x:set name="jsp_contactList.form1" property="companyID" value="${param.companyId}" />
</c:if>

<c:if test="${not empty param.contactID}" >
 <%--   <script>
       window.open("popcontactview.jsp?contactID=${param.contactID}","","resizable=yes,width=450,height=400,scrollbars=yes");
   </script>

--%>

    <c:redirect url="contact_view.jsp?contactID=${param.contactID}&backUrl=contact_list.jsp"/>

</c:if>

<c:if test="${forward.name == 'contactAdded'}" >
    <c:set var="table" value="${widgets['jsp_contactList.table1']}"/>
    <c:set var="contactId" value="${widgets['jsp_contactList.form1'].justCreatedID}"/>
    <%
        ContactTable table = (ContactTable)pageContext.getAttribute("table");
        Collection col = table.getContactList();
        col.add(pageContext.getAttribute("contactId"));
    %>
</c:if>

<c:if test="${forward.name == 'contactsSelected' }" >
    <c:set var="companyId" value="${widgets['jsp_contactList.table1'].companyID}"/>
    <%
        session.setAttribute("companyId",pageContext.getAttribute("companyId"));
    %>
    <c:redirect url="newopportunity_contact_type.jsp" />

<%--
    <c:redirect url="opportunity_list.jsp?companyID=${companyId}"/>
--%>

</c:if>

<%--
<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_contactList.table1'].opportunityID}" />
	</c:otherwise>
</c:choose>
--%>
<%--
<x:set name="jsp_contactList.table1" property="opportunityID" value="${opportunityID}" />
--%>
<%--
<x:set name="jsp_contactList.table1" property="linkUrl" value="contact_view.jsp?opportunityID=${opportunityID}&lulu=lala" />
<x:set name="jsp_contactList.form1" property="opportunityID" value="${opportunityID}" />
--%>

<%--<c:choose>
	<c:when test="${widgets['jsp_contactList.table1'].justTiedContact}">
		<c:redirect url="opportunity_tiecontact.jsp?opportunityID=${opportunityID}" />
	</c:when>
</c:choose>--%>
		

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
        <c:set var="navSelected" value="1" scope="request"/>
      <fmt:message key='sfa.message.addinganOpportunity'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">	<jsp:include page="includes/navAddOpportunity.jsp"/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<c:set var="infoIndex" value="1" scope="request"/>

	<x:display name="jsp_contactList.table1"></x:display>
    </td>
    </tr>

        </table>
        <br>

   <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">

    <tr>
    <td class="sfaHeader">

    <fmt:message key='sfa.message.newContact'/>
        </td>
        </tr>
        <tr>
        <td class="sfaRow">
	<x:display name="jsp_contactList.form1"></x:display>
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
