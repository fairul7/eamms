<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.crm.sales.misc.*, com.tms.crm.sales.model.*" %>
<%@page import="kacang.Application, kacang.services.security.*" %>




<c:if test="${!empty param.contactID}">
    <c:redirect url="/ekms/sfa/oppdetails_contact_view_commain.jsp?contactID=${param.contactID}"/>
</c:if>

<c:choose>
    <c:when test="${! empty param.opportunityID}" >
        <c:set var="opportunityID" value="${param.opportunityID}"/>
    </c:when>
    <c:otherwise>
        <c:set var="opportunityID" value="${widgets['jsp_opportunityDetails.productTable'].opportunityID}"/>
    </c:otherwise>
</c:choose>
<%
	NaviUtil naviUtil = new NaviUtil(pageContext); 
	naviUtil.getCompanyID4Opportunity("opportunityID", "companyID");
	naviUtil.getPartnerCompanyID4Opportunity("opportunityID", "partnerCompanyID");
	naviUtil.canCloseOpportunity("opportunityID", "status_CanClose");
	naviUtil.getStatus("opportunityID", "opportunityStatus");
	Integer opportunityStatus = (Integer) pageContext.getAttribute("opportunityStatus");
	
	User user = (User) session.getAttribute("currentUser");
	String userID = user.getId();
	String opportunityID = request.getParameter("opportunityID");
	boolean isSalesManager = AccessUtil.isSalesManager(userID);
	boolean isSalesAdmin = AccessUtil.isSalesAdmin(userID);
	boolean isOwner        = AccessUtil.isOpportunityOwner(userID, opportunityID);
	
	boolean canClose  = false;
	boolean canDelete = false;
	boolean canEdit   = false;
	boolean canEditDistribution = false;
	boolean canReassign = false;

	if (!opportunityStatus.equals(Opportunity.STATUS_CLOSE)) {
		if (isSalesManager || isSalesAdmin) {
			canDelete = true;
			canEdit   = true;
			canEditDistribution = true;
			canReassign = true;
		} else if (isOwner) {
			canEdit   = true;
		}
		
		boolean status_CanClose = ((Boolean) pageContext.getAttribute("status_CanClose")).booleanValue();
		if (status_CanClose && (isSalesManager || isOwner || isSalesAdmin)) {
			canClose  = true;
		}
	}
	

	if (opportunityStatus.equals(Opportunity.STATUS_CLOSE)) {
		if (isSalesManager || isSalesAdmin) {
			canDelete = true;
			canEdit   = true;
			canEditDistribution = true;
			canReassign = true;
		} else if (isOwner) {
			canEdit   = true;
		}
	}
%>
<x:config>
    <page name="jsp_opportunityDetails">
		<com.tms.crm.sales.ui.OpportunityProductTable name="productTable" template="sfa/Listing_Table" width="100%" linkUrl=""/>
		<com.tms.crm.sales.ui.AccountDistributionTable name="acDistributionTable" template="sfa/Listing_Table" width="100%" />
		<com.tms.crm.sales.ui.OpportunityArchiveTable name="oppArchiveTable"  width="100%" />
     	<com.tms.crm.sales.ui.SimpleCompanyContactTable name="companyContactTable" template="sfa/Listing_Table" width="100%" />
     	<com.tms.crm.sales.ui.ContactTable name="partnerContactTable" type="Partner_Contacts" subType="Filter_OpportunityContacts" template="sfa/Listing_Table" width="100%" />
    </page>
</x:config>

<x:set name="jsp_opportunityDetails.productTable"        property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_opportunityDetails.productTable"        property="linkUrl" value="opportunitydetails_product_view.jsp?opportunityID=${opportunityID}" />
<x:set name="jsp_opportunityDetails.acDistributionTable" property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_opportunityDetails.oppArchiveTable"     property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_opportunityDetails.companyContactTable" property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_opportunityDetails.partnerContactTable" property="opportunityID" value="${opportunityID}" />

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
   <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
<fmt:message key='sfa.message.opportunityDetails'/>

    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<form>
	    <% if (canClose) { %>
    	    <INPUT type="button" class="button" value="<fmt:message key='sfa.message.closethisSale'/>" onClick="location = 'closesale.jsp?opportunityID=<c:out value="${opportunityID}"/>'" />
<%--
    		<a href="closesale.jsp?opportunityID=<c:out value="${opportunityID}"/>"></a>
--%>
    	<% } %>

