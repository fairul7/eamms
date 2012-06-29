<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, kacang.ui.WidgetManager, kacang.services.security.*,
                kacang.Application" %>
<%@page import="com.tms.crm.sales.misc.*, com.tms.crm.sales.model.*, com.tms.crm.sales.ui.*" %>

<x:config>
    <page name="jsp_opportunityList">
     	<com.tms.crm.sales.ui.OpportunityTable name="table1" type="Company_Filter" width="100%" />
     	<com.tms.crm.sales.ui.OpportunityForm name="form1" type="Add" width="100%" />
    </page>
</x:config>

<%--
<c:if test="${widgets['jsp_opportunityList.form1'].contacts == null}">
--%>
    <x:set name="jsp_opportunityList.form1" property="contacts" value="${widgets['jsp_opportunityTiecontact.contactTypeForm'].contacts}" />
<%--
</c:if>
--%>

<c:choose>
	<c:when test="${not empty(param.companyID)}">
		<c:set var="companyID" value="${param.companyID}" />
	</c:when>
	<c:otherwise>
		<c:set var="companyID" value="${widgets['jsp_opportunityList.table1'].companyID}" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${not empty(param.first)}">
		<c:set var="firstTime" value="1" />
	</c:when>
	<c:otherwise>
		<c:set var="firstTime" value="0" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_opportunityList.table1" property="companyID" value="${companyID}" />
<x:set name="jsp_opportunityList.table1" property="linkUrl" value="opportunity_view.jsp" />
<x:set name="jsp_opportunityList.table1" property="firstTime" value="${firstTime}" />
<x:set name="jsp_opportunityList.form1" property="companyID" value="${companyID}" />
<c:choose>
	<c:when test="${forward.name == 'opportunityAdded'}">
		<c:redirect url="newopportunity_opportunityproduct_list.jsp?opportunityID=${widgets['jsp_opportunityList.form1'].justCreatedID}"/>
	</c:when>
	<c:when test="${forward.name == 'selectOpportunity'}">
		<c:set var="selOpportunity" value="${widgets['jsp_opportunityList.table1'].selectedOpportunityID}" />
		<%-- // check that the opportunity is incomplete --%>
		<%
			String selOpportunity = (String) pageContext.getAttribute("selOpportunity");
			NaviUtil naviUtil = new NaviUtil(pageContext);
			Opportunity opp = naviUtil.getOpportunity(selOpportunity);
			Integer status = opp.getOpportunityStatus();
			
			User user = (User) session.getAttribute("currentUser");
			String userID = user.getId();
			boolean isSalesManager = AccessUtil.isSalesManager(userID);
			boolean isOwner        = AccessUtil.isOpportunityOwner(userID, selOpportunity);
		%>
		<% if (status.equals(Opportunity.STATUS_INCOMPLETE) && (isSalesManager || isOwner)) { %>
		        <c:set var="contacts" value="${widgets['jsp_opportunityTiecontact.contactTypeForm'].contacts}"/>

                <%
                    OpportunityContactModule opConModule = (OpportunityContactModule) Application.getInstance().getModule(OpportunityContactModule.class);
                    opConModule.deleteOpportunityContacts(selOpportunity);
                    Collection contacts = (Collection)pageContext.getAttribute("contacts");
                    if(contacts!=null){
                        for (Iterator iterator = contacts.iterator(); iterator.hasNext();) {
                             //String contactId = (String)
                             OpportunityContact opCon = (OpportunityContact)iterator.next();;
                             opCon.setOpportunityID(selOpportunity);
//                             opCon.setOpportunityContactType(OpportunityContact.COMPANY_CONTACT);
 //                            opCon.setContactID(contactId);
  //                           opCon.setContactTypeID(OpportunityContact.DEFAULT);
                             opConModule.addOpportunityContact(opCon);
                     }
                    }
                %>
				<c:redirect url="newopportunity_opportunityproduct_list.jsp?opportunityID=${selOpportunity}"/>
		<% } else { %>
			<script>
				<% if (status != Opportunity.STATUS_INCOMPLETE) { %>
				alert("<fmt:message key='sfa.message.Incompleteopportunityonly'/>");
				<% } else { %>
				alert("<fmt:message key='sfa.message.ownopportunityonly'/>");
				<% } %>
				location = 'newopportunity_opportunity_list.jsp?companyID=<c:out value="${companyID}" />';
			</script>
		<% } %>
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

	<c:set var="navSelected" value="2" scope="request"/>
	<jsp:include page="includes/navAddOpportunity.jsp"/>

        </td>
        </tr>
        <tr>
        <td class="sfaRow">

	<c:set var="infoIndex" value="0" scope="request"/>
	<c:set var="companyID" value="${companyID}" scope="request"/>
	<jsp:include page="includes/navInfoOpportunity.jsp"/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<x:display name="jsp_opportunityList.table1"></x:display>
    </td>
    </tr>
    </table>
    <br>
   <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">

    <tr>
    <td class="sfaHeader">
    <fmt:message key='sfa.message.newOpportunity'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_opportunityList.form1"></x:display>
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
