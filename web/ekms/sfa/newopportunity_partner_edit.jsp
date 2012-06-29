<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_partnerList_edit">
     	<com.tms.crm.sales.ui.CompanyForm name="form1" type="Edit" subType="Partner" width="100%" />
    </page>
</x:config>




<c:choose>
	<c:when test="${not empty(param.companyID)}">
		<c:set var="companyID" value="${param.companyID}" />
	</c:when>
	<c:otherwise>
		<c:set var="companyID" value="${widgets['jsp_partnerList_edit.form1'].companyID}" />
	</c:otherwise>
</c:choose>
<c:if test="${forward.name=='cancel'}">
    <c:redirect url="/ekms/sfa/partner_view.jsp?companyID=${companyID}" />
</c:if>
<c:if test="${forward.name == 'companyUpdated'}" >
    <c:redirect url="/ekms/sfa/partner_view.jsp?companyID=${companyID}" />
</c:if>

<x:set name="jsp_partnerList_edit.form1" property="companyID" value="${companyID}" />
<c:choose>
	<c:when test="${forward.name == 'companyDuplicate'}"><script>
		<!--
			alert ("<fmt:message key='sfa.message.addRecordErrorDuplicated'/>.");
			location = "partner_view.jsp?opportunityID=<c:out value="${opportunityID}"/>";
		//-->
		</script>
	</c:when>
</c:choose>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
        <fmt:message key='sfa.message.editPartnerDetails'/>
   </td>
      </tr>


      <tr>
      <td class="sfaRow">
	<x:display name="jsp_partnerList_edit.form1"></x:display>
  </td>
      </tr>
      <tr>
      <td class="sfaFooter">
      &nbsp;
  </td>
      </tr>
     </table>
<%--
	<c:set var="buttonBack" value="opportunity_tiecontact.jsp?opportunityID=${opportunityID}" scope="request"/>
	<jsp:include page="includes/navButtons.jsp"/>
--%>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
