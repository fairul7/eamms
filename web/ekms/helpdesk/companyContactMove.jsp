<%@ page import="java.util.Collection,
                 java.util.Iterator,
                 com.tms.crm.sales.model.ContactModule,
                 com.tms.crm.sales.model.Contact,
                 com.tms.crm.sales.ui.MoveContactCompanyTable"%>
<%@ include file="/common/header.jsp" %>

<%@ include file="includes/checkHelpdeskPermission.jsp"%>

<x:config >
    <page  name="jsp_movecontact">
        <com.tms.crm.sales.ui.MoveContactCompanyTable name="table1" width="100%" />
        <com.tms.crm.sales.ui.CompanyForm name="form1" type="Add" width="100%" />
        </page>
</x:config>


<c-rt:set var="contactsMoved" value="<%=MoveContactCompanyTable.FORWARD_CONTACTS_MOVED%>"/>

<c:if test="${forward.name == contactsMoved}" >
    <script>
        alert("<fmt:message key='sfa.message.contactsmoved'/>!");
        location =  "companyView.jsp?companyID=<c:out value="${widgets['jsp_movecontact.table1'].selectedCompanyID}"/>";
    </script>
</c:if>




<x:set name="jsp_movecontact.table1" property="contactList" value="${widgets['jsp_companyviewCommain.contactTable'].contactList}"/> 

<c:if test="${forward.name =='companyAdded' }">

    <x:set name="jsp_movecontact.table1" property="selectedCompanyID" value="${widgets['jsp_movecontact.form1'].justCreatedID}"/>
</c:if>




<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
             <fmt:message key='sfa.message.moveContacts'/>



    </td>
    </tr>

    <tr>
    <td class="sfaRow">
        <B><fmt:message key='sfa.message.selectedContacts'/>:</b><br><br>
        <c:set var="contactList" value="${widgets['jsp_companyviewCommain.contactTable'].contactList}"/>
        <%
            Collection col = (Collection)pageContext.getAttribute("contactList");
            if(col!=null){
                ContactModule cm = (ContactModule) Application.getInstance().getModule(ContactModule.class);
                Contact contact;
                for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                    String contactId = (String) iterator.next();
                    contact = cm.getContact(contactId);
                    out.print("<li>"+contact.getContactFirstName() + " " + contact.getContactLastName()+"</li>");
                }
            }
        %>  <br>
    </td>
    </tr>

    <tr>
    <td class="sfaRow">
        <B><fmt:message key='sfa.message.selectCompany'/></b><br>
        <x:display name="jsp_movecontact.table1" />
    </td>
    </tr>
    <table>
    <br>

  <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
      <tr valign="top">
          <td align="left" valign="top" class="sfaHeader">
          <fmt:message key='sfa.message.newCompany'/>
          </td>

       </tr>
      <tr>
      <td class="sfaRow">
          <x:display name="jsp_movecontact.form1" />
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