<c:if test="${not empty(opportunityID)}">
		<script>
		function deleteOpportunity(opportunityID) {
			if (confirm("<fmt:message key='sfa.message.deletetheopportunity'/> ?")) {
				location = 'deleteOpportunity.jsp?opportunityID=' + opportunityID;
			}
		}
	</script>
	<% if (canDelete) { %>
		<input type="button" class="button" value="<fmt:message key='sfa.message.delete'/>" onclick="deleteOpportunity('<c:out value="${opportunityID}"/>')">
	<% } %>
	</form>
	<c:set var="headerText" scope="request">
		<span class="sfaRowLabel"><fmt:message key='sfa.message.opportunityDetails'/></span>
		<span class="smallTitleStyle"><% if (canEdit) { %>| <a href='opportunity_edit.jsp?opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editOpportunityDetails'/></a><% } %></span>
	</c:set>
	<c:set var="opportunityID" value="${opportunityID}" scope="request"/>
	<jsp:include page="includes/form_opportunityView.jsp"/>
	<br>
     </td>
        </tr>
        <tr>
        <td class="sfaRow">

	<span class="sfaRowLabel"><fmt:message key='sfa.message.productsDistribution'/></span>
	<span class="smallTitleStyle"><% if (canEdit) { %>| <a href='opportunitydetails_product_list.jsp?opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editProductsDistribution'/></a><% } %></span>
	<x:display name="jsp_opportunityDetails.productTable"></x:display>
	<br><br>
 </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<span class="sfaRowLabel"><fmt:message key='sfa.message.accountDistribution'/></span>
	<span class="smallTitleStyle"><% if (canEditDistribution) { %>| <a href='opportunitydetails_acdistribution_edit.jsp?opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editAccountDistribution'/></a><% } %></span>
	<x:display name="jsp_opportunityDetails.acDistributionTable"></x:display>
	<br><br>
	
	<%-- // Code for setting archiveLimit --%>
	<c:set var="recordCount" value="${widgets['jsp_opportunityDetails.oppArchiveTable'].recordCount}" scope="request" />
	<%
		int archiveLimit = 5;
		String currLimit;
		
		boolean archiveHasMore = false;
		if (request.getParameter("noArchiveLimit") != null) {
			currLimit = String.valueOf(-1);
		} else {
			currLimit = String.valueOf(archiveLimit);
			Integer recordCount = (Integer) request.getAttribute("recordCount");
			if (recordCount.intValue() > archiveLimit) {
				archiveHasMore = true;
			}
		}
		request.setAttribute("currLimit", currLimit);
	%>
	<x:set name="jsp_opportunityDetails.oppArchiveTable" property="archiveLimit" value="${requestScope.currLimit}" />
 </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<span class="sfaRowLabel"><a name="Archive"><fmt:message key='sfa.message.archive'/></a></span>
	<% if (archiveHasMore) { %>
		<span class="smallTitleStyle">| <a href='opportunity_details.jsp?opportunityID=<c:out value="${opportunityID}"/>&noArchiveLimit=1#Archive'><fmt:message key='sfa.message.more'/></a></span>
	<% } %>
    <table border=0 cellpadding="0" cellspacing="0" width="100%" style="border:1px solid gray" >
    <tr><td>
	<x:display name="jsp_opportunityDetails.oppArchiveTable"></x:display>
    </td></tr>
    </table>
	<br><br>
 </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<c:set var="headerText" scope="request">
		<span class="sfaRowLabel"><fmt:message key='sfa.message.customerCompanyDetails'/></span>
		<span class="smallTitleStyle">
			<% if (canReassign) { %>
				| <a href='reassign_company.jsp?opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.reassignCompany'/></a>
			<% } %>
			| <a href='company_edit_3.jsp?companyID=<c:out value="${companyID}"/>&opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editCustomerCompanyDetails'/></a>
		</span>
	</c:set>
	<c:set var="companyID" value="${companyID}" scope="request"/>
	<jsp:include page="includes/form_companyView.jsp"/>
	<br>
 </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<span class="sfaRowLabel"><fmt:message key='sfa.message.customerContactDetails'/></span>
	<span class="smallTitleStyle"><% if (canEdit) { %>| <a href='contact_list_oppdetails.jsp?opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editContactDetails'/></a><% } %></span>
	<x:display name="jsp_opportunityDetails.companyContactTable"></x:display>
	
	
	<c:if test="${not empty(partnerCompanyID)}">
		<br><br>
 </td>
    </tr>
    <tr>
    <td class="sfaRow">
		<c:set var="headerText" scope="request">
			<span class="sfaRowLabel"><fmt:message key='sfa.message.partnerCompanyDetails'/></span>
			<span class="smallTitleStyle">
				<% if (canReassign) { %>
					| <a href='reassign_partner.jsp?opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.reassignPartner'/></a>
				<% } %>
				| <a href='company_edit_3.jsp?companyID=<c:out value="${partnerCompanyID}"/>&opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editPartnerDetails'/></a>
			</span>
		</c:set>
		<c:set var="companyID" value="${partnerCompanyID}" scope="request"/>
		<jsp:include page="includes/form_partnerView.jsp"/>
		<br>
 </td>
    </tr>
    <tr>
    <td class="sfaRow">

		<span class="sfaRowLabel"><fmt:message key='sfa.message.partnerContactDetails'/></span>
		<span class="smallTitleStyle"><% if (canEdit) { %>| <a href='partnercontact_list_oppdetails.jsp?opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editPartnerContactDetails'/></a><% } %></span>
		<c:set var="opportunityID" value="${opportunityID}" scope="request"/>
		<x:display name="jsp_opportunityDetails.partnerContactTable"></x:display>
	</c:if>
</c:if>  </td>
    </tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
