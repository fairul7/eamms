<%@ include file="/common/header.jsp" %>

<c:set var="companyID" value="${param.companyID}" scope="request"/>
<c:set var="onUpdateURL" value="company_view_commain.jsp" scope="request"/>
<c:set var="backURL" value="company_view_commain.jsp" scope="request"/>


<c:if test="${forward.name =='cancel'}" >
    <c:redirect url="/ekms/sfa/company_view_commain.jsp" />
</c:if>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
<fmt:message key='sfa.message.editCompany'/>
                 </td>
                  </tr>
                  <tr>
                  <td class="sfaRow">

	<jsp:include page="includes/form_companyEdit.jsp"/>

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
