<%@ include file="/common/header.jsp" %>

<c:set var="opportunityID" value="${param.opportunityID}" scope="request"/>
<c:set var="contactID" value="${param.contactID}" scope="request"/>
<c:set var="editURL" value="newopportunity_partnercontact_edit.jsp" scope="request"/>
<c:set var="backURL" value="newopportunity_partnercontact_list.jsp" scope="request"/>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
<fmt:message key='sfa.message.viewPartnerContact'/>
    </td>
     </tr>
         <tr>
          <td class="sfaRow">
	<jsp:include page="includes/form_contactView.jsp"/>
  
          </td>
    </tr>
    <tr>
        <td class="sfaFooter">&nbsp;
        </td>

    </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
