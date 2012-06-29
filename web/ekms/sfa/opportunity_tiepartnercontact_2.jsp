<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, com.tms.crm.sales.misc.*" %>

<x:config>
    <page name="jsp_opportunityTiepartnercontact_2">
        <com.tms.crm.sales.ui.OpportunityContactTieForm name="form1" type="Partner_Tie" width="100%" />
    </page>
</x:config>


<x:set name="jsp_opportunityTiepartnercontact_2.form1" property="contactList" value="${widgets['jsp_partnercontactList_2.table1'].contactList}" />

<% NaviUtil naviUtil = new NaviUtil(pageContext); %>
<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_opportunityTiepartnercontact_2.form1'].opportunityID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_opportunityTiepartnercontact_2.form1" property="opportunityID" value="${opportunityID}" />
<c:choose>
	<c:when test="${forward.name == 'updatedOpportunityContactType'}">
		<c:redirect url="opportunity_details.jsp?opportunityID=${opportunityID}"/>
	</c:when>
</c:choose>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
<fmt:message key='sfa.message.selectPartnerContacts'/>
        </td>
          </tr>
          <tr>
          <td class="sfaRow">

	<c:set var="infoIndex" value="2" scope="request"/>
	<c:set var="opportunityID" value="${opportunityID}" scope="request"/>
	<jsp:include page="includes/navInfoOpportunity.jsp"/>
	</td>
          </tr>
          <tr>
          <td class="sfaRow">
	<x:display name="jsp_opportunityTiepartnercontact_2.form1"/>
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
