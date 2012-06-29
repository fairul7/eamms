<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, kacang.ui.WidgetManager,
                com.tms.crm.sales.model.OpportunityModule,
                com.tms.crm.sales.model.Opportunity,
                kacang.Application" %>

<x:config>
    <page name="jsp_acEdit">
        <com.tms.crm.sales.ui.AccountDistributionForm name="form1" />
    </page>
</x:config>


<x:set name="jsp_acEdit.form1" property="opportunityID" value="${param.opportunityID}" />
<%
	WidgetManager wm = WidgetManager.getWidgetManager(request);
	AccountDistributionForm adForm = (AccountDistributionForm) wm.getWidget("jsp_acEdit.form1");
	request.setAttribute("opportunityID", adForm.getOpportunityID());
%>
<c:choose>
	<c:when test="${forward.name == 'distributionSaved'}">

    <%
        OpportunityModule opModule = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
        String oppId = (String)request.getAttribute("opportunityID");
        Opportunity opp = opModule.getOpportunity(oppId);
        if (opp.getOpportunityStatus().equals(Opportunity.STATUS_INCOMPLETE)) {
			boolean hasPartner = (opp.getHasPartner().equals("1") ? true : false);
			if (!hasPartner) {
				// set status to open
				opp.setOpportunityStatus(Opportunity.STATUS_OPEN);
				opModule.updateOpportunity(opp);
			}
		}

    %>
		        <c:redirect url="newopportunity_haspartner.jsp?opportunityID=${opportunityID}"/>

<%--
        <c:redirect url="opportunity_tiecontact.jsp?opportunityID=${opportunityID}" />
		<c:redirect url="contact_list.jsp?opportunityID=${opportunityID}"/>
--%>
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

	<c:set var="navSelected" value="4" scope="request"/>
	<jsp:include page="includes/navAddOpportunity.jsp"/>

    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<c:set var="infoIndex" value="1" scope="request"/>
    <c:set var="productsSelected" value="true" scope="request"/>
	<c:set var="opportunityID" value="${opportunityID}" scope="request"/>
	<jsp:include page="includes/navInfoOpportunity.jsp"/>

    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<x:display name="jsp_acEdit.form1"/>
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
