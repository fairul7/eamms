<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, com.tms.crm.sales.misc.*" %>

<%@ include file="includes/checkHelpdeskPermission.jsp"%>

<x:config>
    <page name="jsp_contactList_oppdetails">
     	<com.tms.crm.sales.ui.ContactTable name="table1" type="Contact_NoTieOpportunity" template="table" width="100%" />
     	<com.tms.crm.sales.ui.ContactForm name="form1" type="Add" width="100%" />
    </page>
</x:config>


<c:if test="${! empty param.contactID}" >
    <c:redirect url="/ekms/helpdesk/contactDetails.jsp?contactID=${param.contactID}" />

</c:if>

<%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key="helpdesk.label.customer"/> > <fmt:message key='sfa.message.contactListing'/>
                </td>
            </tr>
	 <tr>
    <td class="sfaRow">
	<x:display name="jsp_contactList_oppdetails.table1"></x:display>
     </td>
    </tr>

   
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
