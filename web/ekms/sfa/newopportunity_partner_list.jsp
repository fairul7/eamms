<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_partnerList">
     	<com.tms.crm.sales.ui.CompanyTable name="table1" type="Partner_List" width="100%" />
     	<com.tms.crm.sales.ui.CompanyForm name="form1" type="Add" subType="Partner" width="100%" />
    </page>
</x:config>


<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_partnerList.table1'].opportunityID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_partnerList.table1" property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_partnerList.table1" property="linkUrl" value="partner_view.jsp?opportunityID=${opportunityID}&lulu=lala" />
<x:set name="jsp_partnerList.form1" property="opportunityID" value="${opportunityID}" />
<c:choose>
	<c:when test="${forward.name == 'companyAdded'}">
		<c:redirect url="newopportunity_partnercontact_list.jsp?opportunityID=${opportunityID}"/>
	</c:when>
	<c:when test="${forward.name == 'companyDuplicate'}">
		<script>
		<!--
			alert ("<fmt:message key='sfa.message.addRecordErrorDuplicated'/>.");
			location = "partner_list.jsp?opportunityID=<c:out value="${opportunityID}"/>";
		//-->
		</script>
	</c:when>
	<c:when test="${forward.name == 'selectCompany'}">
		<c:redirect url="newopportunity_partnercontact_list.jsp?opportunityID=${opportunityID}"/>
	</c:when>
</c:choose>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
         <fmt:message key='sfa.message.addinganOpportunity'/>
   </td>
      </tr>
      <tr>
      <td class="sfaRow">

	<c:set var="navSelected" value="5" scope="request"/>
	<jsp:include page="includes/navAddOpportunity.jsp"/>
	</td>
      </tr>
      <tr>
      <td class="sfaRow">
	<c:set var="infoIndex" value="1" scope="request"/>
	<c:set var="opportunityID" value="${opportunityID}" scope="request"/>
    <c:set var="productsSelected" value="true" scope="request"/>
	<jsp:include page="includes/navInfoOpportunity.jsp"/>
	</td>
      </tr>
      <tr>
      <td class="sfaRow">

	<x:display name="jsp_partnerList.table1"></x:display>
	</td>
      </tr>
    </table>
	<br>


    <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
        <tr valign="top">
            <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='sfa.message.newPartner'/>
   </td>
      </tr>
      <tr>
      <td class="sfaRow">
	<x:display name="jsp_partnerList.form1"></x:display>
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
