<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*,
                com.tms.crm.sales.ui.ContactTable" %>

<%@ include file="includes/checkHelpdeskPermission.jsp"%>

<x:config>
    <page name="incidentContact">
     	<com.tms.crm.sales.ui.ContactTable name="table1" type="Company_Contacts" width="100%" multipleSelect="false" />
     	<com.tms.crm.sales.ui.ContactForm name="form1" type="Add" width="100%" />
    </page>
</x:config>


<c:if test="${not empty param.clear}" >
    <x:set name="incidentContact.table1" property="clear" value="1" />
</c:if>

<c:if test="${not empty param.companyId}" >
    <x:set name="incidentContact.table1" property="companyID" value="${param.companyId}" />
    <x:set name="incidentContact.form1" property="companyID" value="${param.companyId}" />
</c:if>

<c:if test="${not empty param.contactID}" >
    <c:redirect url="incidentContactView.jsp?contactID=${param.contactID}&backUrl=contact_list.jsp"/>
</c:if>

<c:if test="${forward.name == 'contactAdded'}" >
    <c:set var="table" value="${widgets['incidentContact.table1']}"/>
    <c:set var="contactId" value="${widgets['incidentContact.form1'].justCreatedID}"/>
    <%
        ContactTable table = (ContactTable)pageContext.getAttribute("table");
        Collection col = table.getContactList();
        col.add(pageContext.getAttribute("contactId"));
    %>
</c:if>

<c:if test="${forward.name == 'contactsSelected' }" >
    <c:set var="companyId" value="${widgets['incidentContact.table1'].companyID}" />
    <c:set var="contactList" value="${widgets['incidentContact.table1'].contactList}"/>
    <%
        // retrieve selected company and contact
        Collection contactList = (Collection)pageContext.getAttribute("contactList");
        String contactId = null;
        if (contactList != null) {
            contactId = (String)contactList.iterator().next();
            pageContext.setAttribute("contactId", contactId);
        }
    %>
    <c:redirect url="incidentAdd.jsp?companyId=${companyId}&contactId=${contactId}" />
</c:if>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
        <c:set var="navSelected" value="1" scope="request"/>
        <fmt:message key="com.tms.crm.helpdesk.HelpdeskHandler"/> > <fmt:message key='helpdesk.message.newIncident'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
        <table border="0" cellspacing="0" cellpadding="0">
        <tr>
        <td>
            <b>&nbsp;<fmt:message key="helpdesk.label.customer"/></b>
             <font color="#990000">&gt; <fmt:message key="helpdesk.label.selectContact"/></font>
             |
        </td>
        <td>
            &nbsp;<fmt:message key="helpdesk.label.incident"/>
        </td>
        </tr>
        </table>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<c:set var="infoIndex" value="1" scope="request"/>

	<x:display name="incidentContact.table1"></x:display>
    </td>
    </tr>

        </table>
        <br>

   <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">

    <tr>
    <td class="sfaHeader">

     <fmt:message key="com.tms.crm.helpdesk.HelpdeskHandler"/> > <fmt:message key='sfa.message.newContact'/>
        </td>
        </tr>
        <tr>
        <td class="sfaRow">
	<x:display name="incidentContact.form1"></x:display>
       </td>
       </tr>
    </table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
