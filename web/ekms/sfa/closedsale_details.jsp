<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.crm.sales.misc.*, com.tms.crm.sales.model.*" %>
<%@page import="kacang.Application, kacang.services.security.*" %>




<c:set var="opportunityID" value="${param.opportunityID}"/>
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

	if (opportunityStatus.equals(Opportunity.STATUS_CLOSE)) {
		if (isSalesAdmin || isSalesManager) {
			canDelete = true;
			canEdit   = true;
			canEditDistribution = true;
			canReassign = true;
		} else if (isOwner) {
			canEdit   = true;
		}

		boolean status_CanClose = ((Boolean) pageContext.getAttribute("status_CanClose")).booleanValue();
		if (status_CanClose && (isSalesManager || isOwner)) {
			canClose  = true;
		}
	}
%>
<x:config>
    <page name="jsp_opportunityDetails">
		<com.tms.crm.sales.ui.ClosedSaleProductTable name="productTable" template="sfa/Listing_Table" width="100%" linkUrl=""/>
		<com.tms.crm.sales.ui.AccountDistributionTable name="acDistributionTable" template="sfa/Listing_Table" width="100%" />
<%--
		<com.tms.crm.sales.ui.OpportunityArchiveTable name="oppArchiveTable" template="sfa/Listing_Table" width="100%" />
--%>
     	<com.tms.crm.sales.ui.ClosedSaleCompanyContactTable name="companyContactTable" template="sfa/Listing_Table" width="100%" />
     	<com.tms.crm.sales.ui.ContactTable name="partnerContactTable" type="Partner_Contacts" subType="Filter_OpportunityContacts" template="sfa/Listing_Table" width="100%" />
    </page>
</x:config>
<x:set name="jsp_opportunityDetails.productTable"        property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_opportunityDetails.productTable"        property="linkUrl" value="opportunitydetails_product_view.jsp?opportunityID=${opportunityID}" />
<x:set name="jsp_opportunityDetails.acDistributionTable" property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_opportunityDetails.companyContactTable" property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_opportunityDetails.partnerContactTable" property="opportunityID" value="${opportunityID}" />

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
   <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='sfa.message.saleDetails'/>
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
			if (confirm("Delete the opportunity ?")) {
				location = 'deleteOpportunity.jsp?opportunityID=' + opportunityID;
			}
		}
	</script>
	<input type="button" class="button" value="Edit" onClick="location= 'closedSale_Edit.jsp?opportunityID=<c:out value="${opportunityID}"/>'"/>
	<% if (canDelete) { %>
		<input type="button" class="button" value="<fmt:message key='sfa.message.delete'/>" onclick="deleteOpportunity('<c:out value="${opportunityID}"/>')">
	<% } %></form>
	
	<c:set var="headerText" scope="request">
		<span class="sfaRowLabel"><fmt:message key='sfa.message.saleDetails'/></span>
		<span class="smallTitleStyle"><% if (canEdit) { %>| <a href='opportunity_edit.jsp?status=closed&opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editSaleDetails'/></a><% } %></span>
	</c:set>
	<c:set var="opportunityID" value="${opportunityID}" scope="request"/>
	<jsp:include page="includes/form_closedSaleView.jsp"/>
    <br>
    
    <br>
	<br>
     </td>
        </tr>
        <tr>
        <td class="sfaRow">

	<span class="sfaRowLabel"><fmt:message key='sfa.message.productsDistribution'/></span>
	<span class="smallTitleStyle"><% if (canEdit) { %>| <a href='opportunitydetails_product_list.jsp?status=closed&opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editProductsDistribution'/></a><% } %></span>
	<x:display name="jsp_opportunityDetails.productTable"></x:display>
	<br><br>
 </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<span class="sfaRowLabel"><fmt:message key='sfa.message.accountDistribution'/></span>
	<span class="smallTitleStyle"><% if (canEditDistribution) { %>| <a href='opportunitydetails_acdistribution_edit.jsp?status=closed&opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editAccountDistribution'/></a><% } %></span>
	<x:display name="jsp_opportunityDetails.acDistributionTable"></x:display>
	<br><br>
    </td>
    </tr>
	<%-- // Code for setting archiveLimit --%>
<%--	<c:set var="recordCount" value="${widgets['jsp_opportunityDetails.oppArchiveTable'].recordCount}" scope="request" />
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


    <tr>
    <td class="sfaRow">

	<span class="sfaRowLabel"><a name="Archive"><fmt:message key='sfa.message.archive'/></a></span>
	<% if (archiveHasMore) { %>
		<span class="smallTitleStyle">| <a href='opportunity_details.jsp?opportunityID=<c:out value="${opportunityID}"/>&noArchiveLimit=1#Archive'><fmt:message key='sfa.message.more'/></a></span>
	<% } %>
	<x:display name="jsp_opportunityDetails.oppArchiveTable"></x:display>
	<br><br>
 </td>
    </tr>
--%>
    <tr>
    <td class="sfaRow">

	<c:set var="headerText" scope="request">
		<span class="sfaRowLabel"><fmt:message key='sfa.message.customerCompanyDetails'/></span>
		<span class="smallTitleStyle">
			<% if (canReassign) { %>
				| <a href='reassign_company.jsp?status=closed&opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.reassignCompany'/></a>
			<% } %>
			| <a href='company_edit_3.jsp?status=closed&companyID=<c:out value="${companyID}"/>&opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editCustomerCompanyDetails'/></a>
		</span>
	</c:set>
	<c:set var="companyID" value="${companyID}" scope="request"/>
	<jsp:include page="includes/form_companyView.jsp"/>
	<br>
 </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<span class="sfaRowLabel"><fmt:message key='sfa.message.contactDetails'/></span>
	<span class="smallTitleStyle"><% if (canEdit) { %>| <a href='contact_list_oppdetails.jsp?status=closed&opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editContactDetails'/></a><% } %></span>
	<x:display name="jsp_opportunityDetails.companyContactTable"></x:display>


	<c:if test="${not empty(partnerCompanyID)}">
		<br><br>
 </td>
    </tr>
    <tr>
    <td class="sfaRow">
		<c:set var="headerText" scope="request">
			<span class="sfaRowLabel"><fmt:message key='sfa.message.partnerDetails'/></span>
			<span class="smallTitleStyle">
				<% if (canReassign) { %>
					| <a href='reassign_partner.jsp?status=closedopportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.reassignPartner'/></a>
				<% } %>
				| <a href='company_edit_3.jsp?status=closedcompanyID=<c:out value="${partnerCompanyID}"/>&opportunityID=<c:out value="${opportunityID}"/>'><fmt:message key='sfa.message.editPartnerDetails'/></a>
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
	</c:if>

	</td>
	</tr>
	</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>